package com.threadserver.exception;

import com.threadserver.common.HttpResponse;
import com.threadserver.exception.domain.ThreadNotFoundException;
import com.threadserver.exception.domain.ThreadStatusException;
import com.threadserver.util.HttpResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        StringBuilder strBuilder = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            strBuilder.append(String.format("%s: %s\n", fieldName, message));
        });
        log.error(strBuilder.substring(0, strBuilder.length()-1));
        return HttpResponseUtil.createHttpErrorResponse(HttpStatus.BAD_REQUEST,strBuilder.substring(0, strBuilder.length()-1));
    }

    @ExceptionHandler(ThreadNotFoundException.class)
    public ResponseEntity<HttpResponse> handleThreadNotFoundException(ThreadNotFoundException exception) {
        log.error(exception.getMessage());
        return HttpResponseUtil.createHttpErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ThreadStatusException.class)
    public ResponseEntity<HttpResponse> handleThreadStatusException(ThreadStatusException exception) {
        log.error(exception.getMessage());
        return HttpResponseUtil.createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    //handle any exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpResponse> handleRuntimeException(Exception exception) {
        log.error(exception.getMessage());
        return HttpResponseUtil.createHttpErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
