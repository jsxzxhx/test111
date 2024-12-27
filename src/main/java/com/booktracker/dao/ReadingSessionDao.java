package com.booktracker.dao;

import com.booktracker.model.ReadingSession;
import java.time.LocalDateTime;
import java.util.List;

public interface ReadingSessionDao {
    ReadingSession create(ReadingSession session);
    List<ReadingSession> findByBookAndUser(Long bookId, Long userId);
    List<ReadingSession> findByUserInTimeRange(Long userId, LocalDateTime start, LocalDateTime end);
    void update(ReadingSession session);
    void delete(Long id);
}