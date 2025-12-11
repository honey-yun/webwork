package com.blog.servlet;

import com.blog.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册Servlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 验证输入
        if (username == null || password == null || confirmPassword == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "所有字段都不能为空");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        username = username.trim();
        
        // 验证用户名长度
        if (username.length() < 3 || username.length() > 20) {
            request.setAttribute("error", "用户名长度必须在3-20个字符之间");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 验证密码长度
        if (password.length() < 6) {
            request.setAttribute("error", "密码长度至少为6个字符");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 验证两次密码是否一致
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // 尝试注册
        boolean success = userDAO.register(username, password);
        
        if (success) {
            request.setAttribute("success", "注册成功！请登录");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "用户名已存在，请选择其他用户名");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }
}
