package com.booktracker.service;

import com.booktracker.dao.BookDao;
import com.booktracker.dao.impl.BookDaoImpl;
import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.Book;

import java.util.List;

/**
 * 图书服务类，处理所有与图书相关的业务逻辑
 */
public class BookService {
    private final BookDao bookDao;
    private Long currentUserId;

    public BookService() {
        this.bookDao = new BookDaoImpl();
    }

    public void setCurrentUser(Long userId) {
        this.currentUserId = userId;
    }

    public Book createBook(Book book) {
        try {
            return bookDao.create(book);
        } catch (Exception e) {
            throw new BookTrackerException("创建图书失败", e);
        }
    }

    public Book getBook(Long id) {
        try {
            return bookDao.findById(id);
        } catch (Exception e) {
            throw new BookTrackerException("获取图书失败", e);
        }
    }

    public List<Book> getUserBooks() {
        try {
            return bookDao.findByUserId(currentUserId);
        } catch (Exception e) {
            throw new BookTrackerException("获取用户图书列表失败", e);
        }
    }

    public void updateBook(Book book) {
        try {
            bookDao.update(book);
        } catch (Exception e) {
            throw new BookTrackerException("更新图书失败", e);
        }
    }

    public void deleteBook(Long id) {
        try {
            bookDao.delete(id);
        } catch (Exception e) {
            throw new BookTrackerException("删除图书失败", e);
        }
    }
}