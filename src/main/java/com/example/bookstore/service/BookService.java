package com.example.bookstore.service;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.enums.TicketStatus;
import com.example.bookstore.exeption.BookDataException;
import com.example.bookstore.exeption.BookNotFoundException;
import com.example.bookstore.exeption.CategoryNotFoundException;
import com.example.bookstore.exeption.ReferencedException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;

    private void validateImportRequest(ManageBookRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Author is required");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("CategoryId is required");
        }
    }

    @Transactional
    public ManageBookResponse importBook(ManageBookRequest request) {
        try {
            validateImportRequest(request);
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

            Book book = new Book();
            book.setAuthor(request.getAuthor());
            book.setTitle(request.getTitle());
            book.setPrice(request.getPrice());
            book.setCategory(category);
            Book savedBook = bookRepository.save(book);
            log.info("Import book successfully: {}", savedBook.getTitle());

            return new ManageBookResponse("SUCCESS", "Import book successfully", savedBook.getTitle());

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when importing book", e);
            throw new BookDataException("Cannot import book due to duplicate data or constraint violation");

        } catch (CategoryNotFoundException e) {
            throw e; //

        } catch (Exception e) {
            log.error("Unexpected error when importing book", e);
            throw new RuntimeException("Failed to import book: " + e.getMessage(), e);
        }
    }

    public List<BookResponse> listBook(Long categoryId) {
        try {
            List<Book> books;
            if (categoryId == null) {
                books = bookRepository.findAll();
            } else {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new CategoryNotFoundException("Category not found with id: " + categoryId));

                books = bookRepository.findByCategoryId(category.getId());
            }

            return books.stream()
                    .map(book -> {
                        Category category = book.getCategory();
                        return new BookResponse(
                                book.getId(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getPrice(),
                                category != null ? category.getId() : null,
                                category != null ? category.getName() : null
                        );
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Error while listing books with categoryId: {}", categoryId, e);
            throw new RuntimeException("Failed to list books: " + e.getMessage(), e);
        }
    }

    private BookResponse mapToResponse(Book book) {
        Category category = book.getCategory();

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null
        );
    }

    public BookResponse getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book id must not be null");
        }
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return mapToResponse(book);
    }

    @Transactional
    public ManageBookResponse updateBook(Long id, ManageBookRequest request) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPrice(request.getPrice());
            book.setCategory(category);
            Book updatedBook = bookRepository.save(book);

            return new ManageBookResponse(
                    "SUCCESS",
                    "Update book successfully",
                    updatedBook.getTitle()
            );

        } catch (DataIntegrityViolationException e) {
            throw new BookDataException("Data constraint violation");
        }
    }

    @Transactional
    public ManageBookResponse deleteBook(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

            boolean isBorrowed = ticketRepository
                    .existsByBookIdAndTicketStatus(id, TicketStatus.ACTIVE);

            if (isBorrowed) {
                throw new ReferencedException("Cannot delete book because it is currently borrowed");
            }

            bookRepository.delete(book);

            return new ManageBookResponse(
                    "SUCCESS",
                    "Delete book successfully",
                    book.getTitle()
            );

        } catch (DataIntegrityViolationException e) {
            throw new ReferencedException("Cannot delete book because it is being referenced by other records");
        }
    }

}
