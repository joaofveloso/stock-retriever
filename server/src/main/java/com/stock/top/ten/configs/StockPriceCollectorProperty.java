package com.stock.top.ten.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.source.alpha-vantage")
public class StockPriceCollectorProperty {

    private String baseUrl;
    private String key;
    private int numberOfDays;
    private List<String> allowedStocks;
}