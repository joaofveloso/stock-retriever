package com.stock.top.ten;

import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand;
import com.stock.top.ten.core.usecases.StockUnityCollectorUseCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

@Disabled("Skipping all tests in this class temporarily")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Import(Application.class)
public class ApplicationTest {

    @Mock
    private StockUnityCollectorUseCase stockUnityCollectorUseCase;

    @InjectMocks
    private Application application;

    @Test
    public void testInit() {
        StockUnityCollectorCommand.Data data = new StockUnityCollectorCommand.Data(LocalDate.now(), Collections.emptyList());

        Mockito.when(stockUnityCollectorUseCase.execute(data)).thenReturn(Mono.empty());

        application.init();

        Mockito.verify(stockUnityCollectorUseCase, Mockito.times(1)).execute(data);
    }
}