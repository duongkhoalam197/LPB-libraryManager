package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.dto.ManageBookResult;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.enums.BookErrorCode;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.service.BorrowServices.CheckIsBorrowed;
import com.example.bookstore.validation.ManageBookRequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ManageBookResult importBook(ManageBookRequest request) {
        manageBookRequestValidators.forEach(v -> v.validate(request));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElse(null);
        if (category == null) {
            return new ManageBookResult(
                    false,
                    BookErrorCode.CATEGORY_NOT_FOUND,
                    "Category not found with id: " + request.getCategoryId(),
                    null
            );
        }
        try {
            Book book = bookEntityMapper.importFromRequest(request, category);
            Book savedBook = bookRepository.save(book);

            ManageBookResponse payload = new ManageBookResponse(savedBook.getTitle());
            return new ManageBookResult(
                    true,
                    null,
                    null,
                    payload
            );
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when importing book", e);
            return new ManageBookResult(
                    false,
                    BookErrorCode.BOOK_DUPLICATED,
                    "Book with this information already exists",
                    null
            );
        } catch (Exception e) {
            log.error("Unexpected error when importing book", e);
            throw new RuntimeException("Failed to import book: " + e.getMessage(), e);
        }
    }



    @Override
    public List<BookResponse> listBook(Long categoryId) {
        List<Book> books;

        if (categoryId == null) {
            books = bookRepository.findAll();
        } else {
            Category category = categoryRepository.findById(categoryId)
                    .orElse(null);
            if (category == null) {
                log.info("Category not found with id: {}, returning empty book list", categoryId);
                return List.of(); // Trả list rỗng, không ném exception
            }
            books = bookRepository.findByCategoryId(category.getId());
        }

        return books.stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    public BookResponse getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book id must not be null");
        }
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        return bookMapper.toResponse(book);
    }

    @Transactional
    @Override
    public ManageBookResult updateBook(Long id, ManageBookRequest request) {
        try {
            Book book = bookRepository.findById(id)
                    .orElse(null);
            if (book == null) {
                return new ManageBookResult(
                        false,
                        BookErrorCode.BOOK_NOT_FOUND,
                        "Book not found with id: " + id,
                        null
                );
            }

            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElse(null);
            if (category == null) {
                return new ManageBookResult(
                        false,
                        BookErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id: " + request.getCategoryId(),
                        null
                );
            }

            bookEntityMapper.updateFromRequest(book, request, category);
            Book updatedBook = bookRepository.save(book);

            ManageBookResponse payload = new ManageBookResponse(updatedBook.getTitle());
            return new ManageBookResult(
                    true,
                    null,
                    null,
                    payload
            );
        } catch (DataIntegrityViolationException e) {
            log.error("Data constraint violation when updating book", e);
            return new ManageBookResult(
                    false,
                    BookErrorCode.BOOK_DUPLICATED,
                    "Book with this information already exists",
                    null
            );
        } catch (Exception e) {
            log.error("Unexpected error when updating book", e);
            throw new RuntimeException("Failed to update book: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public ManageBookResult deleteBook(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElse(null);
            if (book == null) {
                return new ManageBookResult(
                        false,
                        BookErrorCode.BOOK_NOT_FOUND,
                        "Book not found with id: " + id,
                        null
                );
            }

            boolean isBorrowed = bookBorrowCheckService.isBorrowed(id);
            if (isBorrowed) {
                return new ManageBookResult(
                        false,
                        BookErrorCode.BOOK_BORROWED,
                        "Cannot delete book because it is currently borrowed",
                        null
                );
            }

            bookRepository.delete(book);

            ManageBookResponse payload = new ManageBookResponse(book.getTitle());
            return new ManageBookResult(
                    true,
                    null,
                    null,
                    payload
            );
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when deleting book", e);
            return new ManageBookResult(
                    false,
                    BookErrorCode.BOOK_REFERENCED,
                    "Cannot delete book because it is being referenced by other records",
                    null
            );
        } catch (Exception e) {
            log.error("Unexpected error when deleting book", e);
            throw new RuntimeException("Failed to delete book: " + e.getMessage(), e);
        }
    }
    
}
