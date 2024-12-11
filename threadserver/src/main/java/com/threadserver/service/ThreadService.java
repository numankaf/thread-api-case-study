package com.threadserver.service;

import com.threadserver.constants.ExceptionConstants;
import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.exception.domain.ThreadNotFoundException;
import com.threadserver.exception.domain.ThreadStatusException;
import com.threadserver.repository.ThreadRepository;
import com.threadserver.threads.ReceiverThread;
import com.threadserver.threads.SenderThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThreadService {
    private final ThreadRepository threadRepository;
    private final ThreadMapProviderService threadMapProviderService;
    private final ApplicationContext applicationContext;

    //creates threads with the given number
    public List<ThreadEntity> createThreads(ThreadCreateDto threadCreateDto) {
        List<ThreadEntity> threads = new ArrayList<>();
        for (int i = 0; i < threadCreateDto.getThreadNumber(); i++) {
            ThreadEntity thread = ThreadEntity.builder()
                    .type(threadCreateDto.getThreadType())
                    .priority(1)
                    .isActive(true)
                    .build();
            threads.add(thread);
        }
        //save to database
        List<ThreadEntity> threadEntities = threadRepository.saveAll(threads);
        //start each thread
        threadEntities.forEach(this::startThread);
        log.info("Threads are created. ThreadNumber : {}, threadType : {}", threadCreateDto.getThreadNumber(), threadCreateDto.getThreadType());
        return threadEntities;
    }

    public void updateThread(Long id, ThreadUpdateDto threadUpdateDto) {
        ThreadEntity threadEntity = threadRepository.findById(id).orElseThrow(() -> new ThreadNotFoundException(ExceptionConstants.THREAD_NOT_FOUND + id));
        Thread thread = threadMapProviderService.getThread(id);

        //cant update priority if thread is not alive
        if (threadUpdateDto.getPriority() != null && !thread.isAlive()){
            throw new ThreadStatusException(ExceptionConstants.THEAD_IS_NOT_ALIVE);
        }

        //if priority is not null, update it.
        if (threadUpdateDto.getPriority() != null) {
            threadEntity.setPriority(threadUpdateDto.getPriority());
            thread.setPriority(threadUpdateDto.getPriority());
        }

        // if isActive not null, update it.
        if (threadUpdateDto.getIsActive() != null) {
            threadEntity.setIsActive(threadUpdateDto.getIsActive());
            // restart thread
            if (threadUpdateDto.getIsActive() && !thread.isAlive()) {
                startThread(threadEntity);
            }
            //stop thread
            if (!threadUpdateDto.getIsActive() && thread.isAlive()) {
                try {
                    thread.interrupt();
                    thread.join();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
        threadRepository.save(threadEntity);
        log.info("Thread is updated with id : {}", id);
    }

    //deletes current thread by id
    public void deleteThread(Long id) {
        ThreadEntity threadEntity = threadRepository.findById(id).orElseThrow(() -> new ThreadNotFoundException(ExceptionConstants.THREAD_NOT_FOUND + id));
        //delete from database
        threadRepository.delete(threadEntity);
        Thread thread = threadMapProviderService.getThread(id);
        //stop thread
        if (thread != null && thread.isAlive()) {
            try {
                thread.interrupt();
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        //delete from threadMap
        threadMapProviderService.removeThread(id);
    }

    public List<ThreadEntity> findAllThreads() {
        return threadRepository.findAllByOrderByIdDesc();
    }

    //starts thread if thread is active
    public void startThread(ThreadEntity entity) {
        Runnable runnable = switch (entity.getType()) {
            case SENDER -> applicationContext.getBean(SenderThread.class);
            case RECEIVER -> applicationContext.getBean(ReceiverThread.class);
        };
        Thread thread = new Thread(runnable);
        thread.setName("Thread " + entity.getId());
        thread.setPriority(entity.getPriority());
        if (entity.getIsActive()) {
            thread.start();
        }
        threadMapProviderService.putThread(entity.getId(), thread);
    }
}
