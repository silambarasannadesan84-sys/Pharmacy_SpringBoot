package com.example.demo.exception;

public class DuplicateSupplierException extends RuntimeException {
    public DuplicateSupplierException(String message) {
        super(message);
    }
}
