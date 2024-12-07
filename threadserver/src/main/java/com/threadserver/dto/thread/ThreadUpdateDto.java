package com.threadserver.dto.thread;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ThreadUpdateDto {
    @Min(value = 1, message = "Thread priority should be at least 1.")
    @Max(value = 10, message = "Thread priority should be less then 100.")
    private Integer priority;

    private Boolean isActive;
}
