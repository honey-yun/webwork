package com.blog.dao;

import com.blog.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 用户DAO类
 */
public class UserDAO {
    /**
     * 根据用户名和密码查询用户
     */
    public User findByUsernameAndPassword(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                System.err.println("数据库连接失败！连接为null");
                return null;
            }
            System.out.println("数据库连接成功，开始查询用户");
            
            String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            System.out.println("执行SQL: " + sql);
            System.out.println("参数1 (username): [" + username + "]");
            System.out.println("参数2 (password): [" + password + "]");
            
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                System.out.println("✓ 查询到用户: ID=" + user.getId() + ", 用户名=" + user.getUsername() + ", 角色=" + user.getRole());
            } else {
                System.out.println("✗ 未查询到匹配的用户 - 用户名或密码错误");
                // 调试：查询所有用户看看数据库里有什么
                try {
                    Statement debugStmt = conn.createStatement();
                    ResultSet debugRs = debugStmt.executeQuery("SELECT id, username, password, role FROM users");
                    System.out.println("数据库中的所有用户：");
                    while (debugRs.next()) {
                        System.out.println("  ID=" + debugRs.getInt("id") + 
                                         ", 用户名=" + debugRs.getString("username") + 
                                         ", 密码=" + debugRs.getString("password") + 
                                         ", 角色=" + debugRs.getString("role"));
                    }
                    debugRs.close();
                    debugStmt.close();
                } catch (Exception e) {
                    System.err.println("调试查询失败: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ 数据库查询异常: " + e.getMessage());
            System.err.println("SQL状态: " + e.getSQLState());
            System.err.println("错误代码: " + e.getErrorCode());
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
        return user;
    }

    /**
     * 根据ID查询用户
     */
    public User findById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
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
        return user;
    }

    /**
     * 根据用户名查询用户（用于检查用户名是否已存在）
     */
    public User findByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
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
        return user;
    }

    /**
     * 注册新用户
     * @return 注册成功返回true，失败返回false
     */
    public boolean register(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 检查用户名是否已存在
            if (findByUsername(username) != null) {
                return false;
            }

            conn = DBUtil.getConnection();
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'guest')";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

