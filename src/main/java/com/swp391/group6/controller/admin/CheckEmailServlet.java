package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Check if email exists - AJAX endpoint
 */
@WebServlet(name = "CheckEmailServlet", urlPatterns = {"/admin/users/check-email"})
public class CheckEmailServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String userIdStr = request.getParameter("userId");
        
        Integer excludeUserId = null;
        if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                excludeUserId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        boolean exists = false;
        if (email != null && !email.trim().isEmpty()) {
            exists = userDAO.isEmailExists(email.trim(), excludeUserId);
        }

        PrintWriter out = response.getWriter();
        out.print("{\"exists\":" + exists + "}");
        out.flush();
    }
}
