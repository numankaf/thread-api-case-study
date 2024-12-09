package com.threadserver.service;

import com.threadserver.dto.log.LogMessageDto;
import com.threadserver.dto.queue.QueueMetadata;
import com.threadserver.enums.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final BlockingQueue<QueueMetadata> blockingQueue;
    private final SimpMessageSendingOperations messagingTemplate;

    //produce a new QueueMetadata for the queue
    public void produce(QueueMetadata data) throws InterruptedException {
        blockingQueue.put(data);
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
    }

    //consume the QueueMetadata from the queue
    public QueueMetadata consume() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        QueueMetadata data = blockingQueue.take();
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
        return data;
    }

}
