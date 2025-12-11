<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.blog.model.Post" %>
<%@ page import="com.blog.model.Comment" %>
<%
    Post post = (Post) request.getAttribute("post");
    List<Comment> comments = (List<Comment>) request.getAttribute("comments");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    SimpleDateFormat commentSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int commentCount = comments != null ? comments.size() : 0;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= post.getTitle() %> - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/blog.css">
</head>
<body class="blog-detail-page">
    <div class="blog-detail-container">
        <div class="blog-detail-header">
            <a href="post_list" class="blog-back-link">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M19 12H5M12 19l-7-7 7-7"/>
                </svg>
                返回文章列表
            </a>
            <h1 class="blog-detail-title"><%= post.getTitle() %></h1>
            <div class="blog-detail-meta">
                <div class="blog-post-meta-item">
                    <svg viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                    </svg>
                    <span><strong><%= post.getUsername() %></strong></span>
                </div>
                <div class="blog-post-meta-item">
                    <svg viewBox="0 0 24 24" fill="currentColor">
                        <path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"/>
                    </svg>
                    <span><%= sdf.format(post.getCreateTime()) %></span>
                </div>
            </div>
        </div>

        <div class="blog-detail-content">
            <%= post.getContent() %>
        </div>

        <div class="blog-comments-section">
            <div class="blog-comments-title">💬 评论 (<%= commentCount %>)</div>

            <%
                if (comments != null) {
                    for (Comment comment : comments) {
            %>
                <div class="blog-comment-card">
                    <div class="blog-comment-header">
                        <div class="blog-comment-meta">
                            <span class="blog-comment-author"><%= comment.getUsername() %></span>
                            <span>·</span>
                            <span><%= commentSdf.format(comment.getCreateTime()) %></span>
                        </div>
                        <% if ("admin".equals(session.getAttribute("role"))) { %>
                            <form method="post" action="delete_comment" style="display: inline;" onsubmit="return confirm('确定要删除这条评论吗？');">
                                <input type="hidden" name="id" value="<%= comment.getId() %>">
                                <input type="hidden" name="post_id" value="<%= post.getId() %>">
                                <button type="submit" class="btn-delete-small">删除</button>
                            </form>
                        <% } %>
                    </div>
                    <div class="blog-comment-content"><%= comment.getContent() %></div>
                </div>
            <%
                    }
                }
            %>

            <% if (session.getAttribute("user_id") != null) { %>
                <div class="blog-comment-form">
                    <form method="post" action="comment">
                        <input type="hidden" name="post_id" value="<%= post.getId() %>">
                        <textarea name="content" placeholder="写下你的评论..." required></textarea>
                        <button type="submit" class="blog-comment-submit">发表评论</button>
                    </form>
                </div>
            <% } %>
            <% if (session.getAttribute("user_id") == null) { %>
                <div style="text-align: center; padding: 30px; background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%); border-radius: 10px; margin-top: 20px; border: 2px dashed #ddd;">
                    <div style="font-size: 48px; margin-bottom: 15px;">💬</div>
                    <p style="color: #666; margin-bottom: 10px; font-size: 16px; font-weight: 500;">您当前以<strong style="color: #667eea;">游客</strong>身份浏览</p>
                    <p style="color: #999; margin-bottom: 20px; font-size: 14px;">登录后即可发表评论，参与讨论</p>
                    <div style="display: flex; gap: 15px; justify-content: center; flex-wrap: wrap;">
                        <a href="login" style="color: white; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); text-decoration: none; font-weight: 500; padding: 12px 30px; border-radius: 25px; transition: all 0.3s; display: inline-block;">立即登录</a>
                        <a href="register" style="color: #667eea; border: 2px solid #667eea; text-decoration: none; font-weight: 500; padding: 12px 30px; border-radius: 25px; transition: all 0.3s; display: inline-block;">注册账号</a>
                    </div>
                </div>
            <% } %>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js" charset="UTF-8"></script>
</body>
</html>

