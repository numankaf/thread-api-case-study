package com.threadserver.service;

import com.threadserver.constants.ExceptionConstants;
import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.exception.domain.ThreadNotFoundException;
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

    public void createThreads(ThreadCreateDto threadCreateDto) {
        List<ThreadEntity> threads = new ArrayList<>();
        for (int i = 0; i < threadCreateDto.getThreadNumber(); i++) {
            ThreadEntity thread = ThreadEntity.builder()
                    .type(threadCreateDto.getThreadType())
                    .priority(1)
                    .isActive(true)
                    .build();
            threads.add(thread);
        }
        List<ThreadEntity> threadEntities = threadRepository.saveAll(threads);
        threadEntities.forEach(this::startThread);
    }

    public void updateThread(Long id, ThreadUpdateDto threadUpdateDto){
        ThreadEntity threadEntity = threadRepository.findById(id).orElseThrow(() -> new ThreadNotFoundException(ExceptionConstants.THREAD_NOT_FOUND + id));

        threadRepository.save(threadEntity);
        Thread thread = threadMapProviderService.getThread(id);
    }

    public void deleteThread(Long id){
        ThreadEntity threadEntity = threadRepository.findById(id).orElseThrow(() -> new ThreadNotFoundException(ExceptionConstants.THREAD_NOT_FOUND + id));
        threadRepository.delete(threadEntity);
        Thread thread = threadMapProviderService.getThread(id);
        thread.interrupt();
        threadMapProviderService.removeThread(id);
    }

    public List<ThreadEntity> findAllThreads(){
        return threadRepository.findAll();
    }

    public void startThread(ThreadEntity entity) {
        Runnable runnable = switch (entity.getType()) {
            case SENDER -> applicationContext.getBean(SenderThread.class);
            case RECEIVER -> applicationContext.getBean(ReceiverThread.class);
        };

        Thread thread = new Thread(runnable);
        thread.setName("Thread " + entity.getId());
        thread.setPriority(entity.getPriority());
        if (entity.getIsActive()){
            thread.start();
        }
        threadMapProviderService.putThread(entity.getId(), thread);
    }


}
