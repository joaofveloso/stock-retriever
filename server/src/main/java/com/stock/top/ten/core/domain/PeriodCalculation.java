package com.stock.top.ten.core.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodCalculation {

    public static LocalDate calculateStartDate(LocalDate date, StockPeriodRange frequency) {
        return switch (frequency) {
            case DAILY -> date;
            case WEEKLY -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            case MONTHLY -> date.with(TemporalAdjusters.firstDayOfMonth());
            case QUARTERLY -> {
                Month currentMonth = date.getMonth();
                int currentQuarter = (currentMonth.getValue() - 1) / 3 + 1;
                yield LocalDate.of(date.getYear(), (currentQuarter - 1) * 3 + 1, 1);
            }
            case ANNUALLY -> date.with(TemporalAdjusters.firstDayOfYear());
        };
    }

    public static LocalDate calculateEndDate(LocalDate date, StockPeriodRange frequency) {
        return switch (frequency) {
            case DAILY -> date;
            case WEEKLY -> date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            case MONTHLY -> date.with(TemporalAdjusters.lastDayOfMonth());
            case QUARTERLY -> {
                Month currentMonth = date.getMonth();
                int currentQuarter = (currentMonth.getValue() - 1) / 3 + 1;
                yield LocalDate.of(date.getYear(), currentQuarter * 3, 1).with(TemporalAdjusters.lastDayOfMonth());
            }
            case ANNUALLY -> date.with(TemporalAdjusters.lastDayOfYear());
        };
    }
}
