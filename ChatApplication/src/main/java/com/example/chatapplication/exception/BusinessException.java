package com.example.chatapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1;

    private String message;

    @Override
    public String getMessage() {
        return MessageFormat.format("{0}", this.message);
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public BusinessException(String message) {
        this(message, null);
    }


    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException() {
    }

    /**
     * ひもに
     */
    @Override
    public String toString() {
        return MessageFormat.format("{0}", this.message);
    }

}
