<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">
    <div class="login-container">
        <h1>用户登录</h1>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error center"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="login">
            <div class="form-group">
                <label for="username">用户名：</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密码：</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="full-width">登录</button>
        </form>
        <div style="text-align: center; margin-top: 20px;">
            <a href="register" style="color: #007bff; text-decoration: none;">还没有账号？立即注册</a>
        </div>
        <% if (request.getAttribute("success") != null) { %>
            <div class="success center" style="margin-top: 15px;"><%= request.getAttribute("success") %></div>
        <% } %>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js" charset="UTF-8"></script>
</body>
</html>

