package com.threadserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final BlockingQueue<String> blockingQueue;

    public void produce(String data) throws InterruptedException{
        blockingQueue.put(data);
        log.info("Remaining Capacity : {}",blockingQueue.remainingCapacity());
    }

    public String consume() throws InterruptedException{

        return blockingQueue.take();

    }
}
