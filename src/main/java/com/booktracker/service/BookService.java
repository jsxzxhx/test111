package com.booktracker.service;

import com.booktracker.dao.BookDao;
import com.booktracker.dao.impl.BookDaoImpl;
import com.booktracker.model.Book;
import com.booktracker.exception.BookTrackerException;
import java.util.List;

public class BookService {
    private final BookDao bookDao;

    public BookService() {
        this.bookDao = new BookDaoImpl();
    }

    public void addBook(String title, String author, int totalPages) {
        try {
            Book book = new Book(title, author, totalPages);
            bookDao.create(book);
        } catch (BookTrackerException e) {
            throw new BookTrackerException("添加图书失败: " + title, e);
        }
    }

    public void updateBook(Long id, String title, String author, int totalPages) {
        try {
            Book book = bookDao.findById(id);
            if (book != null) {
                book.setTitle(title);
                book.setAuthor(author);
                book.setTotalPages(totalPages);
                bookDao.update(book);
            } else {
                throw new BookTrackerException("找不到ID为" + id + "的图书");
            }
        } catch (BookTrackerException e) {
            throw new BookTrackerException("更新图书失败: " + title, e);
        }
    }

    public void deleteBook(Long id) {
        try {
            bookDao.delete(id);
        } catch (BookTrackerException e) {
            throw new BookTrackerException("删除图书失败: ID " + id, e);
        }
    }

    public Book getBook(Long id) {
        try {
            return bookDao.findById(id);
        } catch (BookTrackerException e) {
            throw new BookTrackerException("获取图书信息失败: ID " + id, e);
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookDao.findAll();
        } catch (BookTrackerException e) {
            throw new BookTrackerException("获取所有图书列表失败", e);
        }
    }
}
