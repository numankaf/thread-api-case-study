package com.threadserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@RequiredArgsConstructor
public class QueueService {

    private BlockingQueue<String> queue;

    public void produce(String data) throws InterruptedException{
        queue.put(data);
    }

    public String consume() throws InterruptedException{
        return queue.take();
    }
}
