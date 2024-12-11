package com.threadserver.service;

import com.threadserver.entity.ThreadEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ThreadInitializerService implements CommandLineRunner {
    private final ThreadService threadService;
    private final QueueService queueService;
    //find db records for threads, then start them in the ThreadService
    @Override
    public void run(String... args) throws Exception {
        List<ThreadEntity> threadEntities = threadService.findAllThreads();
        threadEntities.forEach((threadService::startThread));
        // send initial statistics to websocket too
        queueService.sendQueueStatistics();

    }

}
