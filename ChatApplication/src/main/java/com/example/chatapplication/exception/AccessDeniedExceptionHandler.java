package com.example.chatapplication.exception;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Thread.sleep(3000);

        response.sendRedirect(request.getContextPath() + "/signin");
    }
}