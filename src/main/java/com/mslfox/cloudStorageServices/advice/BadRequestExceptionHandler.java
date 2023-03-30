package com.mslfox.cloudStorageServices.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.exception.BadRequestException;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(BadRequestException e) {
        return new ErrorResponse(e.getMessage(), 1L);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ValidationException e) {
        return new ErrorResponse(e.getMessage(), 1L);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(BindException e) {
        var errors = new StringBuilder();
        errors.append(ConstantsHolder.ERROR_VALIDATION);
        for (FieldError error : e.getFieldErrors()) {
            errors.append(error.getField());
            errors.append(" : ");
            errors.append(error.getDefaultMessage());
        }
        return new ErrorResponse(errors.toString(), 1L);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(MissingServletRequestParameterException e) {
        return new ErrorResponse(e.getMessage(), 1L);
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(JsonProcessingException e) {
        log.error(e.getMessage());
        return new ErrorResponse(ConstantsHolder.ERROR_JSON_PROCESSING, 1L);
    }
}
