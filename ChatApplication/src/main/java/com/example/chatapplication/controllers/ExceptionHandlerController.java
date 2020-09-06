package com.example.chatapplication.controllers;

import com.example.chatapplication.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BusinessException.class)
    public void handlerBusinessException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/signin");
    }


    @ExceptionHandler(Exception.class)
    public void handlerAccessDeniedException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Has error from server");
    }
}
