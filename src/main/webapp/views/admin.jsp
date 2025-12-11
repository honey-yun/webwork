<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理后台 - 个人博客系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin-body">
    <!-- 顶部导航栏 -->
    <nav class="admin-navbar">
        <div class="admin-nav-container">
            <div class="admin-logo">管理后台</div>
            <div class="admin-user-info">
                <span>欢迎，${sessionScope.username}</span>
                <a href="logout" class="admin-logout">退出</a>
            </div>
        </div>
    </nav>

    <div class="admin-wrapper">
        <!-- 左侧导航菜单 -->
        <aside class="admin-sidebar" id="adminSidebar">
            <ul class="admin-menu">
                <li class="admin-menu-item active">
                    <a href="admin">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                            <polyline points="9 22 9 12 15 12 15 22"></polyline>
                        </svg>
                        <span>仪表盘</span>
                    </a>
                </li>
                <li class="admin-menu-item">
                    <a href="post_add">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="12" y1="5" x2="12" y2="19"></line>
                            <line x1="5" y1="12" x2="19" y2="12"></line>
                        </svg>
                        <span>写文章</span>
                    </a>
                </li>
                <li class="admin-menu-item">
                    <a href="post_list">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                            <polyline points="14 2 14 8 20 8"></polyline>
                            <line x1="16" y1="13" x2="8" y2="13"></line>
                            <line x1="16" y1="17" x2="8" y2="17"></line>
                            <polyline points="10 9 9 9 8 9"></polyline>
                        </svg>
                        <span>管理文章和评论</span>
                    </a>
                </li>
            </ul>
        </aside>

        <!-- 主内容区 -->
        <main class="admin-main">
            <div class="admin-content">
                <!-- 仪表盘首页 -->
                <div class="admin-dashboard">
                    <h1 class="admin-page-title">仪表盘</h1>
                    
                    <!-- 数据概览卡片 -->
                    <div class="dashboard-cards">
                        <div class="dashboard-card">
                            <div class="dashboard-card-icon" style="background: #667eea;">
                                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                                    <polyline points="14 2 14 8 20 8"></polyline>
                                </svg>
                            </div>
                            <div class="dashboard-card-content">
                                <div class="dashboard-card-value">${totalPosts}</div>
                                <div class="dashboard-card-label">文章总数</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- 移动端菜单切换按钮 -->
    <button class="admin-sidebar-toggle" id="sidebarToggle">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
    </button>

    <script src="${pageContext.request.contextPath}/js/admin.js" charset="UTF-8"></script>
</body>
</html>
