package com.booktracker.dao.impl;

import com.booktracker.dao.DatabaseManager;
import com.booktracker.dao.ReadingSessionDao;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.ReadingSession;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReadingSessionDaoImpl implements ReadingSessionDao {
    private final DatabaseManager dbManager;

    public ReadingSessionDaoImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public ReadingSession create(ReadingSession session) {
        String sql = "INSERT INTO reading_sessions (book_id, user_id, start_time, end_time, pages_read) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, session.getBookId());
            stmt.setLong(2, session.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(session.getEndTime()));
            stmt.setInt(5, session.getPagesRead());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Creating reading session failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setId(generatedKeys.getLong(1));
                } else {
                    throw new BookTrackerException("Creating reading session failed, no ID obtained.");
                }
            }
            
            return session;
        } catch (SQLException e) {
            throw new BookTrackerException("Error creating reading session", e);
        }
    }

    @Override
    public List<ReadingSession> findByBookAndUser(Long bookId, Long userId) {
        String sql = "SELECT * FROM reading_sessions WHERE book_id = ? AND user_id = ?";
        List<ReadingSession> sessions = new ArrayList<>();
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bookId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToReadingSession(rs));
                }
            }
            return sessions;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding reading sessions", e);
        }
    }

    @Override
    public List<ReadingSession> findByUserInTimeRange(Long userId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM reading_sessions WHERE user_id = ? AND start_time >= ? AND end_time <= ?";
        List<ReadingSession> sessions = new ArrayList<>();
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3, Timestamp.valueOf(end));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToReadingSession(rs));
                }
            }
            return sessions;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding reading sessions in time range", e);
        }
    }

    @Override
    public void update(ReadingSession session) {
        String sql = "UPDATE reading_sessions SET start_time = ?, end_time = ?, pages_read = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(session.getStartTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(session.getEndTime()));
            stmt.setInt(3, session.getPagesRead());
            stmt.setLong(4, session.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Updating reading session failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error updating reading session", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reading_sessions WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Deleting reading session failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error deleting reading session", e);
        }
    }

    private ReadingSession mapResultSetToReadingSession(ResultSet rs) throws SQLException {
        ReadingSession session = new ReadingSession();
        session.setId(rs.getLong("id"));
        session.setBookId(rs.getLong("book_id"));
        session.setUserId(rs.getLong("user_id"));
        session.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        session.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        session.setPagesRead(rs.getInt("pages_read"));
        return session;
    }
}