package com.example.bookstore.dto;

import com.example.bookstore.enums.TicketErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResult {
    private boolean success;
    private TicketErrorCode errorCode; // null nếu success
    private String errorMessage;       // mô tả lỗi nếu failure
    private TicketResponse data;       // payload nếu success
}