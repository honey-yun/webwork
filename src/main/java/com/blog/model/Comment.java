package com.blog.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评论实体类
 */
public class Comment implements Serializable {
    private int id;
    private int postId;
    private int userId;
    private String content;
    private Timestamp createTime;
    private String username; // 评论者用户名（用于显示）

    public Comment() {
    }

    public Comment(int id, int postId, int userId, String content, Timestamp createTime) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

