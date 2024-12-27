package com.booktracker.dao.impl;

import com.booktracker.dao.DatabaseManager;
import com.booktracker.dao.UserDao;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final DatabaseManager dbManager;

    public UserDaoImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (id, username, password, email, database_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection(user.getUsername());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getDatabasePath());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Creating user failed, no rows affected.");
            }
            
            return user;
        } catch (SQLException e) {
            throw new BookTrackerException("Error creating user", e);
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding user by ID", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding user by username", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new BookTrackerException("Error finding all users", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, database_path = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection(user.getUsername());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getDatabasePath());
            stmt.setLong(5, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error updating user", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbManager.getConnection("default");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new BookTrackerException("Deleting user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new BookTrackerException("Error deleting user", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setDatabasePath(rs.getString("database_path"));
        return user;
    }
}