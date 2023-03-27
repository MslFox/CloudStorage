package com.mslfox.cloudStorageServices.advice;

import com.mslfox.cloudStorageServices.exception.BadRequestException;
import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.model.Error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DetectedExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse appExceptionHandler(BadRequestException e) {
        String errorMsg = e.getMessage();
        return new ErrorResponse(errorMsg, 1L);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse appExceptionHandler(InternalServerException e) {
        String errorMsg = e.getMessage();
        return new ErrorResponse(errorMsg, 1L);
    }
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse appExceptionHandler(Exception e) {
//        String errorMsg = e.getMessage();
//        System.out.println(errorMsg);
//        return new ErrorResponse(errorMsg, 1L);
//    }

}
