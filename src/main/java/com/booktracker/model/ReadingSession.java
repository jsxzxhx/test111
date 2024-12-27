package com.booktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class ReadingSession {
    private Long bookId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int pagesRead;

    public ReadingSession(Long bookId, Long userId, LocalDateTime startTime, LocalDateTime endTime, int pagesRead) {
        this.bookId = bookId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pagesRead = pagesRead;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }
}