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
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/signin");
    }


    @ExceptionHandler(RuntimeException.class)
    public void handlerAccessDeniedException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/signin");
    }
}
