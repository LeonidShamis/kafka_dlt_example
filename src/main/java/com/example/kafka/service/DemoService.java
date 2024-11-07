package com.example.kafka.service;

import com.example.kafka.model.Order;
import com.example.kafka.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "demo", havingValue = "true")
public class DemoService {

    private final OrderProducer orderProducer;
    private final Random random = new Random();

    @Scheduled(fixedDelayString = "${demo.delay:10000}")
    public void generateRandomOrder() {
        // Random delay between 10 and 30 seconds
        try {
            long additionalDelay = random.nextInt(20000); // 0-20 seconds
            TimeUnit.MILLISECONDS.sleep(additionalDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        Order order = generateOrder();
        log.info("Demo mode: Generated order: {}", order);
        orderProducer.sendOrder(order);
    }

    private Order generateOrder() {
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        boolean isHighValue = random.nextDouble() < 0.2; // 20% chance of high-value order
        
        double amount;
        if (isHighValue) {
            amount = 1000 + random.nextDouble() * 4000; // 1000-5000
        } else {
            amount = 100 + random.nextDouble() * 900; // 100-1000
        }
        
        return new Order(orderId, Math.round(amount * 100.0) / 100.0, "NEW");
    }
}
