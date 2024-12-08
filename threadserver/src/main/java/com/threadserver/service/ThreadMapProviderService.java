package com.threadserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

// Spring Bean for storing and controlling threads in hashmap
@Component
@RequiredArgsConstructor
public class ThreadMapProviderService {
    // map for storing states of the threads
    private final Map<Long, Thread> threadMap;

    // adding a new thread to map
    public void putThread(Long threadId, Thread thread){
        threadMap.put(threadId, thread);
    }

    // removing thread from the map
    public void removeThread(Long threadId){
        threadMap.remove(threadId);
    }

    // get thread by id from the map
    public Thread getThread(Long threadId){
        return threadMap.get(threadId);
    }

}
