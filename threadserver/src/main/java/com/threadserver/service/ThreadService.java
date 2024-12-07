package com.threadserver.service;

import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.entity.Thread;
import com.threadserver.repository.ThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThreadService {
    private final ThreadRepository threadRepository;

    public void createThreads(ThreadCreateDto threadCreateDto) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCreateDto.getThreadNumber(); i++) {
            Thread thread = Thread.builder()
                    .type(threadCreateDto.getThreadType())
                    .priority(0)
                    .isActive(true)
                    .build();
            threads.add(thread);
        }
        threadRepository.saveAll(threads);
    }
}
