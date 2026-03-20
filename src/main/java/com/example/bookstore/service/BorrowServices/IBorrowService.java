package com.example.bookstore.service.BorrowServices;

import com.example.bookstore.dto.BorrowBookRequest;
import com.example.bookstore.dto.TicketRequest;
import com.example.bookstore.dto.TicketResponse;
import com.example.bookstore.dto.TicketResult;

public interface IBorrowService {
    TicketResult borrowBook(BorrowBookRequest request);
    TicketResult cancelTicket(TicketRequest request);
}
