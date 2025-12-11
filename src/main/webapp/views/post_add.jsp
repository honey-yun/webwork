<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>发布文章 - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="back-link top">
            <a href="admin">← 返回管理后台</a>
        </div>
        <h1>发布文章</h1>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="post_add" id="postAddForm">
            <div id="js-error-message" class="error" style="display: none;"></div>
            <div class="form-group">
                <label for="title">标题：</label>
                <input type="text" id="title" name="title" required maxlength="100">
            </div>
            <div class="form-group">
                <label for="content">内容：</label>
                <textarea id="content" name="content" class="large" required></textarea>
            </div>
            <button type="submit">发布文章</button>
        </form>
    </div>
    <script>
        // 管理员发布文章字数检验
        document.addEventListener('DOMContentLoaded', function() {
            const postAddForm = document.getElementById('postAddForm');
            const errorDiv = document.getElementById('js-error-message');
            const titleInput = document.getElementById('title');
            const contentTextarea = document.getElementById('content');

            function showError(message) {
                errorDiv.textContent = message;
                errorDiv.style.display = 'block';
                errorDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }

            function hideError() {
                errorDiv.style.display = 'none';
            }

            if (postAddForm) {
                postAddForm.addEventListener('submit', function(e) {
                    hideError();

                    if (!titleInput.value.trim()) {
                        e.preventDefault();
                        showError('请输入文章标题');
                        titleInput.focus();
                        return false;
                    }

                    if (titleInput.value.trim().length > 100) {
                        e.preventDefault();
                        showError('文章标题不能超过100个字符');
                        titleInput.focus();
                        return false;
                    }

                    if (!contentTextarea.value.trim()) {
                        e.preventDefault();
                        showError('请输入文章内容');
                        contentTextarea.focus();
                        return false;
                    }

                    if (contentTextarea.value.trim().length < 10) {
                        e.preventDefault();
                        showError('文章内容至少需要十个字符');
                        contentTextarea.focus();
                        return false;
                    }
                });

                // 输入时隐藏错误提示
                if (titleInput) {
                    titleInput.addEventListener('input', hideError);
                }
                if (contentTextarea) {
                    contentTextarea.addEventListener('input', hideError);
                }
            }
        });
    </script>
    <script src="${pageContext.request.contextPath}/js/main.js" charset="UTF-8"></script>
</body>
</html>

