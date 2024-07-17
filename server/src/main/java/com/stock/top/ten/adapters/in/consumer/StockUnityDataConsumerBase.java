package com.stock.top.ten.adapters.in.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.top.ten.core.domain.StockData;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component("unityConsumer")
public class StockUnityDataConsumerBase extends BaseGenericStockDataConsumer<StockData.StockUnity> {

    public StockUnityDataConsumerBase(ObjectMapper mapper) {
        super(mapper, StockData.StockUnity.class);
    }

    @JmsListener(destination = "stock.unity.queue")
    public void receiveMessage(String message) {
        super.receiveMessage(message);
    }
}