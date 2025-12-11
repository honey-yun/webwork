package com.blog.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录验证过滤器
 * 如果用户没登录，访问 post_list.jsp 以外的页面，全部跳转 login.jsp
 */
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = path.substring(contextPath.length());

        // 静态资源路径（CSS、JS、图片等）- 必须最先判断
        boolean isStaticResource = relativePath.startsWith("/css/") ||
                relativePath.startsWith("/js/") ||
                relativePath.startsWith("/images/") ||
                relativePath.endsWith(".css") ||
                relativePath.endsWith(".js") ||
                relativePath.endsWith(".jpg") ||
                relativePath.endsWith(".jpeg") ||
                relativePath.endsWith(".png") ||
                relativePath.endsWith(".gif") ||
                relativePath.endsWith(".ico") ||
                relativePath.endsWith(".woff") ||
                relativePath.endsWith(".woff2") ||
                relativePath.endsWith(".ttf") ||
                relativePath.endsWith(".eot") ||
                relativePath.endsWith(".svg");

        // 如果是静态资源，设置正确的Content-Type并放行
        if (isStaticResource) {
            // 确保CSS文件返回正确的Content-Type
            if (relativePath.endsWith(".css")) {
                httpResponse.setContentType("text/css; charset=UTF-8");
            } else if (relativePath.endsWith(".js")) {
                httpResponse.setContentType("application/javascript; charset=UTF-8");
            }
            chain.doFilter(request, response);
            return;
        }

        // 对于非静态资源，设置字符编码
        httpRequest.setCharacterEncoding("UTF-8");
        // 只对HTML页面设置Content-Type
        if (!httpResponse.isCommitted()) {
            httpResponse.setContentType("text/html;charset=UTF-8");
        }
        
        HttpSession session = httpRequest.getSession(false);

        // 允许访问的路径（不需要登录）
        boolean isPublicPath = relativePath.equals("/") ||
                relativePath.equals("/login") ||
                relativePath.equals("/register") ||
                relativePath.equals("/logout") ||
                relativePath.equals("/post_list") ||
                relativePath.equals("/home") ||
                relativePath.equals("/testdb") ||
                relativePath.equals("/test-css.jsp") ||
                relativePath.startsWith("/post_detail") ||
                relativePath.startsWith("/index.jsp");

        // 如果访问的是公开路径，直接放行
        if (isPublicPath) {
            chain.doFilter(request, response);
            return;
        }

        // 检查是否已登录
        if (session == null || session.getAttribute("user_id") == null) {
            // 未登录，跳转到登录页
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        // 已登录，继续执行
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}

