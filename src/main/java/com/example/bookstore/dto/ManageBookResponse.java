package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ManageBookResponse {
    private String status;
    private String message;
    private String bookTitle;

}
