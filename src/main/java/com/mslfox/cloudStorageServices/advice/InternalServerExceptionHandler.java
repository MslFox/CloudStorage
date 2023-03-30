package com.mslfox.cloudStorageServices.advice;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class InternalServerExceptionHandler {
    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(InternalServerException e) {
        return new ErrorResponse(e.getMessage(), 1L);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(ConstantsHolder.INTERNAL_SERVER_ERROR, 1L);
    }

}
