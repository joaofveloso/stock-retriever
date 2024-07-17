package com.stock.top.ten.core.ports.out;

import com.stock.top.ten.core.domain.StockData;
import reactor.core.publisher.Mono;

public interface StockMessagingRepository {

    Mono<Void> sendMetadata(StockData.MetaData metadata);

    Mono<Void> sendDailyStock(StockData.StockUnity dailyStock);
}
