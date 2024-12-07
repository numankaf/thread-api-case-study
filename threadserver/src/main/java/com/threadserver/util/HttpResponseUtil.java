package com.threadserver.util;

import com.threadserver.common.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class HttpResponseUtil {

    public static ResponseEntity<HttpResponse> createHttpErrorResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .message(message)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                        .build(), httpStatus);
    }

    public static ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .message(message)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                        .build(), httpStatus);
    }


}
