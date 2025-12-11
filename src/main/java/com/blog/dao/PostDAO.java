package com.blog.dao;

import com.blog.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章DAO类
 */
public class PostDAO {
    /**
     * 查询所有文章（包含作者名）
     */
    public List<Post> findAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> posts = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT p.id, p.user_id, p.title, p.content, p.create_time, u.username " +
                    "FROM posts p LEFT JOIN users u ON p.user_id = u.id " +
                    "ORDER BY p.create_time DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCreateTime(rs.getTimestamp("create_time"));
                post.setUsername(rs.getString("username"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    /**
     * 根据ID查询文章
     */
    public Post findById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Post post = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT p.id, p.user_id, p.title, p.content, p.create_time, u.username " +
                    "FROM posts p LEFT JOIN users u ON p.user_id = u.id " +
                    "WHERE p.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new Post();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCreateTime(rs.getTimestamp("create_time"));
                post.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return post;
    }

    /**
     * 添加文章
     */
    public boolean add(Post post) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO posts (user_id, title, content, create_time) VALUES (?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getTitle());
            pstmt.setString(3, post.getContent());
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * 删除文章（同时会删除该文章的所有评论，因为外键设置了ON DELETE CASCADE）
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM posts WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}

