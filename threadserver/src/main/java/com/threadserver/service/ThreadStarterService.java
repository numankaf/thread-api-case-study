package com.threadserver.service;

import com.threadserver.entity.ThreadEntity;
import com.threadserver.threads.ReceiverThread;
import com.threadserver.threads.SenderThread;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ThreadStarterService implements CommandLineRunner {
    private final ApplicationContext applicationContext;
    private final ThreadService threadService;

    @Override
    public void run(String... args) throws Exception {
        List<ThreadEntity> threadEntities = threadService.findAllThreads();
        threadEntities.forEach((threadEntity -> {
            switch (threadEntity.getType()) {
                case SENDER -> {
                    SenderThread senderThread = applicationContext.getBean(SenderThread.class);
                    Thread thread = new Thread(senderThread);
                    thread.setName("Thread " + threadEntity.getId());
                    //thread.setPriority(threadEntity.getPriority());
                    thread.start();
                }
                case RECEIVER -> {
                    ReceiverThread receiverThread = applicationContext.getBean(ReceiverThread.class);
                    Thread thread = new Thread(receiverThread);
                    thread.setName("Thread " + threadEntity.getId());
                    //thread.setPriority(threadEntity.getPriority());
                    thread.start();
                }
            }
        }));

    }
}
