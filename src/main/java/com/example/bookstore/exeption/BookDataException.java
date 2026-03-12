package com.example.bookstore.exeption;

public class BookDataException extends BusinessException {
    public BookDataException(String message) {
        super(message, 409);
    }
}