package com.tomazbr9.linkshort.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String message){
        super(message);
    }
}
