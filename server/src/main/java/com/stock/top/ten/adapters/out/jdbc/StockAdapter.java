package com.stock.top.ten.adapters.out.jdbc;

import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockAdapter implements StockPersistenceRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Void> save(StockData.MetaData message) {

        String sql = """
                INSERT INTO stock.stock_metadata (symbol, information, last_refreshed, output_size, time_zone)\s
                VALUES(:symbol, :information, :last_refreshed, :output_size, :time_zone)\s
                ON DUPLICATE KEY UPDATE last_refreshed = VALUES(last_refreshed)
                """;

        return databaseClient.sql(sql)
                .bind("symbol", message.symbol())
                .bind("information", message.information())
                .bind("last_refreshed", message.lastRefreshed())
                .bind("output_size", message.outputSize())
                .bind("time_zone", message.timeZone())
                .fetch()
                .rowsUpdated()
                .doOnError(error -> log.error("Error saving metadata: ", error))
                .then();
    }

    @Override
    public Mono<Void> save(List<StockData.StockUnity> stockUnities) {

        if (stockUnities.isEmpty()) {
            return Mono.empty();
        }

        String sql = """
                INSERT IGNORE INTO stock.daily_prices (id, date, open, high, low, close, volume, symbol)\s
                VALUES (:id, :date, :open, :high, :low, :close, :volume, :symbol)
                """;

        return Flux.fromIterable(stockUnities)
                .flatMap(stockUnity -> databaseClient.sql(sql)
                        .bind("id", 0)
                        .bind("date", stockUnity.date())
                        .bind("open", stockUnity.open())
                        .bind("high", stockUnity.high())
                        .bind("low", stockUnity.low())
                        .bind("close", stockUnity.close())
                        .bind("volume", stockUnity.volume())
                        .bind("symbol", stockUnity.symbol())
                        .fetch()
                        .rowsUpdated()
                        .doOnError(error -> log.error("Error saving stock unity: ", error)))
                .then();
    }

    @Override
    public Flux<StockData> fetchStockDataByDateRange(LocalDate startDate, LocalDate endDate) {
        return this.fetchStockDataByDateRangeAndSymbol(startDate, endDate, null);
    }

    @Override
    public Flux<StockData> fetchStockDataByDateRangeAndSymbol(LocalDate startDate, LocalDate endDate, String stock) {

        String sql = """
                SELECT si.symbol, si.information, si.last_refreshed, si.output_size, si.time_zone,\s
                dp.date, dp.open, dp.high, dp.low, dp.close, dp.volume, dp.symbol\s
                FROM stock.stock_metadata si\s
                INNER JOIN stock.daily_prices dp\s
                ON dp.symbol = si.symbol\s
                WHERE dp.date >= :start_date AND dp.date <= :end_date AND (:stock is null || si.symbol = :stock)
                """;

        DatabaseClient.GenericExecuteSpec spec =
                databaseClient.sql(sql).bind("start_date", startDate).bind("end_date", endDate);
        spec = stock != null ? spec.bind("stock", stock) : spec.bindNull("stock", String.class);

        return spec.fetch().all().collectList()
                .map(list -> list.stream().collect(Collectors.groupingBy(map -> String.valueOf(map.get("symbol")))))
                .map(result ->

                    result.keySet().stream().map(symbol -> {

                        StockData.MetaData metadata = result.get(symbol).stream().findFirst().map(r -> StockData.MetaData.builder()
                                        .symbol(String.valueOf(r.get("symbol")))
                                        .information(String.valueOf(r.get("information")))
                                        .lastRefreshed((LocalDate) r.get("last_refreshed"))
                                        .outputSize(String.valueOf(r.get("output_size")))
                                        .timeZone(String.valueOf(r.get("time_zone")))
                                        .build())
                                .orElseThrow();

                        List<StockData.StockUnity> stockPrice = result.get(symbol).stream().map(r -> StockData.StockUnity.builder()
                                        .date((LocalDate) r.get("date"))
                                        .open((BigDecimal) r.get("open"))
                                        .high((BigDecimal) r.get("high"))
                                        .low((BigDecimal) r.get("low"))
                                        .close((BigDecimal) r.get("close"))
                                        .volume((Integer) r.get("volume"))
                                        .symbol((String) r.get("symbol"))
                                        .build())
                                .toList();
                        return new StockData(metadata, stockPrice);
                }).toList())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<StockData.MetaData> fetchAllStockMetadata() {
        String sql = """
                SELECT si.symbol, si.information, si.last_refreshed, si.output_size, si.time_zone\s
                FROM stock.stock_metadata si\s
                ORDER BY si.symbol
                """;
        return databaseClient.sql(sql).fetch().all().map(metadataResult -> StockData.MetaData.builder()
                .symbol(String.valueOf(metadataResult.get("symbol")))
                .information(String.valueOf(metadataResult.get("information")))
                .lastRefreshed((LocalDate) metadataResult.get("last_refreshed"))
                .outputSize(String.valueOf(metadataResult.get("output_size")))
                .timeZone(String.valueOf(metadataResult.get("time_zone")))
                .build());
    }
}
