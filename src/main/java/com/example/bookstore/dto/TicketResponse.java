package com.example.bookstore.dto;
import com.example.bookstore.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TicketResponse {
    private String ticket_message;
    private String ticket_status_response;

}
