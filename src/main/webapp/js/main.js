// @charset "UTF-8";
// 文件编码：UTF-8

/**
 * 个人博客系统 - 主要JavaScript文件
 */

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化表单验证
    initFormValidation();

    // 初始化其他功能
    initOtherFeatures();
});

/**
 * 初始化表单验证
 */
function initFormValidation() {
    // 登录表单验证
    const loginForm = document.querySelector('form[action="login"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            const username = document.getElementById('username');
            const password = document.getElementById('password');

            if (!username.value.trim()) {
                e.preventDefault();
                // 使用页面内显示错误，避免alert乱码
                showMessage('请输入用户名', 'error');
                username.focus();
                return false;
            }

            if (!password.value.trim()) {
                e.preventDefault();
                showMessage('请输入密码', 'error');
                password.focus();
                return false;
            }
        });
    }


    // 评论表单验证
    const commentForm = document.querySelector('form[action="comment"]');
    if (commentForm) {
        commentForm.addEventListener('submit', function(e) {
            const content = commentForm.querySelector('textarea[name="content"]');

            if (!content.value.trim()) {
                e.preventDefault();
                showMessage('请输入评论内容', 'error');
                content.focus();
                return false;
            }

            if (content.value.trim().length < 5) {
                e.preventDefault();
                showMessage('评论内容至少需要5个字符', 'error');
                content.focus();
                return false;
            }
        });
    }
}

/**
 * 初始化其他功能
 */
function initOtherFeatures() {
    // 自动聚焦到第一个输入框
    const firstInput = document.querySelector('input[type="text"], input[type="password"], textarea');
    if (firstInput && !firstInput.value) {
        firstInput.focus();
    }

    // 文章列表点击效果
    const postItems = document.querySelectorAll('.post-item');
    postItems.forEach(function(item) {
        item.addEventListener('click', function() {
            const link = item.querySelector('a');
            if (link) {
                window.location.href = link.href;
            }
        });
    });

    // 字符计数（可选功能）
    initCharCounter();
}

/**
 * 初始化字符计数器
 */
function initCharCounter() {
    const titleInput = document.getElementById('title');
    if (titleInput) {
        const maxLength = 100;
        const counter = document.createElement('div');
        counter.className = 'char-counter';
        counter.style.cssText = 'text-align: right; color: #999; font-size: 12px; margin-top: 5px;';
        titleInput.parentNode.appendChild(counter);

        function updateCounter() {
            const length = titleInput.value.length;
            counter.textContent = length + '/' + maxLength;
            if (length > maxLength) {
                counter.style.color = 'red';
            } else {
                counter.style.color = '#999';
            }
        }

        titleInput.addEventListener('input', updateCounter);
        updateCounter();
    }

    const contentTextarea = document.getElementById('content');
    if (contentTextarea && contentTextarea.name === 'content') {
        const maxLength = 5000;
        const counter = document.createElement('div');
        counter.className = 'char-counter';
        counter.style.cssText = 'text-align: right; color: #999; font-size: 12px; margin-top: 5px;';
        contentTextarea.parentNode.appendChild(counter);

        function updateCounter() {
            const length = contentTextarea.value.length;
            counter.textContent = length + '/' + maxLength;
            if (length > maxLength * 0.9) {
                counter.style.color = '#ff9800';
            } else {
                counter.style.color = '#999';
            }
        }

        contentTextarea.addEventListener('input', updateCounter);
        updateCounter();
    }
}

/**
 * 显示提示消息
 */
function showMessage(message, type) {
    type = type || 'info';
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message ' + type;
    messageDiv.textContent = message;
    messageDiv.style.cssText = 'padding: 15px; margin: 20px 0; border-radius: 4px;';

    if (type === 'error') {
        messageDiv.style.backgroundColor = '#ffe6e6';
        messageDiv.style.color = 'red';
        messageDiv.style.borderLeft = '4px solid #ff0000';
    } else if (type === 'success') {
        messageDiv.style.backgroundColor = '#d4edda';
        messageDiv.style.color = '#28a745';
        messageDiv.style.borderLeft = '4px solid #28a745';
    } else {
        messageDiv.style.backgroundColor = '#d1ecf1';
        messageDiv.style.color = '#0c5460';
        messageDiv.style.borderLeft = '4px solid #0c5460';
    }

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(messageDiv, container.firstChild);

        // 3秒后自动消失
        setTimeout(function() {
            messageDiv.remove();
        }, 3000);
    }
}

/**
 * 确认删除操作
 */
function confirmDelete(message) {
    return confirm(message || '确定要删除吗？此操作不可恢复！');
}

/**
 * 格式化日期时间
 */
function formatDateTime(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

