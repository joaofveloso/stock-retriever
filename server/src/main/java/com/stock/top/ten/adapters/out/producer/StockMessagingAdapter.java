package com.stock.top.ten.adapters.out.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.out.StockMessagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StockMessagingAdapter implements StockMessagingRepository {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper mapper;

    @Override
    public Mono<Void> sendMetadata(StockData.MetaData metadata) {
        return produceMessage("stock.metadata.queue", metadata);
    }

    @Override
    public Mono<Void> sendDailyStock(StockData.StockUnity dailyStock) {
        return produceMessage("stock.unity.queue", dailyStock);
    }

    private Mono<Void> produceMessage(String target, Object object) {

        try {
            String message = mapper.writeValueAsString(object);
            return Mono.fromRunnable(() -> jmsTemplate.convertAndSend(target, message));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
