package com.example.chatapplication.controllers;

import com.example.chatapplication.exception.BusinessException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BusinessException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handlerBusinessException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/signin");
    }

    @ExceptionHandler(NullPointerException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handlerNullPointerException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Has error from server");
    }

    @ExceptionHandler(Exception.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public void handlerAccessDeniedException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Has error from server");
    }
}
