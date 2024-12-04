package com.clothesstore.clothesstore.service.exception;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException(String message) {
        super(message);
    }
}
