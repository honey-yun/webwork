-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog2_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE blog2_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'guest') NOT NULL DEFAULT 'guest'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建文章表
CREATE TABLE IF NOT EXISTS posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建评论表
CREATE TABLE IF NOT EXISTS comments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据
-- 1个管理员
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin', 'admin');

-- 2个普通用户
INSERT INTO users (username, password, role) VALUES 
('user1', '123456', 'guest'),
('user2', '123456', 'guest');

-- 3篇文章
INSERT INTO posts (user_id, title, content, create_time) VALUES 
(1, '欢迎来到个人博客系统', '这是第一篇博客文章，欢迎使用本系统！系统支持管理员发布文章，普通用户评论等功能。', NOW()),
(1, 'Java Web开发入门', 'Java Web开发是学习Java后端开发的重要环节。本篇文章将介绍Servlet、JSP等基础知识。', DATE_ADD(NOW(), INTERVAL -1 DAY)),
(1, 'MySQL数据库使用指南', 'MySQL是最流行的关系型数据库之一。本文将介绍MySQL的基本使用方法和常见操作。', DATE_ADD(NOW(), INTERVAL -2 DAY));

-- 5条评论
INSERT INTO comments (post_id, user_id, content, create_time) VALUES 
(1, 2, '很好的文章，学到了很多！', DATE_ADD(NOW(), INTERVAL -1 HOUR)),
(1, 3, '期待更多精彩内容！', DATE_ADD(NOW(), INTERVAL -30 MINUTE)),
(2, 2, 'Java Web开发确实很重要，感谢分享！', DATE_ADD(NOW(), INTERVAL -2 HOUR)),
(2, 3, '希望能有更多实战案例。', DATE_ADD(NOW(), INTERVAL -1 HOUR)),
(3, 2, 'MySQL的使用确实需要多练习。', DATE_ADD(NOW(), INTERVAL -3 HOUR));

