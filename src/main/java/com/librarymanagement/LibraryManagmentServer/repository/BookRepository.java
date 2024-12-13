package com.librarymanagement.LibraryManagmentServer.repository;

import com.librarymanagement.LibraryManagmentServer.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}

