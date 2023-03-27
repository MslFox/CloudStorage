package com.mslfox.cloudStorageServices.advice;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.mslfox.cloudStorageServices.model.Error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JsonMappingExceptionHandler {
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse appExceptionHandler(JsonParseException e) {
        JsonLocation errorLocation = e.getLocation();
        String errorMsg = "Error occurred at line " + errorLocation.getLineNr() + ", column " + errorLocation.getColumnNr();
        return new ErrorResponse(errorMsg, 1L);
    }

}
