package com.example.bookstore.service.BorrowServices;

import com.example.bookstore.enums.TicketStatus;
import com.example.bookstore.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookBorrowCheckService implements CheckIsBorrowed {
    private final TicketRepository ticketRepository;
    @Override
    public boolean isBorrowed(Long bookId) {
        return ticketRepository.existsByBookIdAndTicketStatus(bookId, TicketStatus.ACTIVE);
    }
}
