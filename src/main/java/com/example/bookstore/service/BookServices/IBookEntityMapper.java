package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;

public interface IBookEntityMapper {
    Book importFromRequest(ManageBookRequest req, Category category);
    void updateFromRequest(Book book, ManageBookRequest req, Category category);
}
