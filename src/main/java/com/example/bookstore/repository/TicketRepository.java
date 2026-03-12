package com.example.bookstore.repository;

import com.example.bookstore.entity.Ticket;
import com.example.bookstore.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository <Ticket, Long> {
    boolean existsByBookIdAndTicketStatus(Long bookId, TicketStatus ticketStatus);
}
