package com.example.kafka.producer;

import com.example.kafka.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void sendOrder(Order order) {
        kafkaTemplate.send("orders-topic", order.getOrderId(), order);
    }
}
