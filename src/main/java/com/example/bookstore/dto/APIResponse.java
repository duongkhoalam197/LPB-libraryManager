package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@JsonPropertyOrder({ "status", "code", "message", "data" })
public class APIResponse<T> {
    private String status;
    private String code;
    private String message;
    private T data;
}
