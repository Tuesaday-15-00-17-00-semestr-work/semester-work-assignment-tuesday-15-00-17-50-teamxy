package com.librarymanagement.repository;

import com.librarymanagement.model.Transaction;
import com.librarymanagement.model.Book;
import com.librarymanagement.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUserAndBookAndAction(User user, Book book, String action);
}
