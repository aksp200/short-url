package com.shorturl.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message){
        super(message);
    }

    public InvalidRequestException(String message, Throwable throwable){
        super(message, throwable);
    }
}
