package com.stock.top.ten.adapters.in.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.top.ten.core.domain.StockData;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component("metadataConsumer")
public class StockMetadataConsumerBase extends BaseGenericStockDataConsumer<StockData.MetaData> {

    public StockMetadataConsumerBase(ObjectMapper mapper) {
        super(mapper, StockData.MetaData.class);
    }

    @JmsListener(destination = "stock.metadata.queue")
    public void receiveMessage(String message) {
        super.receiveMessage(message);
    }
}