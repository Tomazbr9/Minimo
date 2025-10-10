package com.tomazbr9.minimo.exception;

public class PermissionDeniedToAccessResourceException extends RuntimeException{

    public PermissionDeniedToAccessResourceException(String message){
        super(message);
    }
}
