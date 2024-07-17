package com.stock.top.ten.core.usecases;

import com.stock.top.ten.configs.StockPriceCollectorProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.exceptions.ApplicatioException;
import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand;
import com.stock.top.ten.core.ports.out.StockCollectorRepository;
import com.stock.top.ten.core.ports.out.StockMessagingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockUnityCollectorUseCase implements StockUnityCollectorCommand {

    private final StockPriceCollectorProperty property;
    private final StockCollectorRepository priceCollectorRepository;
    private final StockMessagingRepository stockMessagingRepository;

    @Override
    public Mono<Void> execute(Data data) {

        var newData = setDefaultStocksIfEmpty(data);

        List<String> allowedStocks = property.getAllowedStocks();
        List<String> inputStocks = newData.stocks();

        for (String stock : inputStocks) {
            if (!allowedStocks.contains(stock)) {
                return Mono.error(new ApplicatioException(format("Stock '%s' is not allowed", stock)));
            }
        }

        return Flux.fromIterable(newData.stocks()).flatMap(priceCollectorRepository::obtainStocksInformation)
                .flatMap(dailyStocks -> filterStockDataByDate(dailyStocks, newData.startDate()))
                .flatMap(this::produceMessages)
                .then();
    }

    private Data setDefaultStocksIfEmpty(Data data) {
        return data.withStocks(data.stocks().isEmpty() ? property.getAllowedStocks() : data.stocks());
    }

    private Mono<Void> produceMessages(StockData stockData) {
        return Mono.fromRunnable(() -> {
                    stockMessagingRepository.sendMetadata(stockData.metaData()).subscribe();
                    List<Mono<Void>> list = stockData.timeSeries().stream()
                            .map(item -> item.withSymbol(stockData.metaData().symbol()))
                            .map(stockMessagingRepository::sendDailyStock)
                            .toList();
                    Mono.when(list)
                            .doOnTerminate(() -> log.info(String.format("All messages published for %s stock",
                                    stockData.metaData().symbol())))
                            .subscribe();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
    private Mono<StockData> filterStockDataByDate(StockData stock, LocalDate localDate) {

        return Flux.fromIterable(stock.timeSeries())
                .filter(dailyStock -> isWithinDefinedPeriodRange(localDate, dailyStock))
                .collectList()
                .map(filteredDailyStock -> new StockData(stock.metaData(), filteredDailyStock));
    }

    private boolean isWithinDefinedPeriodRange(LocalDate localDate, StockData.StockUnity dailyStock) {
        return dailyStock.date().isBefore(localDate.plusDays(1))
                && dailyStock.date().isAfter(localDate.minusDays(property.getNumberOfDays() + 1));
    }
}