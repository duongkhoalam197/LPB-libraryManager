package com.example.bookstore.exeption;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final int status;
    public BusinessException(String message, int status) {
        super(message);
        this.status = status;
    }

}
