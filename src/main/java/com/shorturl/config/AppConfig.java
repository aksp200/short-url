package com.shorturl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {
    @Bean
    ConcurrentHashMap<String,String> shortToLongMap(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    ConcurrentHashMap<String,String> longToShortMap(){
        return new ConcurrentHashMap<>();
    }
}
