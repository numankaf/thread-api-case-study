package com.threadserver.threads;

import com.threadserver.constants.ThreadConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class SenderThread implements Runnable{
    private int priority;

    private volatile boolean running = true;
    public SenderThread(Integer priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (running) {
            try {
                log.info("Value produced by thread: {}", currentThread.getName());
                Thread.sleep(ThreadConstants.FREQUENCY_IN_MS);
            } catch (InterruptedException e) {
                log.warn("Thread {} interrupted. Exiting...", currentThread.getName());
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("An unexpected error occurred in thread {}: {}", currentThread.getName(), e.getMessage(), e);
            }
        }
    }

    public void stop() {
        running = false;
        log.info("Stopping SenderThread...");
    }
}
