package com.threadserver.threads;

import com.threadserver.constants.ThreadConstants;
import com.threadserver.service.QueueService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@Scope("prototype")
public class ReceiverThread implements Runnable{

    @Autowired
    private  QueueService queueService;

    private volatile boolean running = true;

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (running) {
            try {
                queueService.consume();
                Thread.sleep(ThreadConstants.FREQUENCY_IN_MS);
            } catch (InterruptedException e) {
                log.warn("ReceiverThread with name {} interrupted. Exiting...", currentThread.getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("An unexpected error occurred in thread {}: {}", currentThread.getName(), e.getMessage());
            }
        }
    }

    public void startThread(){
        Thread currentThread = Thread.currentThread();
        this.running = true;
        log.info("SenderThread with name : {} is stopped.",currentThread.getName() );
    }

    public void stopThread(){
        Thread currentThread = Thread.currentThread();
        this.running = false;
        log.info("ReceiverThread with name : {} is stopped.",currentThread.getName() );
    }
}
