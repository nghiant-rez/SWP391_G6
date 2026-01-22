package com.swp391.group6.controller;

import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Quick login for testing - creates admin user directly in session
 */
@WebServlet(name = "QuickLoginServlet", urlPatterns = {"/quick-login"})
public class QuickLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Create admin user manually
        User adminUser = new User();
        adminUser.setId(1);
        adminUser.setEmail("admin@gmail.com");
        adminUser.setFullName("System Administrator");
        adminUser.setRoleId(1);
        adminUser.setStatus(true);
        
        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("user", adminUser);
        
        // Redirect to admin/users
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
