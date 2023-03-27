package com.mslfox.cloudStorageServices.exception;

import lombok.Builder;

@Builder
public class InternalServerException extends DetectedException{
    private String message;

    public InternalServerException(String message) {
        super(message);
    }
}