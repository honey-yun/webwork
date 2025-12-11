package com.blog.dao;

import com.blog.model.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论DAO类
 */
public class CommentDAO {
    /**
     * 根据文章ID查询所有评论
     */
    public List<Comment> findByPostId(int postId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Comment> comments = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.id, c.post_id, c.user_id, c.content, c.create_time, u.username " +
                    "FROM comments c LEFT JOIN users u ON c.user_id = u.id " +
                    "WHERE c.post_id = ? ORDER BY c.create_time ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setPostId(rs.getInt("post_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreateTime(rs.getTimestamp("create_time"));
                comment.setUsername(rs.getString("username"));
                comments.add(comment);
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
        return comments;
    }

    /**
     * 添加评论
     */
    public boolean add(Comment comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO comments (post_id, user_id, content, create_time) VALUES (?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, comment.getPostId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
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
     * 删除评论
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM comments WHERE id = ?";
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

