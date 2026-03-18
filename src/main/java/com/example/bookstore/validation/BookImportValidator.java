package com.example.bookstore.validation;

import com.example.bookstore.dto.ManageBookRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BookImportValidator implements RequestValidator<ManageBookRequest> {

    @Override
    public void validate(ManageBookRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }
        if (!StringUtils.hasText(request.getAuthor())) {
            throw new IllegalArgumentException("Author is required");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("CategoryId is required");
        }
        if (request.getPrice() != null && request.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        // add more business-specific checks here
    }
}
