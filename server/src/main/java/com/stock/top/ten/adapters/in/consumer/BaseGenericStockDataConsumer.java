package com.stock.top.ten.adapters.in.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.top.ten.core.ports.in.StockConsumerCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;

@Slf4j
public class BaseGenericStockDataConsumer<T> implements StockConsumerCommand {

    private FluxSink<T> messageSink;
    @Getter
    private final Flux<T> messageFlux;
    private final ObjectMapper mapper;
    private final Class<T> type;

    public BaseGenericStockDataConsumer(ObjectMapper mapper, Class<T> type) {
        this.mapper = mapper;
        this.type = type;
        this.messageFlux = Flux.create(emitter -> this.messageSink = emitter, FluxSink.OverflowStrategy.BUFFER);
    }

    @JmsListener(destination = "#{__listener.destination}")
    public void receiveMessage(String message) {

        if (messageSink == null) {
            return;
        }
        deserialize(message).ifPresent(messageSink::next);
    }

    private Optional<T> deserialize(String value) {
        try {
            return Optional.of(mapper.readValue(value, type));
        } catch (JsonProcessingException e) {
            log.error("Error deserializing payload: {}", value, e);
            return Optional.empty();
        }
    }
}
