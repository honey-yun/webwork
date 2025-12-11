package com.blog.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 管理员权限过滤器
 * 如果 session.role != 'admin' 时，禁止访问 admin.jsp 和 post_add.jsp
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin", "/post_add", "/delete_post", "/delete_comment"})
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null) {
            httpResponse.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            // 不是管理员，跳转到文章列表
            httpResponse.sendRedirect("post_list");
            return;
        }

        // 是管理员，继续执行
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}

