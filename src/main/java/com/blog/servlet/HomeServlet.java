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
 * 首页Servlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取最新文章（用于首页展示，限制数量）
        List<Post> allPosts = postDAO.findAll();
        List<Post> latestPosts = allPosts.size() > 5 ? allPosts.subList(0, 5) : allPosts;
        
        request.setAttribute("latestPosts", latestPosts);
        request.getRequestDispatcher("/views/home.jsp").forward(request, response);
    }
}
