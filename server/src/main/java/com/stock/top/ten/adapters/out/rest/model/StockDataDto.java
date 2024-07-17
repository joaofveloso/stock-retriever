package com.stock.top.ten.adapters.out.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.top.ten.core.domain.StockData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record StockDataDto(
        @JsonProperty("Meta Data")
        MetaData metaData,
        @JsonProperty("Time Series (Daily)")
        Map<String, DailyData> timeSeriesDaily) {
    public record MetaData(
            @JsonProperty("1. Information")
            String information,
            @JsonProperty("2. Symbol")
            String symbol,
            @JsonProperty("3. Last Refreshed")
            String lastRefreshed,
            @JsonProperty("4. Output Size")
            String outputSize,
            @JsonProperty("5. Time Zone")
            String timeZone
    ) {}

    public record DailyData(
            @JsonProperty("1. open")
            String open,
            @JsonProperty("2. high")
            String high,
            @JsonProperty("3. low")
            String low,
            @JsonProperty("4. close")
            String close,
            @JsonProperty("5. volume")
            String volume
    ) {}

    public StockData toDomain() {

        StockData.MetaData metadata = StockData.MetaData.builder()
                .information(metaData().information)
                .symbol(metaData.symbol)
                .lastRefreshed(LocalDate.parse(metaData.lastRefreshed))
                .outputSize(metaData().outputSize)
                .timeZone(metaData.timeZone)
                .build();

        Function<String, BigDecimal> toDecimal = text -> text == null ? null : new BigDecimal(text);

        List<StockData.StockUnity> dailyStock = timeSeriesDaily().entrySet().stream()
                .map(m -> StockData.StockUnity.builder()
                        .date(LocalDate.parse(m.getKey()))
                        .open(toDecimal.apply(m.getValue().open))
                        .high(toDecimal.apply(m.getValue().high))
                        .low(toDecimal.apply(m.getValue().low))
                        .close(toDecimal.apply(m.getValue().close))
                        .volume(Long.parseLong(m.getValue().volume))
                        .build())
                .toList();

        return new StockData(metadata, dailyStock);
    }
}
