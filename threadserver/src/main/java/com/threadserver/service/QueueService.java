package com.threadserver.service;

import com.threadserver.dto.queue.QueueMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final BlockingQueue<QueueMetadata> blockingQueue;

    public void produce(QueueMetadata data) throws InterruptedException{
        blockingQueue.put(data);
    }

    public QueueMetadata consume() throws InterruptedException{
        return blockingQueue.take();
    }

}
