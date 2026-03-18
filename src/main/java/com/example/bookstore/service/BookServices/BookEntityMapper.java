
package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import org.springframework.stereotype.Service;

@Service
//Thao tác với entity
public class BookEntityMapper {
    public Book importFromRequest(ManageBookRequest req, Category category) {
        Book b = new Book();
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setPrice(req.getPrice());
        b.setCategory(category);
        return b;
    }

    public void updateFromRequest(Book book, ManageBookRequest req, Category category) {
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setPrice(req.getPrice());
        book.setCategory(category);
    }
}
