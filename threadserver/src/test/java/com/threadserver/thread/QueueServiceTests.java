package com.threadserver.thread;

import com.threadserver.dto.queue.QueueMetadata;
import com.threadserver.service.QueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QueueServiceTests {
    @Autowired
    private QueueService queueService;

    private QueueMetadata metadata;


    @BeforeEach
    void setUp() {
        QueueMetadata data = QueueMetadata.builder()
                .source("Test Thread")
                .data(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE))
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        metadata = data;
    }

    @Test
    public void shouldProduceNewQueueMetadata() throws InterruptedException {
        int capacityBefore = queueService.getRemainingCapacity();
        queueService.produce(metadata);
        assertThat(capacityBefore - 1).isEqualTo(queueService.getRemainingCapacity());
    }

    @Test
    public void shouldConsumeQueueMetadata() throws InterruptedException {
        int capacityBefore = queueService.getRemainingCapacity();
        queueService.consume();
        assertThat(capacityBefore + 1).isEqualTo(queueService.getRemainingCapacity());
    }
}
