package com.example.bookstore.controller;

import com.example.bookstore.dto.*;
import com.example.bookstore.service.BookServices.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;

    @PostMapping("/import")
    public ResponseEntity<APIResponse<ManageBookResponse>> importBook(
            @RequestBody ManageBookRequest request
    ) {
        ManageBookResult result = bookService.importBook(request);
        APIResponse<ManageBookResponse> body = new APIResponse<>();

        if (result.isSuccess()) {
            body.setStatus("SUCCESS");
            body.setCode("BOOK_IMPORTED");
            body.setMessage("Import book successfully");
            body.setData(result.getData());
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }

        body.setStatus("ERROR");
        HttpStatus status;
        String code;

        switch (result.getErrorCode()) {
            case CATEGORY_NOT_FOUND -> {
                status = HttpStatus.BAD_REQUEST;
                code = "CATEGORY_NOT_FOUND";
            }
            case BOOK_DUPLICATED -> {
                status = HttpStatus.CONFLICT;
                code = "BOOK_DUPLICATED";
            }
            default -> {
                status = HttpStatus.BAD_REQUEST;
                code = "BOOK_IMPORT_FAILED";
            }
        }
        body.setCode(code);
        body.setMessage(result.getErrorMessage());
        body.setData(null);
        return ResponseEntity.status(status).body(body);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<ManageBookResponse>> updateBook(
            @PathVariable Long id,
            @RequestBody ManageBookRequest request
    ) {
        ManageBookResult result = bookService.updateBook(id, request);
        APIResponse<ManageBookResponse> body = new APIResponse<>();

        if (result.isSuccess()) {
            body.setStatus("SUCCESS");
            body.setCode("BOOK_UPDATED");
            body.setMessage("Update book successfully");
            body.setData(result.getData());

            return ResponseEntity.ok(body);
        }

        body.setStatus("ERROR");

        HttpStatus status;
        String code;

        switch (result.getErrorCode()) {
            case BOOK_NOT_FOUND -> {
                status = HttpStatus.NOT_FOUND;
                code = "BOOK_NOT_FOUND";
            }
            case CATEGORY_NOT_FOUND -> {
                status = HttpStatus.BAD_REQUEST;
                code = "CATEGORY_NOT_FOUND";
            }
            case BOOK_DUPLICATED -> {
                status = HttpStatus.CONFLICT;
                code = "BOOK_DUPLICATED";
            }
            default -> {
                status = HttpStatus.BAD_REQUEST;
                code = "BOOK_UPDATE_FAILED";
            }
        }

        body.setCode(code);
        body.setMessage(result.getErrorMessage());
        body.setData(null);

        return ResponseEntity.status(status).body(body);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<ManageBookResponse>> deleteBook(@PathVariable Long id) {
        ManageBookResult result = bookService.deleteBook(id);

        APIResponse<ManageBookResponse> body = new APIResponse<>();

        if (result.isSuccess()) {
            body.setStatus("SUCCESS");
            body.setCode("BOOK_DELETED");
            body.setMessage("Delete book successfully");
            body.setData(result.getData());
            return ResponseEntity.ok(body);
        }
        body.setStatus("ERROR");

        HttpStatus status;
        String code;

        switch (result.getErrorCode()) {
            case BOOK_NOT_FOUND -> {
                status = HttpStatus.NOT_FOUND;
                code = "BOOK_NOT_FOUND";
            }
            case BOOK_BORROWED -> {
                status = HttpStatus.CONFLICT;
                code = "BOOK_BORROWED";
            }
            case BOOK_REFERENCED -> {
                status = HttpStatus.CONFLICT;
                code = "BOOK_REFERENCED";
            }
            default -> {
                status = HttpStatus.BAD_REQUEST;
                code = "BOOK_DELETE_FAILED";
            }
        }

        body.setCode(code);
        body.setMessage(result.getErrorMessage());
        body.setData(null);

        return ResponseEntity.status(status).body(body);
    }


    @GetMapping("/list")
    public ResponseEntity<APIResponse<List<BookResponse>>> findAllBooks(
            @RequestParam(required = false) Long categoryId
    ) {
        List<BookResponse> books = bookService.listBook(categoryId);
        APIResponse<List<BookResponse>> responseBody = new APIResponse<>();

        responseBody.setStatus("SUCCESS");
        responseBody.setCode("BOOK_LIST");
        responseBody.setMessage("List books successfully");
        responseBody.setData(books);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<APIResponse<BookResponse>> bookResponse(@PathVariable Long id) {
        BookResponse data = bookService.getBookById(id);
        APIResponse<BookResponse> body = new APIResponse<>();
        body.setStatus("SUCCESS");
        body.setCode("BOOK_FOUND");
        body.setMessage("Get book successfully");
        body.setData(data);
        return ResponseEntity.ok(body);
    }


}
