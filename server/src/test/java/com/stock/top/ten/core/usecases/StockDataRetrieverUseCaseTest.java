package com.stock.top.ten.core.usecases;

import com.stock.top.ten.aux.StockDetailStub;
import com.stock.top.ten.configs.StockProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.domain.StockPeriodRange;
import com.stock.top.ten.core.ports.in.StockDataRetrieverQuery;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StockDataRetrieverUseCaseTest {

    @Mock
    private StockProperty stockProperty;

    @Mock
    private StockPersistenceRepository stockPersistenceRepository;

    @InjectMocks
    private StockDataRetrieverUseCase stockDataRetrieverUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stockProperty.getStockDetails()).thenReturn(Map.of("AAPL", new StockDetailStub("Apple", "url")));
    }

    @Test
    public void execute_withValidData_shouldReturnStockData() {
        StockData stockData = new StockData(StockData.MetaData.builder().symbol("AAPL").build(), List.of());
        when(stockPersistenceRepository.fetchStockDataByDateRange(any(), any()))
                .thenReturn(Flux.just(stockData));

        StepVerifier.create(stockDataRetrieverUseCase.execute(new StockDataRetrieverQuery.Data(LocalDate.now(), StockPeriodRange.DAILY)))
                .expectNextMatches(data -> "Apple".equals(data.metaData().name()))
                .verifyComplete();
    }
}
