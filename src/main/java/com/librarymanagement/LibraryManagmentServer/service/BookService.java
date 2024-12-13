package com.librarymanagement.LibraryManagmentServer.service;

import com.librarymanagement.LibraryManagmentServer.model.Book;
import com.librarymanagement.LibraryManagmentServer.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(int bookId, Book book) {
        return bookRepository.findById(bookId).map(existingBook -> {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setAvailableCopies(book.getAvailableCopies());
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));
    }

    public void deleteBook(int bookId) {
        bookRepository.deleteById(bookId);
    }
}

