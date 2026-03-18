package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.exeption.BookDataException;
import com.example.bookstore.exeption.BookNotFoundException;
import com.example.bookstore.exeption.CategoryNotFoundException;
import com.example.bookstore.exeption.ReferencedException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.BorrowServices.BookBorrowCheckService;
import com.example.bookstore.service.BorrowServices.CheckIsBorrowed;
import com.example.bookstore.validation.BookImportValidator;
import com.example.bookstore.validation.ManageBookRequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService implements IBookService{
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final IBookEntityMapper bookEntityMapper;
    private final IBookMapper bookMapper;
    private final List<ManageBookRequestValidator> manageBookRequestValidators;
    private final CheckIsBorrowed bookBorrowCheckService;


    @Transactional
    @Override
    public ManageBookResponse importBook(ManageBookRequest request) {
        try {
            manageBookRequestValidators.forEach(v -> v.validate(request));
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

            Book book = bookEntityMapper.importFromRequest(request, category); // ADDED
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

    @Override
    public List<BookResponse> listBook(Long categoryId) {
        try {
            List<Book> books;
            if (categoryId == null) {
                books = bookRepository.findAll();
            } else {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
                books = bookRepository.findByCategoryId(category.getId());
            }
            return books.stream()
                    .map(bookMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Error while listing books with categoryId: {}", categoryId, e);
            throw new RuntimeException("Failed to list books: " + e.getMessage(), e);
        }
    }

    @Override
    public BookResponse getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book id must not be null");
        }
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return bookMapper.toResponse(book);
    }

    @Transactional
    @Override
    public ManageBookResponse updateBook(Long id, ManageBookRequest request) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + request.getCategoryId()));

            bookEntityMapper.updateFromRequest(book, request, category);

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
    @Override
    public ManageBookResponse deleteBook(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

            boolean isBorrowed = bookBorrowCheckService.isBorrowed(id);

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
