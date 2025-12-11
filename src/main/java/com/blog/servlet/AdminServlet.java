package com.blog.servlet;

import com.blog.dao.PostDAO;
import com.blog.dao.CommentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员后台Servlet
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取统计数据
        int totalPosts = postDAO.findAll().size();
        // 这里可以添加更多统计信息，比如待审核评论数、访问量等
        
        request.setAttribute("totalPosts", totalPosts);
        request.getRequestDispatcher("/views/admin.jsp").forward(request, response);
    }
}

