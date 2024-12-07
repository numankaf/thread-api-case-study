package com.threadserver.controller;

import com.threadserver.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread")
@RequiredArgsConstructor
public class ThreadController {
    private final ThreadService threadService;
}
