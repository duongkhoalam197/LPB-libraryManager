package com.example.bookstore.validation;

import com.example.bookstore.dto.ManageBookRequest;

public interface ManageBookRequestValidator extends RequestValidator<ManageBookRequest> {
    // marker + typed alias for autowiring List<ManageBookRequestValidator>
}
