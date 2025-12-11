package com.blog.servlet;

import com.blog.dao.CommentDAO;
import com.blog.dao.PostDAO;
import com.blog.model.Comment;
import com.blog.model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文章详情Servlet
 */
@WebServlet("/post_detail")
public class PostDetailServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("post_list");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Post post = postDAO.findById(id);
            if (post == null) {
                response.sendRedirect("post_list");
                return;
            }

            List<Comment> comments = commentDAO.findByPostId(id);
            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/views/post_detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("post_list");
        }
    }
}

