package com.librarymanagement.controller;

import com.librarymanagement.model.Transaction;
import com.librarymanagement.model.User;
import com.librarymanagement.model.Book;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    @PostMapping("/borrow")
    public ResponseEntity borrowBook(
            @RequestParam Long userId,
            @RequestParam Long bookId
    ) {
        try {
            // Step 1: Validate the user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

            // Step 2: Validate the book and check availability
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found"));

            if (book.getAvailableCopies() <= 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Book with ID " + bookId + " is currently unavailable");
            }

            // Step 3: Decrement the available copies of the book
            book.decrementAvailableCopies();
            bookRepository.save(book);

            // Step 4: Create and save the transaction
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setBook(book);
            transaction.setAction("BORROW");
            transaction.setDate(LocalDateTime.now());
            transactionRepository.save(transaction);

            // Step 5: Return success response
            return ResponseEntity.ok("Book borrowed successfully! Transaction ID: " + transaction.getTransactionId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle and return error responses for invalid input or logic failures
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity returnBook(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            // Step 1: Validate the user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

            // Step 2: Validate the book
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found"));

            // Step 3: Check if the user has borrowed the book
            Transaction transaction = transactionRepository.findByUserAndBookAndAction(user, book, "BORROW")
                    .orElseThrow(() -> new IllegalStateException("No record of borrowing found for this book and user"));

            // Step 4: Increment back the available copies of the book
            book.incrementAvailableCopies();
            bookRepository.save(book);

            // Step 5: Create and save the return transaction
            Transaction returnTransaction = new Transaction();
            returnTransaction.setUser(user);
            returnTransaction.setBook(book);
            returnTransaction.setAction("RETURN");
            returnTransaction.setDate(LocalDateTime.now());
            transactionRepository.save(returnTransaction);

            // Step 6: Return success response
            return ResponseEntity.ok("Book returned successfully! Transaction ID: " + returnTransaction.getTransactionId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle and return error responses for invalid input or logic failures
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getUser().getUserId().equals(userId))
                .toList();
    }
}