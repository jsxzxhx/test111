package com.booktracker.dao;

import com.booktracker.model.User;
import java.util.List;

public interface UserDao {
    User create(User user);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
}