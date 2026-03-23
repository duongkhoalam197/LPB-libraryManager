package com.example.bookstore.validation;


import com.example.bookstore.dto.ManageBookRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("bookUpdateValidator")
public class BookUpdateValidator implements ManageBookRequestValidator {

    @Override
    public void validate(ManageBookRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.getTitle() != null && !StringUtils.hasText(request.getTitle())) {

            throw new IllegalArgumentException("Title must not be blank if provided");
        }
        if (request.getAuthor() != null && !StringUtils.hasText(request.getAuthor())) {
            throw new IllegalArgumentException("Author must not be blank if provided");
        }
        if (request.getCategoryId() != null && request.getCategoryId() <= 0) {
            throw new IllegalArgumentException("CategoryId must be positive if provided");
        }
        if (request.getPrice() != null && request.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
    }
}
