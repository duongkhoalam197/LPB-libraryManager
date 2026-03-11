package com.example.bookstore.service;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.exeption.BookNotFoundException;
import com.example.bookstore.exeption.CategoryNotFoundException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public ManageBookResponse importBook(ManageBookRequest request) {
        try {
            if (request.getTitle() == null || request.getTitle().isBlank()) {
                return new ManageBookResponse("FAILED", "Title is required", null);
            }
            if (request.getAuthor() == null || request.getAuthor().isBlank()) {
                return new ManageBookResponse("FAILED", "Author is required", null);
            }
            if (request.getCategory_id() == null) {
                return new ManageBookResponse("FAILED", "CategoryId is required", null);
            }
            Optional<Category> categoryOptional =
                    categoryRepository.findById(request.getCategory_id());
            if (categoryOptional.isEmpty()) {
                return new ManageBookResponse("FAILED", "Category not found", null);
            }

            Category category = categoryOptional.get();
            Book book = new Book();
            book.setAuthor(request.getAuthor());
            book.setTitle(request.getTitle());
            book.setPrice(request.getPrice());
            book.setCategory(category);

            Book savedBook = bookRepository.save(book);

            log.info("Import book successfully: {}", savedBook.getTitle());
            return new ManageBookResponse(
                    "SUCCESS",
                    "Import book successfully",
                    savedBook.getTitle()
            );
        } catch (Exception e) {
            log.error("Import book failed", e);
            return new ManageBookResponse(
                    "FAILED",
                    e.getMessage(),
                    null
            );
        }
    }

    public List<BookResponse> listBook(Long categoryId) {
        List<Book> books;
        if (categoryId == null) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.findByCategory_Id(categoryId);
        }
        return books.stream()
                .map(book -> new BookResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getCategory().getId(),
                        book.getCategory().getName()
                ))
                .toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getCategory().getId(),
                book.getCategory().getName()
        );
    }

    public ManageBookResponse updateBook(Long id, ManageBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        Category category = categoryRepository.findById(request.getCategory_id())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setCategory(category);

        Book updatedBook = bookRepository.save(book);
        // Trả DTO
        //Step: Tìm -> update giá trị -> ghi vào repo -> Từ bản ghi repo trả response/DTO
        return new ManageBookResponse("SUCCESS",
                "Update book successfully",
                updatedBook.getTitle());
    }

    public void deleteBook (Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        bookRepository.delete(book);

    }
}
