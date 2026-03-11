package com.example.bookstore.dto;


import lombok.Data;

@Data
public class ManageBookRequest {
    private String title;
    private String author;
    private Long price;
    private Long category_id;
}
