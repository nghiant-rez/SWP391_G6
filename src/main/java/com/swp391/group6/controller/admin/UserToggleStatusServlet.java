package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Toggle user status (active/deactive) - Member 3 (Phạm Xuân Ba)
 */
@WebServlet(name = "UserToggleStatusServlet", urlPatterns = {"/admin/users/toggle-status"})
public class UserToggleStatusServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("userId");
        String statusStr = request.getParameter("status");

        if (userIdStr == null || statusStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            boolean newStatus = Boolean.parseBoolean(statusStr);

            boolean success = userDAO.updateUserStatus(userId, newStatus);

            if (success) {
                String message = newStatus ? 
                    "Đã mở khóa tài khoản thành công!" : 
                    "Đã khóa tài khoản thành công!";
                response.sendRedirect(request.getContextPath() + 
                    "/admin/users?message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/users?error=" + java.net.URLEncoder.encode("Không thể thay đổi trạng thái!", "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
