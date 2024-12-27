package com.booktracker.dao.impl;

import com.booktracker.dao.DatabaseManager;
import com.booktracker.dao.ReadingProgressDao;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.ReadingProgress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingProgressDaoImpl implements ReadingProgressDao {
    private final DatabaseManager dbManager;

    public ReadingProgressDaoImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public ReadingProgress create(ReadingProgress progress) {
        String sql = "INSERT INTO reading_progress (book_id, user_id, current_page, progress_percentage, last_read_date) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, progress.getBookId());
            stmt.setLong(2, progress.getUserId());
            stmt.setInt(3, progress.getCurrentPage());
            stmt.setDouble(4, progress.getProgressPercentage());
            stmt.setTimestamp(5, Timestamp.valueOf(progress.getLastReadDate()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Creating reading progress failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    progress.setId(generatedKeys.getLong(1));
                } else {
                    throw new BookTrackerException("Creating reading progress failed, no ID obtained.");
                }
            }
            
            return progress;
        } catch (SQLException e) {
            throw new BookTrackerException("Error creating reading progress", e);
        }
    }

    @Override
    public ReadingProgress findByBookAndUser(Long bookId, Long userId) {
        String sql = "SELECT * FROM reading_progress WHERE book_id = ? AND user_id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bookId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReadingProgress(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding reading progress", e);
        }
    }

    @Override
    public List<ReadingProgress> findByUserId(Long userId) {
        String sql = "SELECT * FROM reading_progress WHERE user_id = ?";
        List<ReadingProgress> progressList = new ArrayList<>();
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    progressList.add(mapResultSetToReadingProgress(rs));
                }
            }
            return progressList;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding reading progress by user ID", e);
        }
    }

    @Override
    public void update(ReadingProgress progress) {
        String sql = "UPDATE reading_progress SET current_page = ?, progress_percentage = ?, last_read_date = ? " +
                    "WHERE book_id = ? AND user_id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, progress.getCurrentPage());
            stmt.setDouble(2, progress.getProgressPercentage());
            stmt.setTimestamp(3, Timestamp.valueOf(progress.getLastReadDate()));
            stmt.setLong(4, progress.getBookId());
            stmt.setLong(5, progress.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Updating reading progress failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error updating reading progress", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reading_progress WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Deleting reading progress failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error deleting reading progress", e);
        }
    }

    private ReadingProgress mapResultSetToReadingProgress(ResultSet rs) throws SQLException {
        ReadingProgress progress = new ReadingProgress();
        progress.setId(rs.getLong("id"));
        progress.setBookId(rs.getLong("book_id"));
        progress.setUserId(rs.getLong("user_id"));
        progress.setCurrentPage(rs.getInt("current_page"));
        progress.setProgressPercentage(rs.getDouble("progress_percentage"));
        progress.setLastReadDate(rs.getTimestamp("last_read_date").toLocalDateTime());
        return progress;
    }
}