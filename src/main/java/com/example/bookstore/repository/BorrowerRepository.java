package com.example.bookstore.repository;

import com.example.bookstore.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByEmail (String email);
    boolean existsByPhone (String phone);
}