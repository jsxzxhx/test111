-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 图书表
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    total_pages INT,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 阅读进度表
CREATE TABLE IF NOT EXISTS reading_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT,
    user_id BIGINT,
    current_page INT,
    percentage DECIMAL(5,2),
    last_updated TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 阅读会话表
CREATE TABLE IF NOT EXISTS reading_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT,
    user_id BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    pages_read INT,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 插入默认用户
INSERT INTO users (username) VALUES ('default');
