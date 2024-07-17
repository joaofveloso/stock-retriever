package com.stock.top.ten.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.source")
public class StockProperty {

    private Map<String, StockDetail> stockDetails;

    @Getter
    @Setter
    public static class StockDetail {
        private String name;
        private String image;
    }
}
