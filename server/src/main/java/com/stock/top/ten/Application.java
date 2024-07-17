package com.stock.top.ten;

import com.stock.top.ten.core.ports.in.StockUnityCollectorCommand;
import com.stock.top.ten.core.usecases.StockUnityCollectorUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Collections;

@RequiredArgsConstructor
@SpringBootApplication
public class Application {

    private final StockUnityCollectorUseCase stockUnityCollectorUseCase;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        stockUnityCollectorUseCase.execute(new StockUnityCollectorCommand.Data(LocalDate.now(), Collections.emptyList())).subscribe();
    }
}