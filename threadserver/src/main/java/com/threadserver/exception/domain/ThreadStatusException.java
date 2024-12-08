package com.threadserver.exception.domain;

public class ThreadStatusException extends RuntimeException{
    public ThreadStatusException(String message){
        super(message);
    }
}
