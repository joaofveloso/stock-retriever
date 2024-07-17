package com.stock.top.ten.core.ports.in;

import reactor.core.publisher.Flux;

public interface StockConsumerCommand {

    Flux<?> getMessageFlux();
}
