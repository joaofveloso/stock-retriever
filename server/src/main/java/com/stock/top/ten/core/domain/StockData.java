package com.stock.top.ten.core.domain;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record StockData(
        @With
        MetaData metaData,
        List<StockUnity> timeSeries) {

    @Builder
    public record MetaData(
            String information,
            String symbol,
            LocalDate lastRefreshed,
            String outputSize,
            String timeZone,
            @With
            String name,
            @With
            String image
    ) {}

    @Builder
    public record StockUnity(
            LocalDate date,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal close,
            long volume,
            @With
            String symbol
    ) {}
}
