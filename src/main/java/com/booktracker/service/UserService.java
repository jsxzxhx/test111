package com.booktracker.service;

import com.booktracker.dao.UserDao;
import com.booktracker.dao.impl.UserDaoImpl;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.User;

/**
 * 用户服务类，处理所有与用户相关的业务逻辑
 */
public class UserService {
    private final UserDao userDao;
    private static final String DEFAULT_USERNAME = "default";

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public User getCurrentUser(Long userId) {
        try {
            User user = userDao.findById(userId);
            if (user == null) {
                // If no user exists with this ID, check if default user exists
                user = userDao.findByUsername(DEFAULT_USERNAME);
                if (user == null) {
                    // Create default user if it doesn't exist
                    user = new User(DEFAULT_USERNAME);
                    user.setId(1L);
                    userDao.create(user);
                }
            }
            return user;
        } catch (Exception e) {
            throw new BookTrackerException("获取当前用户失败", e);
        }
    }

    public String getUsernameById(Long userId) {
        User user = getCurrentUser(userId);
        return user.getUsername();
    }
}