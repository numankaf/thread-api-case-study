package com.threadserver.dto.thread;

import com.threadserver.enums.ThreadType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ThreadCreateDto {
    @NotNull(message = "Thread number cannot be null.")
    @Min(value = 0, message = "Thread number should be at least 1.")
    @Max(value = 100, message = "Thread number should be less then 100.")
    private int threadNumber;


    @NotNull(message = "Thread type cannot be null.")
    private ThreadType threadType;
}
