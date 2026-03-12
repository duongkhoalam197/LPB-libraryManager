package com.example.bookstore.exeption;

public class BorrowerNotFoundException extends BusinessException {
    public BorrowerNotFoundException(String message) {
        super(message, 404);
    }
}
