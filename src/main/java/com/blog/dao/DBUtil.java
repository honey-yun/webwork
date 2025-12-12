package com.blog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // 优先读取环境变量，便于云端部署；本地开发可留默认值
    private static final String HOST = System.getenv().getOrDefault("MYSQL_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("MYSQL_PORT", "3306");
    private static final String DB = System.getenv().getOrDefault("MYSQL_DB", "blog2_db");
    private static final String USERNAME = System.getenv().getOrDefault("MYSQL_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("MYSQL_PASS", "123456");
    private static final String URL = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8",
            HOST, PORT, DB);

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
