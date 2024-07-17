package com.stock.top.ten.adapters.out.rest;

import com.stock.top.ten.adapters.out.rest.model.StockDataDto;
import com.stock.top.ten.configs.StockPriceCollectorProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.out.StockCollectorRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StockDailyCollectorAdapter implements StockCollectorRepository {

    private final WebClient webClient;
    private final StockPriceCollectorProperty property;

    public StockDailyCollectorAdapter(StockPriceCollectorProperty property) {
        int sizeInBytes = 16 * 1024 * 1024; // 16MB, adjust as needed

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(sizeInBytes))
                .build();

        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(property.getBaseUrl())
                .build();
        this.property = property;
    }

    @Override
    public Mono<StockData> obtainStocksInformation(String stock) {

        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "TIME_SERIES_DAILY")
                        .queryParam("symbol", stock)
                        .queryParam("apikey", property.getKey())
                        .queryParam("outputsize", "full")
                        .build())
                .retrieve()
                .bodyToMono(StockDataDto.class)
                .map(StockDataDto::toDomain);
    }
}
