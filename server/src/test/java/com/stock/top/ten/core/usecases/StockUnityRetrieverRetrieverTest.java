package com.stock.top.ten.core.usecases;

import com.stock.top.ten.aux.StockDetailStub;
import com.stock.top.ten.configs.StockProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.domain.StockPeriodRange;
import com.stock.top.ten.core.ports.in.StockUnityRetrieverQuery;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class StockUnityRetrieverRetrieverTest {

    @Mock
    private StockProperty stockProperty;

    @Mock
    private StockPersistenceRepository stockPersistenceRepository;

    @InjectMocks
    private StockUnityRetrieverRetriever stockUnityRetrieverRetriever;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        when(stockProperty.getStockDetails()).thenReturn(Map.of("AAPL", new StockDetailStub("Apple", "url")));
    }

    @Test
    public void execute_withValidData_shouldReturnStockData() {
        StockData stockData = new StockData(StockData.MetaData.builder().symbol("AAPL").build(), List.of());
        when(stockPersistenceRepository.fetchStockDataByDateRangeAndSymbol(any(), any(), any()))
                .thenReturn(Flux.just(stockData));

        StepVerifier.create(stockUnityRetrieverRetriever.execute(new StockUnityRetrieverQuery.Data(LocalDate.now(), "AAPL", StockPeriodRange.DAILY)))
                .expectNextMatches(data -> "Apple".equals(data.metaData().name()))
                .verifyComplete();
    }
}
