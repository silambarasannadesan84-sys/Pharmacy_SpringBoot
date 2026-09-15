package com.example.demo.exception;

public class DuplicateBatchNumberException extends RuntimeException {

    public DuplicateBatchNumberException(String message) {
        super(message);
    }
}
