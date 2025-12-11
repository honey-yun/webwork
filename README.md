# 个人博客系统

基于 Java EE8 + JSP + Servlet + MySQL 的简易 MVC 博客系统。

## 技术栈

- **后端框架**: Java EE 8 (Servlet + JSP)
- **数据库**: MySQL 8.0+
- **构建工具**: Maven
- **服务器**: Tomcat 9.0.63
- **视图技术**: JSP + JSTL

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/blog/
│   │       ├── model/          # JavaBean实体类
│   │       │   ├── User.java
│   │       │   ├── Post.java
│   │       │   └── Comment.java
│   │       ├── dao/             # 数据访问层
│   │       │   ├── DBUtil.java
│   │       │   ├── UserDAO.java
│   │       │   ├── PostDAO.java
│   │       │   └── CommentDAO.java
│   │       ├── servlet/         # 控制层
│   │       │   ├── LoginServlet.java
│   │       │   ├── LogoutServlet.java
│   │       │   ├── PostListServlet.java
│   │       │   ├── PostDetailServlet.java
│   │       │   ├── CommentServlet.java
│   │       │   ├── AdminServlet.java
│   │       │   └── PostAddServlet.java
│   │       └── filter/          # 过滤器
│   │           ├── AuthFilter.java
│   │           └── AdminFilter.java
│   └── webapp/
│       ├── index.jsp
│       └── WEB-INF/
│           ├── views/           # JSP视图
│           │   ├── login.jsp
│           │   ├── post_list.jsp
│           │   ├── post_detail.jsp
│           │   ├── admin.jsp
│           │   └── post_add.jsp
│           └── web.xml
├── database/
│   └── init.sql                 # 数据库初始化脚本
└── pom.xml
```

## 功能特性

### 用户功能
- ✅ 用户登录/退出
- ✅ 查看文章列表
- ✅ 查看文章详情
- ✅ 发表评论

### 管理员功能
- ✅ 所有用户功能
- ✅ 发布文章
- ✅ 访问管理后台

### 权限控制
- ✅ 登录验证过滤器（AuthFilter）
- ✅ 管理员权限过滤器（AdminFilter）

## 快速开始

### 1. 数据库配置

执行 `database/init.sql` 创建数据库和表：

```bash
mysql -u root -p < database/init.sql
```

### 2. 修改数据库连接

编辑 `src/main/java/com/blog/dao/DBUtil.java`，修改数据库连接信息：

```java
private static final String USERNAME = "root";  // 你的MySQL用户名
private static final String PASSWORD = "root";  // 你的MySQL密码
```

### 3. 编译打包

```bash
mvn clean package
```

### 4. 部署到Tomcat

将生成的 `target/webwork2-1.0-SNAPSHOT.war` 复制到 Tomcat 的 `webapps` 目录。

### 5. 启动Tomcat

```bash
# Windows
bin\startup.bat

# Linux/Mac
./bin/startup.sh
```

### 6. 访问系统

打开浏览器访问：`http://localhost:8080/webwork2`

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin  | admin | 管理员 |
| user1  | 123456 | 普通用户 |
| user2  | 123456 | 普通用户 |

## 数据库设计

### users 表
- `id`: 主键，自增
- `username`: 用户名（唯一）
- `password`: 密码
- `role`: 角色（admin/guest）

### posts 表
- `id`: 主键，自增
- `user_id`: 外键，关联 users.id
- `title`: 文章标题
- `content`: 文章内容
- `create_time`: 创建时间

### comments 表
- `id`: 主键，自增
- `post_id`: 外键，关联 posts.id
- `user_id`: 外键，关联 users.id
- `content`: 评论内容
- `create_time`: 创建时间

## 详细部署说明

请参考 [部署教程.md](部署教程.md) 获取详细的部署步骤和常见问题解决方案。

## 开发说明

- 使用 JDBC 直接操作数据库
- 采用 MVC 架构模式
- 使用 JSTL 标签库简化 JSP 开发
- 通过 Filter 实现权限控制
- 所有 SQL 操作封装在 DAO 层

## 注意事项

1. 确保 MySQL 服务已启动
2. 数据库字符集使用 `utf8mb4`
3. 修改数据库连接信息后需要重新编译打包
4. 首次部署前需要执行数据库初始化脚本

## 许可证

本项目仅用于学习和教学目的。

