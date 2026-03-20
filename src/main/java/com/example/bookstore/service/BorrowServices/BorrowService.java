package com.example.bookstore.service.BorrowServices;

import com.example.bookstore.dto.BorrowBookRequest;
import com.example.bookstore.dto.TicketRequest;
import com.example.bookstore.dto.TicketResponse;
import com.example.bookstore.dto.TicketResult;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Borrower;
import com.example.bookstore.entity.Ticket;
import com.example.bookstore.enums.TicketErrorCode;
import com.example.bookstore.enums.TicketStatus;
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
public class BorrowService implements IBorrowService {
    private final BookRepository bookRepository;
    private final TicketRepository ticketRepository;
    private final BorrowerRepository borrowerRepository;


    @Transactional
    @Override
    public TicketResult borrowBook(BorrowBookRequest borrowBookRequest) {
        try {
            // 1. Tìm book
            Book book = bookRepository.findById(borrowBookRequest.getBook_id())
                    .orElse(null);
            if (book == null) {
                return new TicketResult(
                        false,
                        TicketErrorCode.BOOK_NOT_FOUND,
                        "Book not found with id: " + borrowBookRequest.getBook_id(),
                        null
                );
            }

            // 2. Tìm borrower
            Borrower borrower = borrowerRepository.findById(borrowBookRequest.getBorrower_id())
                    .orElse(null);
            if (borrower == null) {
                return new TicketResult(
                        false,
                        TicketErrorCode.BORROWER_NOT_FOUND,
                        "Borrower not found with id: " + borrowBookRequest.getBorrower_id(),
                        null
                );
            }

            // 3. Kiểm tra book đã được mượn chưa
            List<Ticket> tickets = book.getTickets();
            Ticket latestTicket = tickets.stream()
                    .max(Comparator.comparing(Ticket::getBorrowDate))
                    .orElse(null);

            if (latestTicket != null && latestTicket.getTicketStatus() == TicketStatus.ACTIVE) {
                return new TicketResult(
                        false,
                        TicketErrorCode.BOOK_ALREADY_BORROWED,
                        "Book is already borrowed",
                        null
                );
            }

            // 4. Tạo ticket mới
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

            TicketResponse payload = new TicketResponse(
                    "Borrow book successfully",
                    "SUCCESS"
            );

            return new TicketResult(
                    true,
                    null,
                    null,
                    payload
            );
        } catch (Exception e) {
            log.error("Borrow book failed", e);
            // Lỗi hệ thống không lường trước → cho văng lên GlobalExceptionHandler
            throw new RuntimeException("Failed to borrow book: " + e.getMessage(), e);
        }
    }


    @Override
    public TicketResult cancelTicket(TicketRequest ticketRequest) {
        try {
            Ticket ticket = ticketRepository.findById(ticketRequest.getTicketId())
                    .orElse(null);
            if (ticket == null) {
                return new TicketResult(
                        false,
                        TicketErrorCode.TICKET_NOT_FOUND,
                        "Ticket not found with id: " + ticketRequest.getTicketId(),
                        null
                );
            }

            if (ticket.getTicketStatus() == TicketStatus.RETURNED
                    || ticket.getTicketStatus() == TicketStatus.CANCELED) {
                return new TicketResult(
                        false,
                        TicketErrorCode.INVALID_TICKET_STATUS,
                        "Invalid ticket status",
                        null
                );
            }

            ticket.setTicketStatus(ticketRequest.getTicketStatus());
            Ticket updatedTicket = ticketRepository.save(ticket);

            log.info("Ticket with id {} has updated successfully",
                    updatedTicket.getId());

            TicketResponse payload = new TicketResponse(
                    "Update ticket status successfully",
                    "SUCCESS"
            );

            return new TicketResult(
                    true,
                    null,
                    null,
                    payload
            );
        } catch (Exception e) {
            log.error("Cancel/update ticket failed", e);
            throw new RuntimeException("Failed to cancel/update ticket: " + e.getMessage(), e);
        }
    }

}


