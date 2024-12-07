package com.threadserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MapConfig {
    @Bean
    public Map<Long, Thread> threadMap() {
        return new HashMap<>();
    }

}
