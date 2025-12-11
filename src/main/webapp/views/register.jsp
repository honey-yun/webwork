<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-page">
    <div class="login-container">
        <h1>用户注册</h1>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error center"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="register" id="registerForm">
            <div class="form-group">
                <label for="username">用户名：</label>
                <input type="text" id="username" name="username" required minlength="3" maxlength="20" 
                       placeholder="3-20个字符">
            </div>
            <div class="form-group">
                <label for="password">密码：</label>
                <input type="password" id="password" name="password" required minlength="6" 
                       placeholder="至少6个字符">
            </div>
            <div class="form-group">
                <label for="confirmPassword">确认密码：</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
            </div>
            <button type="submit" class="full-width">注册</button>
        </form>
        <div style="text-align: center; margin-top: 20px;">
            <a href="login" style="color: #007bff; text-decoration: none;">已有账号？立即登录</a>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js" charset="UTF-8"></script>
    <script>
        // 密码确认验证
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('两次输入的密码不一致！');
                return false;
            }
        });
    </script>
</body>
</html>
