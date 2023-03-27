package com.mslfox.cloudStorageServices.security;

import com.mslfox.cloudStorageServices.model.Error.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String previousErrorMessage = (String) request.getAttribute("error-message");
        String currentErrorMessage = authException.getMessage();
        String errorMessage = previousErrorMessage != null && !previousErrorMessage.isEmpty() ?
                previousErrorMessage:
                currentErrorMessage;
        var errorResponse = ErrorResponse.builder().message(errorMessage).id(1L).build();
        response.getWriter().write(errorResponse.toJsonString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        SecurityContextHolder.clearContext();
    }
}