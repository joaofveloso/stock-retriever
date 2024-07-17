package com.stock.top.ten.core.domain;

import java.util.Arrays;
import java.util.Optional;

public enum StockPeriodRange {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUALLY;

    public static Optional<StockPeriodRange> findByName(String value) {
        return Arrays.stream(StockPeriodRange.values()).filter(e -> e.name().equals(value)).findFirst();
    }
}