package com.example.bookstore.validation;

/**
 * Generic validator for request objects.
 * Throw runtime exceptions (IllegalArgumentException or custom) on invalid input.
 */
public interface RequestValidator<T> {
    void validate(T request);
}
