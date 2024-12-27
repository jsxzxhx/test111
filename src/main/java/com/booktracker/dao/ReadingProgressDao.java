package com.booktracker.dao;

import com.booktracker.model.ReadingProgress;
import java.util.List;

public interface ReadingProgressDao {
    ReadingProgress create(ReadingProgress progress);
    ReadingProgress findByBookAndUser(Long bookId, Long userId);
    List<ReadingProgress> findByUserId(Long userId);
    void update(ReadingProgress progress);
    void delete(Long id);
}