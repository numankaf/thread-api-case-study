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

    @Override
    public void run(String... args) throws Exception {
        List<ThreadEntity> threadEntities = threadService.findAllThreads();
        threadEntities.forEach((threadService::startThread));

    }

}
