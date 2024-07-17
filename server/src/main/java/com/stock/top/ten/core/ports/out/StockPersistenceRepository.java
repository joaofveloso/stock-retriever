package com.stock.top.ten.core.ports.out;

import com.stock.top.ten.core.domain.StockData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface StockPersistenceRepository {
    Mono<Void> save(StockData.MetaData metadata);

    Mono<Void> save(List<StockData.StockUnity> unity);

    Flux<StockData> fetchStockDataByDateRange(LocalDate startDate, LocalDate endDate);
    Flux<StockData> fetchStockDataByDateRangeAndSymbol(LocalDate date, LocalDate endDate, String stock);

    Flux<StockData.MetaData> fetchAllStockMetadata();
}
