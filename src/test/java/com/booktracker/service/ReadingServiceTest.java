package com.booktracker.service;

import com.booktracker.model.Book;
import com.booktracker.model.ReadingProgress;
import com.booktracker.model.ReadingSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingServiceTest {
    private ReadingService readingService;
    private BookService bookService;
    private UserService userService;
    private static final Long DEFAULT_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        readingService = new ReadingService();
        bookService = new BookService();
        userService = new UserService();
        
        // Ensure default user exists and set it for both services
        userService.getCurrentUser(DEFAULT_USER_ID);
        readingService.setCurrentUser(DEFAULT_USER_ID);
        bookService.setCurrentUser(DEFAULT_USER_ID);
    }

    @Test
    void testGetTotalReadingHours_WithDefaultUser() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        // When
        double totalHours = readingService.getTotalReadingHours(start, end);

        // Then
        assertTrue(totalHours >= 0.0, "Total reading hours should be non-negative");
    }

    @Test
    void testRecordReadingSession_WithDefaultUser() {
        // Given
        Book book = new Book("Test Book", "Test Author", 100, DEFAULT_USER_ID);
        book = bookService.createBook(book);
        assertNotNull(book.getId(), "Book ID should not be null");
        
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        // When
        readingService.recordReadingSession(book.getId(), startTime, endTime, 1, 10);

        // Then
        ReadingProgress progress = readingService.getReadingProgress(book.getId());
        assertNotNull(progress);
        assertEquals(10, progress.getCurrentPage());
        assertEquals(10.0, progress.getProgressPercentage());
    }
}