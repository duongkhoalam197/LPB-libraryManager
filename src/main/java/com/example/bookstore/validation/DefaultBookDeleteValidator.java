package com.example.bookstore.validation;

import org.springframework.stereotype.Component;

@Component
public class DefaultBookDeleteValidator implements BookDeleteValidator {

    @Override
    public void validate(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book id must not be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Book id must be positive");
        }
        // thêm rule khác nếu cần
    }
}