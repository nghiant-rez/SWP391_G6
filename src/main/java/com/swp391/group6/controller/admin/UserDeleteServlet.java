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
 * Placeholder for User Delete feature
 * AuthorizationFilter already checked USER_DELETE permission
 */
@WebServlet(name = "UserDeleteServlet", urlPatterns = {"/admin/users/delete"})
public class UserDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // AuthorizationFilter ensures session and user are not null here
        User currentUser = (User) session.getAttribute("user");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Delete User</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; " +
                "margin: 50px auto; padding: 20px; }");
        out.println(".success { background: #d4edda; border: 1px solid " +
                "#c3e6cb; padding: 20px; border-radius: 5px; }");
        out.println(".warning { background: #fff3cd; padding: 15px; " +
                "border-radius: 5px; margin-top: 20px; }");
        out.println("</style></head><body>");

        out.println("<div class='success'>");
        out.println("<h1>✅ Delete User Page</h1>");
        out.println("<p>Authorization successful! You have " +
                "<strong>USER_DELETE</strong> permission.</p>");
        out.println("</div>");

        out.println("<div class='warning'>");
        out.println("<p><strong>⚠️ This is a dangerous permission! " +
                "</strong></p>");
        out.println("<p>Only ADMINISTRATOR role should have this. </p>");
        out.println("<p><strong>Logged in as:</strong> " +
                currentUser.getFullName() + "</p>");
        out.println("</div>");

        out.println("<hr><p><a href='" + request.getContextPath() +
                "/mock-login'>Back to Mock Login</a></p>");
        out.println("</body></html>");
    }
}