package com.tomazbr9.minimo.exception.handler;

import com.tomazbr9.minimo.dto.exceptionDTO.ErrorResponseDTO;
import com.tomazbr9.minimo.exception.PermissionDeniedToAccessResourceException;
import com.tomazbr9.minimo.exception.UrlAlreadyExistsException;
import com.tomazbr9.minimo.exception.UrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UrlAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUrlAlreadyException (UrlAlreadyExistsException exeption, HttpServletRequest request) {
        return buildErrorResponse(exeption.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUrlNotFoundException(UrlNotFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedToAccessResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handlePermissionDaniedToAccessResourceException(PermissionDeniedToAccessResourceException exception, HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), request.getRequestURI(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception exeption, HttpServletRequest request) {

        logger.error("Erro não tratado: ", exeption);

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