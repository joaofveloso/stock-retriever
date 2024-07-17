package com.stock.top.ten.core.ports.in;

import lombok.With;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface StockUnityCollectorCommand {

    Mono<Void> execute(Data data);

    record Data(LocalDate startDate, @With List<String> stocks){}
}
