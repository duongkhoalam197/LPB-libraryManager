package com.example.bookstore.dto;


import lombok.Data;

@Data
public class ManageBookRequest {
    private String title;
    private String author;
    private Double price;
    private Long categoryId;
}
