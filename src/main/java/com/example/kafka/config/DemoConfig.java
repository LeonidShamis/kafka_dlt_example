package com.example.kafka.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DemoConfig {
    
    @Bean
    public boolean demoMode(Environment env) {
        return env.getProperty("demo", Boolean.class, false);
    }
    
    @Bean
    CommandLineRunner demoModeNotification(boolean demoMode) {
        return args -> {
            if (demoMode) {
                System.out.println("==============================================");
                System.out.println("Running in DEMO mode!");
                System.out.println("Random orders will be generated automatically.");
                System.out.println("20% chance of high-value orders (amount > 1000)");
                System.out.println("==============================================");
            }
        };
    }
}
