package com.blog.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文章实体类
 */
public class Post implements Serializable {
    private int id;
    private int userId;
    private String title;
    private String content;
    private Timestamp createTime;
    private String username; // 作者用户名（用于显示）

    public Post() {
    }

    public Post(int id, int userId, String title, String content, Timestamp createTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

