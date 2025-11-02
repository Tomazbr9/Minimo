package com.tomazbr9.linkshort.exception;

public class PermissionDeniedToAccessResourceException extends RuntimeException{

    public PermissionDeniedToAccessResourceException(String message){
        super(message);
    }
}
