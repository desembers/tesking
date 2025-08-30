package com.example.tests.domain.common.exception;

public class InValidRequestException extends RuntimeException {
    public InValidRequestException(String message) {
        super(message);
    }
}
