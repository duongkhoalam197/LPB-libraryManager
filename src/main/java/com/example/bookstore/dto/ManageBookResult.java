package com.example.bookstore.dto;

import com.example.bookstore.enums.BookErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManageBookResult {
    private boolean success;
    private BookErrorCode errorCode;
    private String errorMessage;
    private ManageBookResponse data;  //payload
}