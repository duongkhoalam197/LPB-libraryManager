package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ManageBookResponse {
    private String bookTitle;
    private String author;
    private Double price;
    private Long categoryId;
}
