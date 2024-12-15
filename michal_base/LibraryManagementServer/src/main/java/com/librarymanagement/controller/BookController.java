package com.librarymanagement.controller;

import com.librarymanagement.model.Book;
import com.librarymanagement.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Search books based on optional filters (title, author, or ISBN).
     * @param title Book title (optional).
     * @param author Book author (optional).
     * @param isbn Book ISBN (optional).
     * @return List of books matching the filters.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn) {
        try {
            // Custom logic for filtering books based on the query
            List<Book> books;
            if (title != null && author == null && isbn == null) {
                books = bookRepository.findByTitleContainingIgnoreCase(title);
            } else if (title == null && author != null && isbn == null) {
                books = bookRepository.findByAuthorContainingIgnoreCase(author);
            } else if (title == null && author == null && isbn != null) {
                books = bookRepository.findByIsbn(isbn);
            } else {
                books = bookRepository.findAll(); // Return all books if no filters are provided
            }

            return ResponseEntity.ok(books);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());
        }
    }
}
