package com.threadserver.dto.queue;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@Builder
public class QueueMetadata {
    private Integer data;
    private String source;
    private Timestamp timestamp;
}
