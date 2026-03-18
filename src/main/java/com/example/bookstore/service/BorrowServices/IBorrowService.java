package com.example.bookstore.service.BorrowServices;

import com.example.bookstore.dto.BorrowBookRequest;
import com.example.bookstore.dto.TicketRequest;
import com.example.bookstore.dto.TicketResponse;

public interface IBorrowService {
    TicketResponse borrowBook(BorrowBookRequest request);
    TicketResponse cancelTicket(TicketRequest request);
}
