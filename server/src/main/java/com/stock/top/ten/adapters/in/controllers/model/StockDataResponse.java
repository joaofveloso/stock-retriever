package com.stock.top.ten.adapters.in.controllers.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stock.top.ten.core.domain.StockData;
import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StockDataResponse(
        MetaDataResponse metaData, List<StockUnityResponse> timeSeries) {

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MetaDataResponse(
            String symbol,
            String timeZone,
            String name,
            String image
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record StockUnityResponse(
            LocalDate date,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal close,
            long volume,
            @With
            String symbol
    ) {}

    public static StockDataResponse toResponse(StockData data) {

        MetaDataResponse metadataResult = toMetadataResponse(data.metaData());
        List<StockUnityResponse> timeSeries = toStockUnityResponse(data);
        return new StockDataResponse(metadataResult, timeSeries);
    }

    public static List<StockUnityResponse> toStockUnityResponse(StockData data) {
        return data.timeSeries().stream()
                .map(stock -> StockUnityResponse.builder()
                        .date(stock.date())
                        .open(stock.open())
                        .high(stock.high())
                        .low(stock.low())
                        .close(stock.low())
                        .volume(stock.volume())
                        .build())
                .toList();
    }

    public static MetaDataResponse toMetadataResponse(StockData.MetaData metaData) {
        return MetaDataResponse.builder()
                .symbol(metaData.symbol())
                .timeZone(metaData.timeZone())
                .name(metaData.name())
                .image(metaData.image())
                .build();
    }
}
