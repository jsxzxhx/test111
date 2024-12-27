package com.booktracker.dao;

import com.booktracker.model.User;
import com.booktracker.exception.BookTrackerException;
import java.util.List;

/**
 * 用户数据访问接口
 * 定义了用户相关的数据库操作方法
 */
public interface UserDao {
    /**
     * 设置当前操作的用户名
     * @param username 用户名
     */
    void setUsername(String username);

    /**
     * 创建新用户
     * @param user 要创建的用户对象
     * @throws BookTrackerException 如果创建过程中发生错误
     */
    void create(User user) throws BookTrackerException;

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 找到的用户对象，如果不存在返回null
     * @throws BookTrackerException 如果查询过程中发生错误
     */
    User findById(Long id) throws BookTrackerException;

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 找到的用户对象，如果不存在返回null
     * @throws BookTrackerException 如果查询过程中发生错误
     */
    User findByUsername(String username) throws BookTrackerException;

    /**
     * 更新用户信息
     * @param user 要更新的用户对象
     * @throws BookTrackerException 如果更新过程中发生错误
     */
    void update(User user) throws BookTrackerException;

    /**
     * 删除用户
     * @param id 要删除的用户ID
     * @throws BookTrackerException 如果删除过程中发生错误
     */
    void delete(Long id) throws BookTrackerException;
}
