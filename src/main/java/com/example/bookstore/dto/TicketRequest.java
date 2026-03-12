package com.example.bookstore.dto;

import com.example.bookstore.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketRequest {
    private Long ticketId;
    private TicketStatus ticketStatus;
}
