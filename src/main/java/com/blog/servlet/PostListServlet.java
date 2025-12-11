package com.blog.servlet;

import com.blog.dao.PostDAO;
import com.blog.model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文章列表Servlet
 */
@WebServlet("/post_list")
public class PostListServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Post> posts = postDAO.findAll();
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/views/post_list.jsp").forward(request, response);
    }
}

