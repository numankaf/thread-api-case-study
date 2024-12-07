package com.threadserver.controller;

import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread")
@RequiredArgsConstructor
public class ThreadController {
    private final ThreadService threadService;
    
    @PostMapping()
    public ResponseEntity<String> createThreads(@RequestBody ThreadCreateDto threadCreateDto){
        threadService.createThreads(threadCreateDto);
        return ResponseEntity.ok("Threads are created successfully");
    }

}
