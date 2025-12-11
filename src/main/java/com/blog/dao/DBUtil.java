package com.blog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 超简化版 DBUtil - 专门针对 Railway 环境
 */
public class DBUtil {
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL驱动加载失败: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("=== 开始数据库连接 ===");
        
        // 1. 尝试 JDBC_DATABASE_URL（最直接）
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        if (jdbcUrl != null && !jdbcUrl.isEmpty()) {
            System.out.println("使用 JDBC_DATABASE_URL");
            return DriverManager.getConnection(jdbcUrl);
        }
        
        // 2. 尝试 DATABASE_URL
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && !dbUrl.isEmpty()) {
            System.out.println("使用 DATABASE_URL: " + maskPassword(dbUrl));
            // 转换为 JDBC 格式
            if (dbUrl.startsWith("mysql://")) {
                String jdbcUrl2 = "jdbc:" + dbUrl + "?useSSL=false&serverTimezone=UTC";
                return DriverManager.getConnection(jdbcUrl2);
            }
        }
        
        // 3. 尝试分开的环境变量（Railway 常用）
        String user = System.getenv("MYSQLUSER");
        String password = System.getenv("MYSQL_ROOT_PASSWORD");
        String host = System.getenv("RAILWAY_TCP_PROXY_DOMAIN");
        String port = System.getenv("RAILWAY_TCP_PROXY_PORT");
        String database = System.getenv("MYSQL_DATABASE");
        
        if (user != null && password != null && host != null && database != null) {
            System.out.println("使用分开的环境变量");
            String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC", 
                                      host, port != null ? port : "3306", database);
            System.out.println("连接URL: " + url);
            System.out.println("用户: " + user);
            return DriverManager.getConnection(url, user, password);
        }
        
        // 4. 其他可能的变量名
        user = System.getenv("MYSQL_USER");
        password = System.getenv("MYSQL_PASSWORD");
        host = System.getenv("MYSQL_HOST");
        port = System.getenv("MYSQL_PORT");
        database = System.getenv("MYSQL_DATABASE");
        
        if (user != null && password != null && host != null && database != null) {
            System.out.println("使用 MYSQL_* 环境变量");
            String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC", 
                                      host, port != null ? port : "3306", database);
            return DriverManager.getConnection(url, user, password);
        }
        
        // 5. 如果都没有，使用 Railway 默认值
        System.out.println("使用 Railway 默认配置");
        String defaultUrl = "jdbc:mysql://containers-us-west-XX.railway.app:XXXX/railway";
        String defaultUser = "root";
        String defaultPassword = "password_from_railway";
        
        throw new SQLException("无法找到数据库配置，请检查环境变量");
    }
    
    private static String maskPassword(String url) {
        if (url == null) return null;
        return url.replaceAll(":([^:@]+)@", ":****@");
    }
    
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
