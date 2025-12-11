package com.blog.servlet;

import com.blog.dao.PostDAO;
import com.blog.model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 发表文章Servlet（仅管理员）
 */
@WebServlet("/post_add")
public class PostAddServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/post_add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");

        // 检查是否为管理员
        if (userId == null || !"admin".equals(role)) {
            response.sendRedirect("post_list");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || content == null || title.trim().isEmpty() || content.trim().isEmpty()) {
            request.setAttribute("error", "标题和内容不能为空");
            request.getRequestDispatcher("/views/post_add.jsp").forward(request, response);
            return;
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title.trim());
        post.setContent(content.trim());

        if (postDAO.add(post)) {
            // 发表成功后跳转到文章列表
            response.sendRedirect("post_list");
        } else {
            request.setAttribute("error", "发表文章失败，请重试");
            request.getRequestDispatcher("/views/post_add.jsp").forward(request, response);
        }
    }
}

