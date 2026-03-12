package com.example.bookstore.controller;

import com.example.bookstore.dto.BorrowBookRequest;
import com.example.bookstore.dto.TicketRequest;
import com.example.bookstore.dto.TicketResponse;
import com.example.bookstore.enums.TicketStatus;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping("/borrow")
    public TicketResponse borrowResponse (@RequestBody BorrowBookRequest borrowBookRequest){
        return borrowService.borrowBook(borrowBookRequest);
    }

    @PostMapping("/cancelBorrow")
    public TicketResponse cancelBorrowResponse (@RequestBody TicketRequest ticketRequest){
        return borrowService.cancelTicket(ticketRequest);
    }
}
