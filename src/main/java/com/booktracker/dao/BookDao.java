package com.booktracker.dao;

import com.booktracker.model.Book;
import java.util.List;

public interface BookDao {
    Book create(Book book);
    Book findById(Long id);
    List<Book> findByUserId(Long userId);
    void update(Book book);
    void delete(Long id);
}