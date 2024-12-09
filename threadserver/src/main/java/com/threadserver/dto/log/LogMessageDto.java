package com.threadserver.dto.log;

import com.threadserver.enums.LogType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogMessageDto {
    String message;
    LogType type;
}
