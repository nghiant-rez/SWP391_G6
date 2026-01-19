package com.swp391.group6.controller;

import com.swp391.group6.dal.UserDAO;
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
 * TEMPORARY servlet for testing RBAC without real login
 * DELETE THIS FILE BEFORE PRODUCTION!
 */
@WebServlet(name = "MockLoginServlet", urlPatterns = {"/mock-login"})
public class MockLoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userIdParam = request.getParameter("userId");
        String logoutParam = request.getParameter("logout");

        // Handle logout
        if ("true".equals(logoutParam)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("mock-login");
            return;
        }

        if (userIdParam == null || userIdParam.isEmpty()) {
            showLoginMenu(request, response, out);
            return;
        }

        // Simulate login
        try {
            int userId = Integer.parseInt(userIdParam);
            User user = userDAO.getUserById(userId);

            if (user == null) {
                out.println("<!DOCTYPE html><html><body>");
                out.println("<h1>User not found: " + userId + "</h1>");
                out.println("<a href='mock-login'>Back</a>");
                out.println("</body></html>");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            out.println("<!DOCTYPE html><html><head>" +
                    "<title>Login Success</title></head><body>");
            out.println("<h1>Logged in as: " + user.getFullName() + "</h1>");
            out.println("<p>User ID: " + user.getId() + "</p>");
            out.println("<p>Email: " + user.getEmail() + "</p>");
            out.println("<p>Role ID: " + user.getRoleId() + "</p>");
            out.println("<hr>");
            out.println("<p><a href='mock-login'>Back to Mock Login Menu" +
                    "</a></p>");
            out.println("</body></html>");

        } catch (NumberFormatException e) {
            out.println("<h1>Invalid userId: " + userIdParam + "</h1>");
        }
    }

    private void showLoginMenu(HttpServletRequest request,
                               HttpServletResponse response, PrintWriter out) {
        String contextPath = request.getContextPath();

        out.println("<!DOCTYPE html><html><head><title>Mock Login</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; max-width: 800px; " +
                "margin: 50px auto; padding: 20px; }");
        out.println(".user-card { border: 1px solid #ddd; padding: 15px; " +
                "margin: 10px 0; border-radius: 5px; }");
        out.println(".user-card:hover { background: #f5f5f5; }");
        out.println("a { text-decoration: none; color: #0066cc; }");
        out.println(".test-links { background: #f0f0f0; padding: 20px; " +
                "border-radius: 5px; margin-top: 20px; }");
        out.println(".success { color: green; }");
        out.println(".warning { color: orange; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>Mock Login (Testing Only)</h1>");
        out.println("<p><strong>WARNING:</strong> " +
                "Delete this servlet before production!</p>");

        // Current session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User currentUser = (User) session.getAttribute("user");
            out.println("<div class='user-card success'>");
            out.println("<p>Currently logged in as: <strong>" +
                    currentUser.getFullName() + "</strong> (ID: " +
                    currentUser.getId() + ")</p>");
            out.println("<p><a href='mock-login?logout=true'>Logout</a></p>");
            out.println("</div>");
        } else {
            out.println("<p class='warning'>Not logged in</p>");
        }

        out.println("<hr>");
        out.println("<h2>Select User to Login As: </h2>");

        // User options
        out.println("<div class='user-card'>");
        out.println("<a href='mock-login?userId=1'><strong>Admin</strong></a>" +
                " (userId=1)");
        out.println("<br><small>Has: USER_READ, USER_CREATE, USER_UPDATE, " +
                "USER_DELETE, PRODUCT_MANAGE</small>");
        out.println("</div>");

        out.println("<div class='user-card'>");
        out.println("<a href='mock-login?userId=2'><strong>Manager</strong>" +
                "</a> (userId=2)");
        out.println("<br><small>Has: USER_READ, PRODUCT_MANAGE</small>");
        out.println("</div>");

        out.println("<div class='user-card'>");
        out.println("<a href='mock-login?userId=3'><strong>Staff</strong>" +
                "</a> (userId=3)");
        out.println("<br><small>Has: No admin permissions</small>");
        out.println("</div>");

        // Test links
        out.println("<div class='test-links'>");
        out.println("<h3>Test Protected URLs:</h3>");
        out.println("<ul>");
        out.println("<li><a href='" + contextPath + "/admin/users'>" +
                "/admin/users</a> - requires USER_READ</li>");
        out.println("<li><a href='" + contextPath + "/admin/users/create'>" +
                "/admin/users/create</a> - requires USER_CREATE</li>");
        out.println("<li><a href='" + contextPath + "/admin/users/delete'>" +
                "/admin/users/delete</a> - requires USER_DELETE</li>");
        out.println("<li><a href='" + contextPath + "/admin/roles'>" +
                "/admin/roles</a> - requires ROLE_READ</li>");
        out.println("</ul>");
        out.println("</div>");

        out.println("</body></html>");
    }
}