package com.threadserver.service;

import com.threadserver.entity.ThreadEntity;
import com.threadserver.threads.ReceiverThread;
import com.threadserver.threads.SenderThread;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ThreadStarterService implements CommandLineRunner {

    private final ThreadService threadService;

    @Override
    public void run(String... args) throws Exception {
        List<ThreadEntity> threadEntities = threadService.findAllThreads();
        threadEntities.forEach((threadEntity -> {
            switch (threadEntity.getType()) {
                case SENDER -> {
                    Thread thread = new Thread(new SenderThread(threadEntity.getPriority()));
                    thread.setName("Thread " + threadEntity.getId());
                    thread.start();
                }
                case RECEIVER -> {
                    Thread thread = new Thread(new ReceiverThread(threadEntity.getPriority()));
                    thread.setName("Thread " + threadEntity.getId());
                    thread.start();
                }
            }
        }));

    }
}
