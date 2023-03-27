package com.mslfox.cloudStorageServices.exception;

import lombok.Builder;

@Builder
public class BadRequestException extends DetectedException {
     private  String message;
    public BadRequestException(String message) {
        super(message);
    }
}

