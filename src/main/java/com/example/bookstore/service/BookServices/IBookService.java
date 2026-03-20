package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.dto.ManageBookResult;

import java.util.List;

public interface IBookService {
    ManageBookResult importBook(ManageBookRequest request);
    List<BookResponse> listBook(Long categoryId);
    BookResponse getBookById(Long id);
    ManageBookResult updateBook(Long id, ManageBookRequest request);
    ManageBookResult deleteBook(Long id);
}
