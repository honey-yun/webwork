package com.blog.servlet;

import com.blog.dao.PostDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 删除文章Servlet（仅管理员）
 */
@WebServlet("/delete_post")
public class DeletePostServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");

        // 检查是否为管理员
        if (session == null || !"admin".equals(role)) {
            response.sendRedirect("post_list");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("post_list");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            if (postDAO.delete(id)) {
                // 删除成功，跳转到文章列表
                response.sendRedirect("post_list");
            } else {
                // 删除失败
                request.setAttribute("error", "删除文章失败");
                response.sendRedirect("post_list");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("post_list");
        }
    }
}

