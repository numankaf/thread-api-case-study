package com.threadserver.service;

import com.threadserver.constants.QueueConstants;
import com.threadserver.dto.log.LogMessageDto;
import com.threadserver.dto.queue.QueueMetadata;
import com.threadserver.dto.queue.QueueStatistics;
import com.threadserver.enums.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final BlockingQueue<QueueMetadata> blockingQueue;
    private final SimpMessageSendingOperations messagingTemplate;
    private final AtomicInteger totalProduced = new AtomicInteger(0);
    private final AtomicInteger totalConsumed = new AtomicInteger(0);

    public void sendQueueStatistics() {
        int currentSize = QueueConstants.CAPACITY - blockingQueue.remainingCapacity();

        QueueStatistics queueStatistics = QueueStatistics.builder()
                .remaining(blockingQueue.remainingCapacity())
                .currentSize(currentSize)
                .capacity(QueueConstants.CAPACITY)
                .totalConsumed(totalConsumed.get())
                .totalProduced(totalProduced.get())
                .build();

        messagingTemplate.convertAndSend("/topic/queueStatistics", queueStatistics);
    }

    //produce a new QueueMetadata for the queue
    public void produce(QueueMetadata data) throws InterruptedException {
        blockingQueue.put(data);
        totalProduced.incrementAndGet(); // Increment total consumed count
        Thread currentThread = Thread.currentThread();
        // synchronize logging only
        synchronized (this) {
            String logMessage = "Value %s produced by %s".formatted(data.toString(), currentThread.getName());
            log.info(logMessage);
            LogMessageDto logMessageDto = LogMessageDto.builder()
                    .message(logMessage)
                    .type(LogType.PRODUCE_MESSAGE)
                    .build();
            messagingTemplate.convertAndSend("/topic/queueLog", logMessageDto);
        }
        sendQueueStatistics();
    }

    //consume the QueueMetadata from the queue
    public QueueMetadata consume() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        QueueMetadata data = blockingQueue.take();
        totalConsumed.incrementAndGet(); // Increment total consumed count
        // synchronize logging only
        synchronized (this) {
            String logMessage = "Value %s consumed by %s".formatted(data.toString(), currentThread.getName());
            log.info(logMessage);
            LogMessageDto logMessageDto = LogMessageDto.builder()
                    .message(logMessage)
                    .type(LogType.CONSUME_MESSAGE)
                    .build();
            messagingTemplate.convertAndSend("/topic/queueLog", logMessageDto);

        }
        sendQueueStatistics();
        return data;
    }

    public int getRemainingCapacity(){
        return blockingQueue.remainingCapacity();
    }


}
