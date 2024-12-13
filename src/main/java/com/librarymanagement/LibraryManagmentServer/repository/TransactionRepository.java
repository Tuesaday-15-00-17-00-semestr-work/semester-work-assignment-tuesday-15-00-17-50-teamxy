package com.librarymanagement.LibraryManagmentServer.repository;

import com.librarymanagement.LibraryManagmentServer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}

