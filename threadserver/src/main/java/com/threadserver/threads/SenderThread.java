package com.threadserver.threads;

import com.threadserver.constants.ThreadConstants;
import com.threadserver.dto.queue.QueueMetadata;
import com.threadserver.service.QueueService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Data
@Component
@Scope("prototype")
public class SenderThread implements Runnable{
    @Autowired
    private QueueService queueService;

    private volatile boolean running = true;

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (running) {
            try {
                Thread.sleep(ThreadConstants.FREQUENCY_IN_MS);
                QueueMetadata data= QueueMetadata.builder()
                        .source(currentThread.getName())
                        .data(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE))
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .build();
                queueService.produce(data);
                log.info("Value produced: {}", data.toString());
            } catch (InterruptedException e) {
                log.warn("Thread {} interrupted. Exiting...", currentThread.getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("An unexpected error occurred in thread {}: {}", currentThread.getName(), e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
        log.info("Stopping SenderThread...");
    }
}
