package com.stock.top.ten.core.ports.out;

import com.stock.top.ten.core.domain.StockData;
import reactor.core.publisher.Mono;

public interface StockCollectorRepository {
    Mono<StockData> obtainStocksInformation(String stock);
}
