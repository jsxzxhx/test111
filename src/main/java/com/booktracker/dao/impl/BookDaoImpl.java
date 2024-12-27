package com.booktracker.dao.impl;

import com.booktracker.dao.BookDao;
import com.booktracker.dao.DatabaseManager;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private final DatabaseManager dbManager;

    public BookDaoImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO books (title, author, total_pages, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getTotalPages());
            stmt.setLong(4, book.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                } else {
                    throw new BookTrackerException("Creating book failed, no ID obtained.");
                }
            }
            
            conn.commit();
            return book;
        } catch (SQLException e) {
            throw new BookTrackerException("Error creating book", e);
        }
    }

    @Override
    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding book by ID", e);
        }
    }

    @Override
    public List<Book> findByUserId(Long userId) {
        String sql = "SELECT * FROM books WHERE user_id = ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding books by user ID", e);
        }
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, total_pages = ?, user_id = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getTotalPages());
            stmt.setLong(4, book.getUserId());
            stmt.setLong(5, book.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Updating book failed, no rows affected.");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new BookTrackerException("Error updating book", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Deleting book failed, no rows affected.");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new BookTrackerException("Error deleting book", e);
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setTotalPages(rs.getInt("total_pages"));
        book.setUserId(rs.getLong("user_id"));
        return book;
    }
}