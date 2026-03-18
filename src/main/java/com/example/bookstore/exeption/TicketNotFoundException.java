package com.example.bookstore.exeption;

public class TicketNotFoundException extends BusinessException {
  public TicketNotFoundException(String message) {
    super(message, 404);
  }
}