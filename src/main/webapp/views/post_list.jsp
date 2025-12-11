<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.blog.model.Post" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>文章列表 - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/blog.css">
</head>
<body class="blog-list-page">
    <div class="blog-list-container">
        <div class="blog-list-header">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px; width: 100%;">
                <div style="display: flex; align-items: center; gap: 20px; flex-wrap: wrap;">
                    <% if (session.getAttribute("user_id") == null) { %>
                        <a href="home" style="display: inline-flex; align-items: center; gap: 8px; color: #667eea; text-decoration: none; font-weight: 500; padding: 8px 16px; border: 2px solid #667eea; border-radius: 25px; transition: all 0.3s;">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                                <polyline points="9 22 9 12 15 12 15 22"/>
                            </svg>
                            返回首页
                        </a>
                    <% } %>
                    <% if ("admin".equals(session.getAttribute("role"))) { %>
                        <a href="admin" style="display: inline-flex; align-items: center; gap: 8px; color: white; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); text-decoration: none; font-weight: 500; padding: 8px 16px; border-radius: 25px; transition: all 0.3s;">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                                <line x1="9" y1="3" x2="9" y2="21"/>
                                <line x1="3" y1="9" x2="21" y2="9"/>
                            </svg>
                            返回管理后台
                        </a>
                    <% } %>
                    <h1 style="margin: 0;">📚 文章列表</h1>
                </div>
                <div class="blog-user-info">
                    <% if (session.getAttribute("user_id") != null) { %>
                        <span style="color: #666;">欢迎，<strong><%= session.getAttribute("username") %></strong>！</span>
                        <a href="logout">退出</a>
                    <% } else { %>
                        <span style="color: #999; font-size: 14px;">👤 游客模式</span>
                        <a href="login">登录</a>
                        <a href="register">注册</a>
                    <% } %>
                </div>
            </div>
        </div>

        <%
            List<Post> posts = (List<Post>) request.getAttribute("posts");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (posts == null || posts.isEmpty()) {
        %>
                <div class="blog-empty">
                    <div class="blog-empty-icon">📝</div>
                    <div class="blog-empty-text">暂无文章</div>
                </div>
        <%
            } else {
                for (Post post : posts) {
                    String content = post.getContent();
                    String excerpt = content.length() > 150 ? content.substring(0, 150) + "..." : content;
        %>
                    <div class="blog-post-card" onclick="location.href='post_detail?id=<%= post.getId() %>'">
                        <div class="blog-post-title"><%= post.getTitle() %></div>
                        <div class="blog-post-meta">
                            <div class="blog-post-meta-item">
                                <svg viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                                </svg>
                                <span><%= post.getUsername() %></span>
                            </div>
                            <div class="blog-post-meta-item">
                                <svg viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"/>
                                </svg>
                                <span><%= sdf.format(post.getCreateTime()) %></span>
                            </div>
                        </div>
                        <div class="blog-post-excerpt">
                            <%= excerpt %>
                        </div>
                        <div class="blog-post-footer">
                            <a href="post_detail?id=<%= post.getId() %>" class="blog-read-more-btn" onclick="event.stopPropagation();">
                                继续阅读 →
                            </a>
                            <% if ("admin".equals(session.getAttribute("role"))) { %>
                                <div class="blog-post-actions" onclick="event.stopPropagation();">
                                    <form method="post" action="delete_post" style="display: inline;" onsubmit="return confirm('确定要删除这篇文章吗？删除后无法恢复！');">
                                        <input type="hidden" name="id" value="<%= post.getId() %>">
                                        <button type="submit" class="btn-delete-small">删除</button>
                                    </form>
                                </div>
                            <% } %>
                        </div>
                    </div>
        <%
                }
            }
        %>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js" charset="UTF-8"></script>
</body>
</html>

