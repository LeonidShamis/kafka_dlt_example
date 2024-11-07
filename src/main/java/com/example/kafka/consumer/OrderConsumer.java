package com.example.kafka.consumer;

import com.example.kafka.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {
    
    @KafkaListener(topics = "orders-topic")
    public void processOrder(Order order) {
        log.info("Processing order: {}", order);
        
        // Simulate processing failure for orders with amount > 1000
        if (order.getAmount() > 1000) {
            throw new RuntimeException("Unable to process high-value order: " + order.getOrderId());
        }
        
        // Process the order
        order.setStatus("PROCESSED");
        log.info("Order processed successfully: {}", order);
    }
    
    @KafkaListener(topics = "orders-topic.DLT")
    public void processFailedOrder(Order order) {
        log.error("Processing failed order from DLT: {}", order);
        // Handle failed order (e.g., save to database, send notification, etc.)
    }
}
