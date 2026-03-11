package com.example.bookstore.service;

import com.example.bookstore.dto.BorrowBookRequest;
import com.example.bookstore.dto.ManageBookResponse;
import com.example.bookstore.dto.TicketResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Borrower;
import com.example.bookstore.entity.Ticket;
import com.example.bookstore.enums.TicketStatus;
import com.example.bookstore.exeption.BookNotFoundException;
import com.example.bookstore.exeption.BorrowerNotFoundException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.BorrowerRepository;
import com.example.bookstore.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BookRepository bookRepository;
    private final TicketRepository ticketRepository;
    private final BorrowerRepository borrowerRepository;


    @Transactional
    public TicketResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        try {
            Book book = bookRepository.findById(borrowBookRequest.getBook_id())
                    .orElseThrow(() -> new BookNotFoundException("Book not found"));
            Borrower borrower = borrowerRepository.findById(borrowBookRequest.getBorrower_id())
                    .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found"));

            List<Ticket> tickets = book.getTickets();

            // tìm ticket gần nhất
            Ticket latestTicket = tickets.stream()
                    .max(Comparator.comparing(Ticket::getBorrowDate))
                    .orElse(null);

            if (latestTicket != null && latestTicket.getTicketStatus() == TicketStatus.ACTIVE) {
                return new TicketResponse(
                        "Book is already borrowed",
                        "FAILED"
                );
            }

            Ticket newTicket = new Ticket();
            newTicket.setBook(book);
            newTicket.setBorrower(borrower);
            newTicket.setBorrowDate(Instant.now());
            newTicket.setReturnDate(borrowBookRequest.getReturnDate());
            newTicket.setTicketStatus(TicketStatus.ACTIVE);

            ticketRepository.save(newTicket);

            log.info("Book {} borrowed by {} (ID: {})",
                    book.getTitle(),
                    borrower.getFullName(),
                    borrower.getId());

            return new TicketResponse(
                    "Borrow book successfully",
                    "SUCCESS"
            );
        } catch (Exception e) {
            log.error("Import book failed", e);
            return new TicketResponse(
                    "FAILED",
                    e.getMessage()
            );
        }
    }

}


