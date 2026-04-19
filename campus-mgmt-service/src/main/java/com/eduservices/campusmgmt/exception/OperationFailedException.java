package com.eduservices.campusmgmt.exception;

public class OperationFailedException extends RuntimeException {
    
    public OperationFailedException(String message) {
        super(message);
    }
}