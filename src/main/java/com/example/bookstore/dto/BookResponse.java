package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private Double price;

    private Long categoryId;
    private String categoryName;
}
