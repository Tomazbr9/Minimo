package com.tomazbr9.linkshort.exception;

public class UrlAlreadyExistsException extends RuntimeException{

    public UrlAlreadyExistsException(String message){

        super(message);
    }
}
