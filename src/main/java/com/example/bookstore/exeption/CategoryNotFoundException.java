package com.example.bookstore.exeption;

public class CategoryNotFoundException extends BusinessException{
    public CategoryNotFoundException(String message) {
        super(message, 404);
    }
}


