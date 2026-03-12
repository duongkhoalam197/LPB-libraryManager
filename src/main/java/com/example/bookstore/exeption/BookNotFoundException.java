package com.example.bookstore.exeption;

public class BookNotFoundException extends BusinessException {
    public BookNotFoundException(String message) {
        super(message, 404);
    }
}