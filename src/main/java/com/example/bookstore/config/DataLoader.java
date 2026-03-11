package com.example.bookstore.config;

import com.example.bookstore.entity.Borrower;
import com.example.bookstore.entity.Category;
import com.example.bookstore.repository.BorrowerRepository;
import com.example.bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BorrowerRepository borrowerRepository;

    @Override
    public void run(String... args) {

        // Seed Category
        if (categoryRepository.count() == 0) {

            Category c1 = new Category();
            c1.setName("Programming");
            c1.setPrefix_code("PRG");

            Category c2 = new Category();
            c2.setName("Database");
            c2.setPrefix_code("DB");

            Category c3 = new Category();
            c3.setName("AI");
            c3.setPrefix_code("AI");

            Category c4 = new Category();
            c4.setName("Networking");
            c4.setPrefix_code("NET");

            Category c5 = new Category();
            c5.setName("Security");
            c5.setPrefix_code("SEC");

            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c3);
            categoryRepository.save(c4);
            categoryRepository.save(c5);
        }

        // Seed Borrower
        if (borrowerRepository.count() == 0) {

            for (int i = 1; i <= 20; i++) {

                Borrower borrower = new Borrower();
                borrower.setFullName("Borrower " + i);
                borrower.setEmail("borrower" + i + "@mail.com");
                borrower.setPhone("09000000" + i);

                borrowerRepository.save(borrower);
            }
        }
    }
}