package com.tomazbr9.minimo.exception.handler;

import com.tomazbr9.minimo.dto.exceptionDTO.ErrorResponseDTO;
import com.tomazbr9.minimo.exception.UrlAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUrlAlreadyException (UrlAlreadyExistsException exeption, HttpServletRequest request) {
        return buildErrorResponse(exeption.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception exeption, HttpServletRequest request) {
        return buildErrorResponse("Erro interno no servidor", request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(String message, String request, HttpStatus status) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                message,
                request,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

}