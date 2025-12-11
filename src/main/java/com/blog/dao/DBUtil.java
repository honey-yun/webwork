package com.blog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类 - 适配 Railway 环境变量模板
 */
public class DBUtil {
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL驱动加载成功");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL驱动加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接 - 支持多种环境变量格式
     */
    public static Connection getConnection() throws SQLException {
        printDatabaseConfig(); // 打印配置信息用于调试
        
        // 尝试多种连接方式，按优先级排序
        Connection conn = null;
        
        // 方式1: 直接使用JDBC_DATABASE_URL（最直接）
        conn = tryDirectJdbcUrl();
        if (conn != null) return conn;
        
        // 方式2: 使用DATABASE_URL并转换
        conn = tryDatabaseUrl();
        if (conn != null) return conn;
        
        // 方式3: 使用分开的环境变量（你的格式）
        conn = trySeparateEnvVars();
        if (conn != null) return conn;
        
        // 方式4: 使用本地配置
        conn = tryLocalConfig();
        if (conn != null) return conn;
        
        throw new SQLException("所有数据库连接方式都失败了！");
    }
    
    /**
     * 方式1: 直接使用JDBC_DATABASE_URL环境变量
     */
    private static Connection tryDirectJdbcUrl() {
        try {
            String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
            if (jdbcUrl != null && !jdbcUrl.trim().isEmpty()) {
                System.out.println("🔧 尝试 JDBC_DATABASE_URL");
                return DriverManager.getConnection(jdbcUrl);
            }
        } catch (SQLException e) {
            System.err.println("JDBC_DATABASE_URL连接失败: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 方式2: 使用DATABASE_URL环境变量（需要转换格式）
     */
    private static Connection tryDatabaseUrl() {
        try {
            String dbUrl = System.getenv("DATABASE_URL");
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                System.out.println("🔧 尝试 DATABASE_URL");
                
                // 格式可能是: mysql://用户名:密码@主机:端口/数据库名
                if (dbUrl.startsWith("mysql://")) {
                    // 转换为JDBC格式
                    String jdbcUrl = "jdbc:" + dbUrl;
                    
                    // 确保有必要的参数
                    if (!jdbcUrl.contains("?")) {
                        jdbcUrl += "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                    }
                    
                    System.out.println("转换后的JDBC URL: " + maskPassword(jdbcUrl));
                    return DriverManager.getConnection(jdbcUrl);
                }
            }
        } catch (SQLException e) {
            System.err.println("DATABASE_URL连接失败: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 方式3: 使用分开的环境变量（你的格式）
     * 根据你的模板：mysql://${{MYSQLUSER}}:${{MYSQL_ROOT_PASSWORD}}@${{RAILWAY_TCP_PROXY_DOMAIN}}:${{RAILWAY_TCP_PROXY_PORT}}/${{MYSQL_DATABASE}}
     */
    private static Connection trySeparateEnvVars() {
        try {
            // 从环境变量获取各个部分
            String username = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQL_ROOT_PASSWORD");
            String host = System.getenv("RAILWAY_TCP_PROXY_DOMAIN");
            String port = System.getenv("RAILWAY_TCP_PROXY_PORT");
            String database = System.getenv("MYSQL_DATABASE");
            
            // 同时检查其他可能的变量名
            if (username == null) username = System.getenv("MYSQL_USER");
            if (password == null) password = System.getenv("MYSQLPASSWORD");
            if (host == null) host = System.getenv("MYSQLHOST");
            if (port == null) port = System.getenv("MYSQLPORT");
            if (database == null) database = System.getenv("MYSQLDATABASE");
            
            // 检查必要的变量是否存在
            boolean hasRequiredVars = (username != null && password != null && 
                                      host != null && database != null);
            
            if (hasRequiredVars) {
                System.out.println("🔧 使用分开的环境变量构建连接");
                
                // 构建JDBC URL
                String jdbcUrl;
                if (port != null && !port.trim().isEmpty()) {
                    jdbcUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
                } else {
                    jdbcUrl = String.format("jdbc:mysql://%s/%s", host, database);
                }
                
                // 添加参数
                if (!jdbcUrl.contains("?")) {
                    jdbcUrl += "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                } else {
                    jdbcUrl += "&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                }
                
                System.out.println("构建的JDBC URL: " + maskPassword(jdbcUrl));
                System.out.println("用户名: " + username);
                
                // 使用用户名和密码连接
                return DriverManager.getConnection(jdbcUrl, username, password);
            }
        } catch (SQLException e) {
            System.err.println("分开环境变量连接失败: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 方式4: 使用本地开发配置
     */
    private static Connection tryLocalConfig() throws SQLException {
        System.out.println("🔧 使用本地开发配置");
        String localUrl = "jdbc:mysql://localhost:3306/blog2_db";
        String localUsername = "root";
        String localPassword = "123456";
        
        String jdbcUrl = localUrl + "?useSSL=false&serverTimezone=UTC";
        System.out.println("本地URL: " + jdbcUrl);
        System.out.println("本地用户: " + localUsername);
        
        return DriverManager.getConnection(jdbcUrl, localUsername, localPassword);
    }
    
    /**
     * 打印数据库配置信息（用于调试）
     */
    private static void printDatabaseConfig() {
        System.out.println("=== 数据库配置信息 ===");
        
        // 列出所有可能的环境变量
        String[] envVars = {
            "JDBC_DATABASE_URL",
            "DATABASE_URL",
            "MYSQLUSER", "MYSQL_USER",
            "MYSQL_ROOT_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD",
            "RAILWAY_TCP_PROXY_DOMAIN", "MYSQLHOST", "MYSQL_HOST",
            "RAILWAY_TCP_PROXY_PORT", "MYSQLPORT", "MYSQL_PORT",
            "MYSQL_DATABASE", "MYSQLDATABASE"
        };
        
        for (String var : envVars) {
            String value = System.getenv(var);
            if (value != null) {
                // 安全地显示密码
                if (var.toLowerCase().contains("password")) {
                    System.out.println(var + ": ****");
                } else {
                    System.out.println(var + ": " + value);
                }
            }
        }
        
        System.out.println("=====================");
    }
    
    /**
     * 隐藏URL中的密码（安全考虑）
     */
    private static String maskPassword(String url) {
        if (url == null) return null;
        return url.replaceAll("(:)([^:@/]+)(@)", ":****@")
                  .replaceAll("password=[^&]*", "password=****");
    }
    
    /**
     * 测试数据库连接
     */
    public static void testConnection() {
        System.out.println("\n=== 开始数据库连接测试 ===");
        
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ 数据库连接测试成功！");
                
                // 获取数据库信息
                System.out.println("数据库产品: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("数据库版本: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("当前数据库: " + conn.getCatalog());
                
                closeConnection(conn);
            }
        } catch (SQLException e) {
            System.err.println("❌ 数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== 测试结束 ===\n");
    }
    
    /**
     * 获取当前使用的连接类型
     */
    public static String getConnectionType() {
        if (System.getenv("JDBC_DATABASE_URL") != null) {
            return "Railway JDBC_DATABASE_URL";
        } else if (System.getenv("DATABASE_URL") != null) {
            return "Railway DATABASE_URL";
        } else if (System.getenv("MYSQLUSER") != null || System.getenv("MYSQL_USER") != null) {
            return "Railway 分开的环境变量";
        } else {
            return "本地开发环境";
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
