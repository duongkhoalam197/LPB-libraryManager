package com.example.bookstore.controller;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/list")
    public List<BookResponse> findAllBooks(@RequestParam(required = false) Long category_id){
        return bookService.listBook(category_id);
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
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }
}
