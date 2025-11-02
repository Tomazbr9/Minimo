package com.tomazbr9.linkshort.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String message){
        super(message);
    }
}
