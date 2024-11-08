package com.example.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaDltApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaDltApplication.class, args);
    }
}
