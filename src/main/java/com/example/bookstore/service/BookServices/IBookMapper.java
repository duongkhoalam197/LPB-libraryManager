package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.entity.Book;

public interface IBookMapper {
    BookResponse toResponse(Book book);
}