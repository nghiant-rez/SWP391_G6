package com.swp391.group6.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);

    }

    protected void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // kiểm tra xem có sesion chưa, nếu chưa có thì không cần tạo ra.
        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionEmail = (String) session.getAttribute("email");
            if (sessionEmail != null) {
                System.out.println("Use logged out: " + sessionEmail);
            }
            session.invalidate();
        }
        response.sendRedirect("login");
    }
}
