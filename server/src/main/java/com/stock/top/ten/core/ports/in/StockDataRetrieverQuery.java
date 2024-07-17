package com.stock.top.ten.core.ports.in;

import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.domain.StockPeriodRange;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface StockDataRetrieverQuery {

    Flux<StockData> execute(Data data);

    record Data(LocalDate date, StockPeriodRange frequency){}
}
