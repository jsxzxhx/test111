package com.booktracker.exception;

public class BookTrackerException extends RuntimeException {
    public BookTrackerException(String message) {
        super(message);
    }

    public BookTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}