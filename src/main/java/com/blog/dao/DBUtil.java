package com.blog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/blog2_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("数据库连接成功 - URL: " + URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("数据库连接失败！");
            System.err.println("URL: " + URL);
            System.err.println("用户名: " + USERNAME);
            System.err.println("错误信息: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

