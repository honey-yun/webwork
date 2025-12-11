package com.blog.servlet;

import com.blog.dao.CommentDAO;
import com.blog.model.Comment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 评论Servlet
 */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String postIdStr = request.getParameter("post_id");
        String content = request.getParameter("content");

        if (postIdStr == null || content == null || content.trim().isEmpty()) {
            response.sendRedirect("post_list");
            return;
        }

        try {
            int postId = Integer.parseInt(postIdStr);
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setContent(content.trim());

            if (commentDAO.add(comment)) {
                // 评论成功后跳转到文章详情页
                response.sendRedirect("post_detail?id=" + postId);
            } else {
                response.sendRedirect("post_list");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("post_list");
        }
    }
}

