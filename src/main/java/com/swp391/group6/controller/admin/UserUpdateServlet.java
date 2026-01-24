package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.Role;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Update user information - Member 3 (Phạm Xuân Ba)
 */
@WebServlet(name = "UserUpdateServlet", urlPatterns = {"/admin/users/edit"})
public class UserUpdateServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();

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
            
            List<Role> roles = roleDAO.getAllRoles(false);
            request.setAttribute("roles", roles);
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
            String roleIdStr = request.getParameter("roleId");
            boolean status = "true".equals(request.getParameter("status"));

            if (fullName == null || fullName.trim().isEmpty() ||
                roleIdStr == null || roleIdStr.trim().isEmpty()) {
                
                List<Role> roles = roleDAO.getAllRoles(false);
                request.setAttribute("roles", roles);
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc!");
                request.setAttribute("user", existingUser);
                request.setAttribute("isEdit", true);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
                return;
            }

            existingUser.setFullName(fullName.trim());
            existingUser.setGender(gender);
            existingUser.setPhone(phone != null ? phone.trim() : null);
            existingUser.setAddress(address != null ? address.trim() : null);
            existingUser.setRoleId(Integer.parseInt(roleIdStr));
            existingUser.setStatus(status);

            boolean success = userDAO.updateUser(existingUser);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/users?message=" + java.net.URLEncoder.encode("User updated successfully!", "UTF-8"));
            } else {
                List<Role> roles = roleDAO.getAllRoles(false);
                request.setAttribute("roles", roles);
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng!");
                request.setAttribute("user", existingUser);
                request.setAttribute("isEdit", true);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            List<Role> roles = roleDAO.getAllRoles(false);
            request.setAttribute("roles", roles);
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
        }
    }
}
