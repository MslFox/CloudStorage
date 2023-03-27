package com.mslfox.cloudStorageServices.advice;

import com.mslfox.cloudStorageServices.model.Error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse appExceptionHandler(BindException e) {
        var errors = new StringBuilder();
        for (FieldError error : e.getFieldErrors()) {
            errors.append(error.getField());
            errors.append(" : ");
            errors.append(error.getDefaultMessage());
        }
        return new ErrorResponse(errors.toString(), 1L);
    }
}
