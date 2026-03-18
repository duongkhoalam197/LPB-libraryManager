package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class BookMapper implements IBookMapper{
    @Override
    public BookResponse toResponse(Book book) {
        Category c = book.getCategory();
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                c != null ? c.getId() : null,
                c != null ? c.getName() : null
        );
    }
}
