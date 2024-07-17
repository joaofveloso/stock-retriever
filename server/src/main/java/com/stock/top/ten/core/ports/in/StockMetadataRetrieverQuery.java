package com.stock.top.ten.core.ports.in;

import com.stock.top.ten.core.domain.StockData;
import reactor.core.publisher.Flux;

public interface StockMetadataRetrieverQuery {

    Flux<StockData.MetaData> execute();
}
