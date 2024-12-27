package com.booktracker.dao.impl;

import com.booktracker.dao.UserDao;
import com.booktracker.dao.DatabaseManager;
import com.booktracker.model.User;
import com.booktracker.exception.BookTrackerException;

import java.sql.*;

/**
 * UserDao接口的实现类
 * 负责处理用户相关的数据库操作
 */
public class UserDaoImpl implements UserDao {
    private String username;
    private final DatabaseManager dbManager;

    public UserDaoImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void create(User user) throws BookTrackerException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection(username);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new BookTrackerException("创建用户失败: " + user.getUsername(), e);
        }
    }

    @Override
    public User findById(Long id) throws BookTrackerException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbManager.getConnection(username);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("查询用户失败: ID " + id, e);
        }
    }

    @Override
    public User findByUsername(String username) throws BookTrackerException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dbManager.getConnection(this.username);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BookTrackerException("查询用户失败: " + username, e);
        }
    }

    @Override
    public void update(User user) throws BookTrackerException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection(username);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setLong(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookTrackerException("更新用户失败: " + user.getUsername(), e);
        }
    }

    @Override
    public void delete(Long id) throws BookTrackerException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbManager.getConnection(username);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookTrackerException("删除用户失败: ID " + id, e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
