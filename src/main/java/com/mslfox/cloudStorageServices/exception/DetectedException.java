package com.mslfox.cloudStorageServices.exception;

public abstract class DetectedException extends RuntimeException {
    public DetectedException(String message) {
        super(message);
    }
}

