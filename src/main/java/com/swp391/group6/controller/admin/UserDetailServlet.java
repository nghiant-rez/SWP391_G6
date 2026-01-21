package com.swp391.group6.controller.admin;

import com.swp391.group6.dal.UserDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * View user information - Member 3 (Phạm Xuân Ba)
 */
@WebServlet(name = "UserDetailServlet", urlPatterns = {"/admin/users/detail"})
public class UserDetailServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("id");
        
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);
            
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/admin/user-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
