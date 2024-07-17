package com.stock.top.ten.core.usecases;

import com.stock.top.ten.configs.StockProperty;
import com.stock.top.ten.core.domain.PeriodCalculation;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.domain.StockPeriodRange;
import com.stock.top.ten.core.ports.in.StockDataRetrieverQuery;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDataRetrieverUseCase implements StockDataRetrieverQuery {

    private final StockProperty stockProperty;
    private final StockPersistenceRepository stockPersistenceRepository;

    @Override
    public Flux<StockData> execute(Data data) {

        StockPeriodRange frequency = data.frequency() == null ? StockPeriodRange.DAILY : data.frequency();
        LocalDate startDate = PeriodCalculation.calculateStartDate(data.date(), frequency);
        LocalDate endDate = PeriodCalculation.calculateEndDate(data.date(), frequency);

        return stockPersistenceRepository.fetchStockDataByDateRange(startDate, endDate)
                .map(stock -> stock.withMetaData(
                        stock.metaData().withName(stockProperty.getStockDetails().get(removeDotFromSymbol(stock)).getName())));
    }

    private static String removeDotFromSymbol(StockData stock) {
        return stock.metaData().symbol().replace(".", "");
    }
}
