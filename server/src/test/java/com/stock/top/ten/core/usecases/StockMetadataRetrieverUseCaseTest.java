package com.stock.top.ten.core.usecases;

import com.stock.top.ten.aux.StockDetailStub;
import com.stock.top.ten.configs.StockProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.when;

public class StockMetadataRetrieverUseCaseTest {

    @Mock
    private StockProperty stockProperty;

    @Mock
    private StockPersistenceRepository stockPersistenceRepository;

    @InjectMocks
    private StockMetadataRetrieverUseCase stockMetadataRetrieverUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stockProperty.getStockDetails()).thenReturn(Map.of("AAPL", new StockDetailStub("Apple", "url")));
    }

    @Test
    public void execute_withValidData_shouldReturnStockMetaData() {
        StockData.MetaData metaData = StockData.MetaData.builder().symbol("AAPL").build();
        when(stockPersistenceRepository.fetchAllStockMetadata())
                .thenReturn(Flux.just(metaData));

        StepVerifier.create(stockMetadataRetrieverUseCase.execute())
                .expectNextMatches(data -> "Apple".equals(data.name()))
                .verifyComplete();
    }
}