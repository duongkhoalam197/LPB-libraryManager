package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;

import java.util.List;

public interface IBookService {
    ManageBookResponse importBook(ManageBookRequest request);
    List<BookResponse> listBook(Long categoryId);
    BookResponse getBookById(Long id);
    ManageBookResponse updateBook(Long id, ManageBookRequest request);
    ManageBookResponse deleteBook(Long id);
}
