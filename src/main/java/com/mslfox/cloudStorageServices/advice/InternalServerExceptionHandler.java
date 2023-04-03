package com.mslfox.cloudStorageServices.advice;

import com.mslfox.cloudStorageServices.exception.InternalServerException;
import com.mslfox.cloudStorageServices.messages.ErrorMessage;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
//@RestControllerAdvice
@Component
@RequiredArgsConstructor
public class InternalServerExceptionHandler {
    private final ErrorMessage errorMessage;

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(InternalServerException e) {
        return new ErrorResponse(e.getMessage(), 1L);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(errorMessage.internal, 1L);
    }

}
