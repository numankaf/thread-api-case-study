package com.threadserver.exception.domain;

public class ThreadNotFoundException extends RuntimeException{
    public ThreadNotFoundException(String message){
        super(message);
    }
}
