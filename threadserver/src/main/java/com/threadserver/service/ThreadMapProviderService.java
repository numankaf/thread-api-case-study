package com.threadserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThreadMapProviderService {
    private final Map<Long, Thread> threadMap;

    public void putThread(Long threadId, Thread thread){
        threadMap.put(threadId, thread);
    }

    public void removeThread(Long threadId){
        threadMap.remove(threadId);
    }

    public Thread getThread(Long threadId){
        return threadMap.get(threadId);
    }

}
