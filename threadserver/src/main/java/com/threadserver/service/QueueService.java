package com.threadserver.service;

import com.threadserver.dto.queue.QueueMetadata;
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
    public void produce(QueueMetadata data) throws InterruptedException{
        blockingQueue.put(data);
        // synchronize logging only
        synchronized (this) {
            log.info("Value produced: {}", data);
            messagingTemplate.convertAndSend("/topic/queueLog", "Produced");
        }
    }

    //consume the QueueMetadata from the queue
    public QueueMetadata consume() throws InterruptedException{
        Thread currentThread = Thread.currentThread();
        QueueMetadata data = blockingQueue.take();
        // synchronize logging only
        synchronized (this) {
            log.info("Value consumed: {} by thread : {}", data, currentThread.getName());
            messagingTemplate.convertAndSend("/topic/queueLog", "Produced");

        }
        return data;
    }

}
