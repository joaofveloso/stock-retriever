package com.stock.top.ten.core.usecases;

import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.in.StockConsumerCommand;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDataProcessingUseCase {

    private final StockPersistenceRepository stockPersistenceRepository;

    private final StockConsumerCommand metadataConsumer;
    private final StockConsumerCommand unityConsumer;

    @PostConstruct
    void postConstructor() {
        metadataConsumer.getMessageFlux()
                .flatMap(this::process)
                .subscribe();
        unityConsumer.getMessageFlux()
                .bufferTimeout(100, Duration.ofSeconds(1))
                .flatMap(this::process)
                .subscribe();
    }

    Mono<Void> process(Object value) {
        if (value instanceof StockData.MetaData metadata) {
            return stockPersistenceRepository.save(metadata)
                    .doOnError(error -> log.error("Error saving metadata: ", error));
        } else if (value instanceof List<?> list && !list.isEmpty() && list.getFirst() instanceof StockData.StockUnity) {
            @SuppressWarnings("unchecked")
            List<StockData.StockUnity> unityList = (List<StockData.StockUnity>) list;
            return stockPersistenceRepository.save(unityList);
        }
        return Mono.empty();
    }
}