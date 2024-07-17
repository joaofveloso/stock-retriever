package com.stock.top.ten.core.usecases;

import com.stock.top.ten.configs.StockProperty;
import com.stock.top.ten.core.domain.StockData;
import com.stock.top.ten.core.ports.in.StockMetadataRetrieverQuery;
import com.stock.top.ten.core.ports.out.StockPersistenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockMetadataRetrieverUseCase implements StockMetadataRetrieverQuery {

    private final StockProperty stockProperty;
    private final StockPersistenceRepository stockPersistenceRepository;

    @Override
    public Flux<StockData.MetaData> execute() {

        Map<String, StockProperty.StockDetail> stockImages = stockProperty.getStockDetails();
        return stockPersistenceRepository.fetchAllStockMetadata().map(info -> {
            StockProperty.StockDetail stockDetail = stockImages.get(info.symbol().replace(".", ""));
            return info.withName(stockDetail.getName()).withImage(stockDetail.getImage());
        });
    }
}
