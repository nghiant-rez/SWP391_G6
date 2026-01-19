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
 * AuthorizationFilter already checked USER_CREATE permission
 */
@WebServlet(name = "UserCreateServlet",
        urlPatterns = {"/admin/users/create", "/admin/users/add"})
public class UserCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // AuthorizationFilter ensures session and user are not null here
        User currentUser = (User) session.getAttribute("user");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Create User</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; " +
                "margin: 50px auto; padding: 20px; }");
        out.println(".success { background: #d4edda; border: 1px solid " +
                "#c3e6cb; padding: 20px; border-radius: 5px; }");
        out.println(".info { background: #e7f3ff; padding: 15px; " +
                "border-radius: 5px; margin-top: 20px; }");
        out.println("</style></head><body>");

        out.println("<div class='success'>");
        out.println("<h1>✅ Create User Page</h1>");
        out.println("<p>Authorization successful! You have " +
                "<strong>USER_CREATE</strong> permission.</p>");
        out.println("</div>");

        out.println("<div class='info'>");
        out.println("<p><strong>Logged in as:</strong> " +
                currentUser.getFullName() + "</p>");
        out.println("<p><em>This is a placeholder. Member 3 will implement " +
                "the create user form here.</em></p>");
        out.println("</div>");

        out.println("<hr><p><a href='" + request.getContextPath() +
                "/mock-login'>Back to Mock Login</a></p>");
        out.println("</body></html>");
    }
}