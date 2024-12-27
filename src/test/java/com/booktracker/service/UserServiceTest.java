package com.booktracker.service;

import com.booktracker.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserService userService = new UserService();

    @Test
    public void testGetCurrentUser_WhenUserDoesNotExist_ShouldCreateDefaultUser() {
        // When
        User user = userService.getCurrentUser(1L);

        // Then
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("default", user.getUsername());
        assertNotNull(user.getDatabasePath());
    }

    @Test
    public void testGetUsernameById_ShouldReturnUsername() {
        // When
        String username = userService.getUsernameById(1L);

        // Then
        assertEquals("default", username);
    }
}