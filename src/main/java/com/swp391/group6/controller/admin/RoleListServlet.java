package com.swp391.group6.controller.admin;

import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * YOUR Feature: Role List
 * AuthorizationFilter already checked ROLE_READ permission
 * TODO: Replace this placeholder with real implementation
 */
@WebServlet(name = "RoleListServlet", urlPatterns = {"/admin/roles"})
public class RoleListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // AuthorizationFilter ensures session and user are not null here
        User currentUser = (User) session.getAttribute("user");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Role List</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; " +
                "margin: 50px auto; padding: 20px; }");
        out.println(".success { background: #d4edda; border: 1px solid " +
                "#c3e6cb; padding: 20px; border-radius: 5px; }");
        out.println(".info { background: #e7f3ff; padding: 15px; " +
                "border-radius: 5px; margin-top: 20px; }");
        out.println(".todo { background: #fff3cd; padding: 15px; " +
                "border-radius: 5px; margin-top: 20px; }");
        out.println("</style></head><body>");

        out.println("<div class='success'>");
        out.println("<h1>✅ Role List Page</h1>");
        out.println("<p>Authorization successful! You have " +
                "<strong>ROLE_READ</strong> permission.</p>");
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<p><strong>Logged in as:</strong> " +
                currentUser.getFullName() + "</p>");
        out.println("<p><strong>Role ID:</strong> " +
                currentUser.getRoleId() + "</p>");
        out.println("</div>");

        out.println("<div class='todo'>");
        out.println("<h3>📋 YOUR TODO List:</h3>");
        out.println("<ul>");
        out.println("<li>Create RoleDAO with getAllRoles()</li>");
        out.println("<li>Query roles from database</li>");
        out.println("<li>Display in a table</li>");
        out.println("<li>Add link to view/edit permissions for each role</li>");
        out.println("</ul>");
        out.println("</div>");

        out.println("<hr><p><a href='" + request.getContextPath() +
                "/mock-login'>Back to Mock Login</a></p>");
        out.println("</body></html>");
    }
}