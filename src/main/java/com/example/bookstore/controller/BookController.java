package com.example.bookstore.controller;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.service.BookServices.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;

    @GetMapping("/list")
    public List<BookResponse> findAllBooks(@RequestParam(required = false) Long categoryId){
        return bookService.listBook(categoryId);
    }

    @PostMapping("/import")
    public ManageBookResponse importBook(@RequestBody ManageBookRequest request) {
        return bookService.importBook(request);
    }

    @GetMapping("/{id}")
    public BookResponse bookResponse(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PutMapping("/update/{id}")
    public ManageBookResponse updateBook(@PathVariable Long id, @RequestBody ManageBookRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ManageBookResponse deleteBook(@PathVariable Long id){
        return bookService.deleteBook(id);
    }
}
