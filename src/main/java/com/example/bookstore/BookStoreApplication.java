package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
        int a = 0;
        a++;
        int b = 1;
        for (int i = 0; i < 10; i++) {
            a++;
            System.out.println("a = " + a);
        }
    }
}



