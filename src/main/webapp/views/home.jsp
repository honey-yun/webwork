<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.blog.model.Post" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人博客 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-logo">我的博客</div>
            <ul class="nav-menu" id="navMenu">
                <li><a href="#home">首页</a></li>
                <li><a href="#blog">博客</a></li>
                <li><a href="#about">关于</a></li>
                <li><a href="#contact">联系</a></li>
                <% if (session.getAttribute("user_id") != null) { %>
                    <li><a href="post_list">文章列表</a></li>
                    <% if ("admin".equals(session.getAttribute("role"))) { %>
                        <li><a href="admin">管理后台</a></li>
                    <% } %>
                    <li><a href="logout">退出</a></li>
                <% } %>
                <% if (session.getAttribute("user_id") == null) { %>
                    <li><a href="login">登录</a></li>
                <% } %>
            </ul>
            <div class="hamburger" id="hamburger">
                <span></span>
                <span></span>
                <span></span>
            </div>
        </div>
    </nav>

    <!-- 1. 首屏 / 英雄区域 - Z式布局 -->
    <section id="home" class="hero-section">
        <div class="hero-container">
            <div class="hero-content">
                <div class="hero-left">
                    <h1 class="hero-name">honeyun</h1>
                    <p class="hero-tagline">网页作者</p>
                    <p class="hero-description">用代码构建世界，用文字记录思考</p>
                </div>
                <div class="hero-right">
                    <div class="hero-image">
                        <div class="avatar-placeholder">
                            <svg width="300" height="300" viewBox="0 0 300 300" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <circle cx="150" cy="150" r="150" fill="#e0e0e0"></circle>
                                <circle cx="150" cy="120" r="50" fill="#999"></circle>
                                <path d="M50 250 Q150 200 250 250" stroke="#999" stroke-width="3" fill="none"></path>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
            <div class="hero-bottom">
                <a href="#blog" class="btn-primary">阅读博客</a>
                <div class="scroll-indicator">
                    <span>向下滚动</span>
                    <svg class="chevron" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M6 9l6 6 6-6"/>
                    </svg>
                </div>
            </div>
        </div>
    </section>

    <!-- 2. 最新博客文章区域 - F式布局与卡片式布局结合 -->
    <section id="blog" class="blog-section">
        <div class="section-container">
            <h2 class="section-title">最新文章</h2>
            <div class="blog-list">
                <%
                    List<Post> latestPosts = (List<Post>) request.getAttribute("latestPosts");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if (latestPosts == null || latestPosts.isEmpty()) {
                %>
                        <div class="empty">暂无文章</div>
                <%
                    } else {
                        for (Post post : latestPosts) {
                            String content = post.getContent();
                            String excerpt = content.length() > 150 ? content.substring(0, 150) + "..." : content;
                %>
                            <article class="blog-item">
                                <div class="blog-meta-left">
                                    <div class="blog-date">
                                        <%= sdf.format(post.getCreateTime()) %>
                                    </div>
                                    <div class="blog-category">技术</div>
                                </div>
                                <div class="blog-content">
                                    <h3 class="blog-title">
                                        <a href="post_detail?id=<%= post.getId() %>"><%= post.getTitle() %></a>
                                    </h3>
                                    <p class="blog-excerpt">
                                        <%= excerpt %>
                                    </p>
                                    <a href="post_detail?id=<%= post.getId() %>" class="blog-read-more">继续阅读 →</a>
                                </div>

                            </article>
                <%
                        }
                    }
                %>
            </div>
            <div class="blog-actions">
                <a href="post_list" class="btn-secondary">查看所有文章</a>
            </div>
        </div>
    </section>

    <!-- 3. 关于我 / 技能区域 - 分栏式布局 -->
    <section id="about" class="about-section">
        <div class="section-container">
            <h2 class="section-title">关于我</h2>
            <div class="about-content">
                <div class="about-left">
                    <h3>个人简介</h3>
                    <p>这里是一段更详细的个人简介。</p>
                    <p>暂时没想好</p>
                </div>
                <div class="about-right">
                    <h3>技能标签</h3>
                    <div class="skills-cloud">
                        <span class="skill-tag skill-large">Java</span>
                        <span class="skill-tag skill-large">Spring</span>
                        <span class="skill-tag skill-medium">MySQL</span>
                        <span class="skill-tag skill-medium">JavaScript</span>
                        <span class="skill-tag skill-medium">HTML/CSS</span>
                        <span class="skill-tag skill-small">React</span>
                        <span class="skill-tag skill-small">Vue.js</span>
                        <span class="skill-tag skill-small">TypeScript</span>
                        <span class="skill-tag skill-small">Node.js</span>
                        <span class="skill-tag skill-small">Git</span>
                        <span class="skill-tag skill-small">Docker</span>
                    </div>
                    <h3 style="margin-top: 30px;">技能水平</h3>
                    <div class="skills-bars">
                        <div class="skill-bar">
                            <div class="skill-label">Java</div>
                            <div class="skill-bar-container">
                                <div class="skill-bar-fill" style="width: 90%"></div>
                            </div>
                        </div>
                        <div class="skill-bar">
                            <div class="skill-label">Spring Framework</div>
                            <div class="skill-bar-container">
                                <div class="skill-bar-fill" style="width: 85%"></div>
                            </div>
                        </div>
                        <div class="skill-bar">
                            <div class="skill-label">MySQL</div>
                            <div class="skill-bar-container">
                                <div class="skill-bar-fill" style="width: 80%"></div>
                            </div>
                        </div>
                        <div class="skill-bar">
                            <div class="skill-label">JavaScript</div>
                            <div class="skill-bar-container">
                                <div class="skill-bar-fill" style="width: 75%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 4. 页脚联系区域 -->
    <footer id="contact" class="footer-section">
        <div class="section-container">
            <h2 class="section-title">联系我</h2>
            <div class="contact-content">
                <div class="contact-info">
                    <div class="contact-item">
                        <strong>邮箱：</strong>
                        <a href="mailto:your.email@example.com">2696955741@qq.com</a>
                    </div>
                    <div class="contact-item">
                        <strong>GitHub：</strong>
                        <a href="https://github.com/yourusername" target="_blank">honey-yun</a>
                    </div>
                    <div class="contact-item">
                        <strong>博客：</strong>
                        <a href="post_list">查看所有文章</a>
                    </div>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 个人博客. 保留所有权利.</p>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/home.js" charset="UTF-8"></script>
</body>
</html>
