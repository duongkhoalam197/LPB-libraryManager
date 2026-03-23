package com.example.bookstore.exeption;


import com.example.bookstore.dto.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Void>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());

        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode("BAD_REQUEST");
        body.setMessage(ex.getMessage());
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);

        String message = "Operation violates database constraints";
        String code = "DATA_INTEGRITY_VIOLATION";

        String exMessage = ex.getMessage();
        if (exMessage != null) {
            if (exMessage.contains("foreign key constraint")) {
                message = "Cannot delete because this record is referenced by other records";
                code = "FOREIGN_KEY_CONSTRAINT";
            } else if (exMessage.contains("Duplicate entry")) {
                message = "Record with this information already exists";
                code = "DUPLICATE_ENTRY";
            }
        }

        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode(code);
        body.setMessage(message);
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<APIResponse<Void>> handleNotFound(NoSuchElementException ex) {
        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode("NOT_FOUND");
        body.setMessage(ex.getMessage());
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation failed");

        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode("VALIDATION_FAILED");
        body.setMessage(errorMessage);
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<Void>> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());

        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode("MALFORMED_JSON");
        body.setMessage("Malformed JSON request");
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    //Ném all exeption không thuộc business exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleSystemException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        APIResponse<Void> body = new APIResponse<>();
        body.setStatus("ERROR");
        body.setCode("INTERNAL_SERVER_ERROR");
        body.setMessage("Internal server error");
        body.setData(null);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }


}

