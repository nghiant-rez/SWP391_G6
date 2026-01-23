package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Update user information - Member 3 (Phạm Xuân Ba)
 */
@WebServlet(name = "UserUpdateServlet", urlPatterns = {"/admin/users/edit"})
public class UserUpdateServlet extends HttpServlet {

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
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            request.setAttribute("user", user);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            User existingUser = userDAO.getUserById(userId);
            
            if (existingUser == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            String fullName = request.getParameter("fullName");
            String gender = request.getParameter("gender");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String avatarUrl = request.getParameter("avatarUrl");
            String roleIdStr = request.getParameter("roleId");
            boolean status = "true".equals(request.getParameter("status"));

            // Debug logging
            System.out.println("=== Update User Debug Info ===");
            System.out.println("User ID: " + userId);
            System.out.println("Full Name: " + fullName);
            System.out.println("Gender: " + gender);
            System.out.println("Phone: " + phone);
            System.out.println("Address: " + address);
            System.out.println("Avatar URL: " + avatarUrl);
            System.out.println("Role ID: " + roleIdStr);
            System.out.println("Status: " + status);
            System.out.println("==============================");

            if (fullName == null || fullName.trim().isEmpty() ||
                roleIdStr == null || roleIdStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Please fill in all required fields!");
                request.setAttribute("user", existingUser);
                request.setAttribute("isEdit", true);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
                return;
            }

            existingUser.setFullName(fullName.trim());
            existingUser.setGender(gender);
            existingUser.setPhone(phone != null ? phone.trim() : null);
            existingUser.setAddress(address != null ? address.trim() : null);
            existingUser.setAvatarUrl(avatarUrl != null ? avatarUrl.trim() : null);
            existingUser.setRoleId(Integer.parseInt(roleIdStr));
            existingUser.setStatus(status);

            boolean success = userDAO.updateUser(existingUser);

            System.out.println("Update result: " + success);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/users?message=" + java.net.URLEncoder.encode("User updated successfully!", "UTF-8"));
            } else {
                System.err.println("Failed to update user in database!");
                request.setAttribute("error", "An error occurred while updating user!");
                request.setAttribute("user", existingUser);
                request.setAttribute("isEdit", true);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Exception in UserUpdateServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
        }
    }
}
