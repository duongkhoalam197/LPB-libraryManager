
package com.example.bookstore.service.BookServices;

import com.example.bookstore.dto.ManageBookRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import org.springframework.stereotype.Service;

@Service
//Thao tác với entity
public class BookEntityMapper implements IBookEntityMapper {
    @Override
    public Book importFromRequest(ManageBookRequest req, Category category) {
        Book b = new Book();
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setPrice(req.getPrice());
        b.setCategory(category);
        return b;
    }

    @Override
    public void updateFromRequest(Book book, ManageBookRequest request, Category category) {
        // Cần kiểu logic như sau:

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }

        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }

        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }

        if (request.getCategoryId() != null) {
            // category đã được lấy từ DB tương ứng id mới
            book.setCategory(category);
        }
    }
}
