package com.stock.top.ten;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @Value("${spring.r2dbc.url}")
    private String value;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void add() {
        System.out.println("\n\n\n\n\n\n\n" + value + "\n\n\n\n\n\n\n\n\n");
    }
}