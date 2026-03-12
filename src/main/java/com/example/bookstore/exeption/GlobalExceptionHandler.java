package com.example.bookstore.exeption;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            BookNotFoundException.class,
            CategoryNotFoundException.class,
            BorrowerNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


    @ExceptionHandler(ReferencedException.class)
    public ResponseEntity<String> handleBookReferenced(ReferencedException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);

        String message = "Operation violates database constraints";

        if (ex.getMessage().contains("foreign key constraint")) {
            message = "Cannot delete because this record is referenced by other records";
        } else if (ex.getMessage().contains("Duplicate entry")) {
            message = "Record with this information already exists";
        }
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(message);
    }

    //Ném all exeption không thuộc business exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSystemException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
    }
}