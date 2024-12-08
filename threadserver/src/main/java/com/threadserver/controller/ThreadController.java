package com.threadserver.controller;

import com.threadserver.common.HttpResponse;
import com.threadserver.dto.thread.ThreadCreateDto;
import com.threadserver.dto.thread.ThreadUpdateDto;
import com.threadserver.entity.ThreadEntity;
import com.threadserver.service.ThreadService;
import com.threadserver.util.HttpResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thread")
@RequiredArgsConstructor
public class ThreadController {
    private final ThreadService threadService;

    @GetMapping()
    public ResponseEntity<List<ThreadEntity>> findAllThreads(){
        return ResponseEntity.ok(threadService.findAllThreads());
    }

    @PostMapping()
    public ResponseEntity<HttpResponse> createThreads(@Valid @RequestBody ThreadCreateDto threadCreateDto){
        threadService.createThreads(threadCreateDto);
        return HttpResponseUtil.createHttpResponse(HttpStatus.CREATED,"Threads are created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpResponse> updateThread(@PathVariable Long id, @Valid @RequestBody ThreadUpdateDto threadUpdateDto) throws InterruptedException {
        threadService.updateThread(id, threadUpdateDto);
        return HttpResponseUtil.createHttpResponse(HttpStatus.OK,"Thread is updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }



}
