package com.booktracker.dao;

import com.booktracker.exception.BookTrackerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    @Test
    void getConnection_WithValidUserId_ShouldReturnConnection() throws SQLException {
        // Arrange
        DatabaseManager manager = DatabaseManager.getInstance();
        Long userId = 1L;

        // Act
        Connection connection = manager.getConnection(userId);

        // Assert
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    void getConnection_WithNullUserId_ShouldUseDefaultId() throws SQLException {
        // Arrange
        DatabaseManager manager = DatabaseManager.getInstance();

        // Act
        Connection connection = manager.getConnection(null);

        // Assert
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    void closeDataSource_ShouldRemoveDataSource() throws SQLException {
        // Arrange
        DatabaseManager manager = DatabaseManager.getInstance();
        Long userId = 2L;
        manager.getConnection(userId); // Create data source

        // Act
        manager.closeDataSource(userId);

        // Assert
        // Try to get a new connection - should create a new data source
        Connection newConnection = manager.getConnection(userId);
        assertNotNull(newConnection);
        assertFalse(newConnection.isClosed());
        newConnection.close();
    }
}
