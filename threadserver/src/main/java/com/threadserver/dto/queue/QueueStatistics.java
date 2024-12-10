package com.threadserver.dto.queue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueueStatistics {
    private int remaining;
    private int capacity;
    private int currentSize;
    private int totalConsumed;
    private int totalProduced;
}
