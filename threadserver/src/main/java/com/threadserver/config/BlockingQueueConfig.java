package com.threadserver.config;

import com.threadserver.constants.QueueConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
public class BlockingQueueConfig {
    @Bean
    public BlockingQueue<String> blockingQueue() {
        return new ArrayBlockingQueue<>(QueueConstants.CAPACITY);
    }

}
