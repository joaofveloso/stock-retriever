package com.stock.top.ten.adapters.in.controllers;

import com.stock.top.ten.adapters.in.controllers.model.StockDataResponse;
import com.stock.top.ten.core.domain.StockPeriodRange;
import com.stock.top.ten.adapters.in.controllers.model.UpdateListOfStockRequest;
import com.stock.top.ten.core.ports.in.StockDataRetrieverQuery;
import com.stock.top.ten.core.ports.in.StockMetadataRetrieverQuery;
import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand;
import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand.Data;
import com.stock.top.ten.core.ports.in.StockUnityRetrieverQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Validated
@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StocksController {

    private final StockUnityRetrieverQuery stockUnityRetrieverQuery;
    private final StockUnityCollectorCommand stockUnityCollectorCommand;
    private final StockDataRetrieverQuery stockDataRetrieverQuery;
    private final StockMetadataRetrieverQuery stockMetadataRetrieverQuery;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateListOfStocks(@RequestBody UpdateListOfStockRequest requestBody) {
        Data data = new Data(requestBody.endDate(), requestBody.stocks());
        stockUnityCollectorCommand.execute(data)
                .doOnSuccess(m -> log.info("Successfully processed"))
                .doOnError(error -> log.error("Error processing data", error))
                .subscribe();
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockDataResponse> obtainListOfStocks(
            @RequestParam("date") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date,
            @RequestParam(value = "size", required = false) String frequency) {
        StockPeriodRange stockPeriodRange = StockPeriodRange.findByName(frequency).orElse(StockPeriodRange.WEEKLY);
        return stockDataRetrieverQuery.execute(new StockDataRetrieverQuery.Data(date, stockPeriodRange))
                .map(StockDataResponse::toResponse);
    }

    @GetMapping("/information")
    @ResponseStatus(HttpStatus.OK)
    public Flux<StockDataResponse.MetaDataResponse> obtainStocksInformation() {
        return stockMetadataRetrieverQuery.execute().map(StockDataResponse::toMetadataResponse);
    }

    @GetMapping("/{symbol}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<StockDataResponse> obtainStockUnityForSymbol(
            @PathVariable("symbol") String symbol,
            @RequestParam("date") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date,
            @RequestParam(value = "size", required = false) String frequency) {

        StockPeriodRange stockPeriodRange = StockPeriodRange.findByName(frequency).orElse(StockPeriodRange.DAILY);

        return stockUnityRetrieverQuery.execute(new StockUnityRetrieverQuery.Data(date, symbol, stockPeriodRange))
                .map(StockDataResponse::toResponse);
    }
}