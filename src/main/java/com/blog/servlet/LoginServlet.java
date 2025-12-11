package com.blog.servlet;

import com.blog.dao.UserDAO;
import com.blog.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录Servlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "用户名和密码不能为空");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // 调试信息
        System.out.println("尝试登录 - 用户名: " + username + ", 密码: " + password);
        
        User user = userDAO.findByUsernameAndPassword(username.trim(), password);
        
        if (user != null) {
            System.out.println("登录成功 - 用户ID: " + user.getId() + ", 角色: " + user.getRole());
            HttpSession session = request.getSession();
            session.setAttribute("user_id", user.getId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());

            // 根据角色跳转
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("post_list");
            }
        } else {
            System.out.println("登录失败 - 用户名或密码错误");
            request.setAttribute("error", "用户名或密码错误");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }
}

