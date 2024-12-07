package com.threadserver.dto.thread;

import com.threadserver.enums.ThreadType;
import lombok.Data;

@Data
public class ThreadCreateDto {
    private int threadNumber;
    private ThreadType threadType;
}
