package com.example.bookstore.dto;

import com.example.bookstore.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketRequest {
    private Long ticket_id;
    private String reason;
    private TicketStatus ticket_status;
}
