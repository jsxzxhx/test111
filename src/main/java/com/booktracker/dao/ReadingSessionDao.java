package com.booktracker.dao;

import com.booktracker.exception.BookTrackerException;
import com.booktracker.model.ReadingSession;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 阅读会话数据访问接口
 * 定义了所有与阅读会话相关的数据库操作
 */
public interface ReadingSessionDao {
    /**
     * 设置当前用户ID
     * @param userId 用户ID
     */
    void setCurrentUserId(Long userId);

    /**
     * 创建新的阅读会话记录
     * @param session 阅读会话对象
     * @throws BookTrackerException 如果创建失败
     */
    void create(ReadingSession session) throws BookTrackerException;

    /**
     * 查找指定用户的指定图书的所有阅读会话
     * @param bookId 图书ID
     * @param userId 用户ID
     * @return 阅读会话列表
     * @throws BookTrackerException 如果查询失败
     */
    List<ReadingSession> findByBookAndUser(Long bookId, Long userId) throws BookTrackerException;

    /**
     * 查找指定用户在指定时间范围内的所有阅读会话
     * @param userId 用户ID
     * @param start 开始时间
     * @param end 结束时间
     * @return 阅读会话列表
     * @throws BookTrackerException 如果查询失败
     */
    List<ReadingSession> findByUserInTimeRange(Long userId, LocalDateTime start, LocalDateTime end) 
            throws BookTrackerException;
}