package com.example.bookstore.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class BorrowBookRequest {
    private Long book_id;
    private Long borrower_id;
    private Instant returnDate;
}
