package com.example.bookstore.controller;

import com.example.bookstore.dto.*;
import com.example.bookstore.service.BorrowServices.IBorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class BorrowController {
    private final IBorrowService borrowService;

    @PostMapping("/borrow")
    public ResponseEntity<APIResponse<TicketResponse>> borrowResponse(
            @RequestBody BorrowBookRequest borrowBookRequest
    ) {
        TicketResult result = borrowService.borrowBook(borrowBookRequest);

        APIResponse<TicketResponse> body = new APIResponse<>();

        if (result.isSuccess()) {
            body.setStatus("SUCCESS");
            body.setCode("BOOK_BORROWED");
            body.setMessage("Borrow book successfully");
            body.setData(result.getData());
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } else {
            body.setStatus("ERROR");

            HttpStatus status;
            String code;

            switch (result.getErrorCode()) {
                case BOOK_NOT_FOUND -> {
                    status = HttpStatus.NOT_FOUND;
                    code = "BOOK_NOT_FOUND";
                }
                case BORROWER_NOT_FOUND -> {
                    status = HttpStatus.NOT_FOUND;
                    code = "BORROWER_NOT_FOUND";
                }
                case BOOK_ALREADY_BORROWED -> {
                    status = HttpStatus.CONFLICT;
                    code = "BOOK_ALREADY_BORROWED";
                }
                default -> {
                    status = HttpStatus.BAD_REQUEST;
                    code = "BORROW_FAILED";
                }
            }
            body.setCode(code);
            body.setMessage(result.getErrorMessage());
            body.setData(null);

            return ResponseEntity.status(status).body(body);
        }
    }

    @PostMapping("/cancelBorrow")
    public ResponseEntity<APIResponse<TicketResponse>> cancelBorrowResponse(
            @RequestBody TicketRequest ticketRequest
    ) {
        TicketResult result = borrowService.cancelTicket(ticketRequest);

        APIResponse<TicketResponse> body = new APIResponse<>();

        if (result.isSuccess()) {
            body.setStatus("SUCCESS");
            body.setCode("TICKET_UPDATED");
            body.setMessage("Update ticket status successfully");
            body.setData(result.getData());
            return ResponseEntity.ok(body);
        } else {
            body.setStatus("ERROR");

            HttpStatus status;
            String code;

            switch (result.getErrorCode()) {
                case TICKET_NOT_FOUND -> {
                    status = HttpStatus.NOT_FOUND;
                    code = "TICKET_NOT_FOUND";
                }
                case INVALID_TICKET_STATUS -> {
                    status = HttpStatus.BAD_REQUEST;
                    code = "INVALID_TICKET_STATUS";
                }
                default -> {
                    status = HttpStatus.BAD_REQUEST;
                    code = "TICKET_UPDATE_FAILED";
                }
            }
            body.setCode(code);
            body.setMessage(result.getErrorMessage());
            body.setData(null);
            return ResponseEntity.status(status).body(body);
        }
    }

}
