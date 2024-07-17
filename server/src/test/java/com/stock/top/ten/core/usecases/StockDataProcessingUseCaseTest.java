package com.stock.top.ten.core.usecases;

import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.in.StockConsumerCommand;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StockDataProcessingUseCaseTest {

    @Mock
    private StockPersistenceRepository stockPersistenceRepository;

    @Mock
    private StockConsumerCommand metadataConsumer;

    @Mock
    private StockConsumerCommand unityConsumer;

    @InjectMocks
    private StockDataProcessingUseCase stockDataProcessingUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessMetaData() {
        StockData.MetaData metaData = StockData.MetaData.builder().symbol("AAPL").build();
        when(metadataConsumer.getMessageFlux()).thenReturn(Flux.empty());
        when(stockPersistenceRepository.save(any(StockData.MetaData.class))).thenReturn(Mono.empty());

        StepVerifier.create(stockDataProcessingUseCase.process(metaData))
                .verifyComplete();
    }

    @Test
    public void testProcessStockUnityList() {
        StockData.StockUnity stockUnity = StockData.StockUnity.builder().symbol("AAPL").build();
        List<StockData.StockUnity> unityList = List.of(stockUnity);
        when(unityConsumer.getMessageFlux()).thenReturn(Flux.empty());
        when(stockPersistenceRepository.save(any(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(stockDataProcessingUseCase.process(unityList))
                .verifyComplete();
    }

    @Test
    public void testProcessUnknownObject() {
        String unknownObject = "unknown";
        StepVerifier.create(stockDataProcessingUseCase.process(unknownObject))
                .verifyComplete();
    }
}
