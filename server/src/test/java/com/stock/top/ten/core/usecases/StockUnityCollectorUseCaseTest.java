package com.stock.top.ten.core.usecases;

import com.stock.top.ten.configs.StockPriceCollectorProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.exceptions.ApplicatioException;
import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand;
import com.stock.top.ten.core.ports.out.StockCollectorRepository;
import com.stock.top.ten.core.ports.out.StockMessagingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StockUnityCollectorUseCaseTest {

    @Mock
    private StockPriceCollectorProperty property;

    @Mock
    private StockCollectorRepository priceCollectorRepository;

    @Mock
    private StockMessagingRepository stockMessagingRepository;

    @InjectMocks
    private StockUnityCollectorUseCase stockUnityCollectorUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(property.getAllowedStocks()).thenReturn(List.of("AAPL", "MSFT"));
    }

    @Test
    public void execute_withValidStocks_shouldFilterStockDataByDateSuccessfully() {
        StockData stockData = new StockData(StockData.MetaData.builder().build(), List.of());
        when(priceCollectorRepository.obtainStocksInformation(any()))
                .thenReturn(Mono.just(stockData));
        when(stockMessagingRepository.sendMetadata(any())).thenReturn(Mono.empty());
        when(stockMessagingRepository.sendDailyStock(any())).thenReturn(Mono.empty());

        StepVerifier.create(stockUnityCollectorUseCase.execute(new StockUnityCollectorCommand.Data(LocalDate.now(), List.of("AAPL"))))
                .verifyComplete();
    }

    @Test
    public void execute_withInvalidStock_shouldThrowException() {
        StepVerifier.create(stockUnityCollectorUseCase.execute(new StockUnityCollectorCommand.Data(LocalDate.now(), List.of("INVALID"))))
                .expectError(ApplicatioException.class)
                .verify();
    }
}
