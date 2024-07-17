package com.stock.top.ten.core.ports.in;

import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.domain.StockPeriodRange;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface StockUnityRetrieverQuery {

    Mono<StockData> execute(Data data);

    record Data(LocalDate date, String stock, StockPeriodRange frequency){}
}
