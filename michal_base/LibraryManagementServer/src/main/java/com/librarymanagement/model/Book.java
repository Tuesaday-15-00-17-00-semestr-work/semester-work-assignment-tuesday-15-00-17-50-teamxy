package com.librarymanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = true)
    private Integer availableCopies;


    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void decrementAvailableCopies() {
        if (this.availableCopies != null && this.availableCopies > 0) {
            this.availableCopies--;
        } else {
            throw new IllegalStateException("No available copies left to borrow!");
        }
    }

    public void incrementAvailableCopies() {
        if (this.availableCopies != null) {
            this.availableCopies++;
        } else {
            throw new IllegalStateException("Available copies cannot be null!");
        }
    }
}
