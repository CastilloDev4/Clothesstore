package com.clothesstore.clothesstore.presentation.advice;


import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.exception.DuplicateNameException;
import com.clothesstore.clothesstore.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscountException.class)
    public ResponseEntity<Map<String, Object>> handleDiscountException(DiscountException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return buildErrorResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateNameException(DuplicateNameException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        return new ResponseEntity<>(errorDetails, status);
    }


}
