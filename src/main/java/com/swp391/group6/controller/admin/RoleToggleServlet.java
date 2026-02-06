package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Toggles role active/inactive status
 * Requires ROLE_UPDATE permission (checked by 
 * AuthorizationFilter)
 */
@WebServlet(name = "RoleToggleServlet", 
            urlPatterns = {"/admin/roles/toggle"})
public class RoleToggleServlet extends HttpServlet {

    private final RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doPost(HttpServletRequest request, 
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        String roleIdStr = request.getParameter("roleId");
        
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles?error=" + 
                URLEncoder.encode("ID vai tro khong hop le", 
                    "UTF-8"));
            return;
        }

        try {
            int roleId = Integer.parseInt(roleIdStr);

            // Validate roleId is positive
            if (roleId <= 0) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("ID vai tro khong hop le", 
                        "UTF-8"));
                return;
            }

            // Verify role exists before toggling
            if (roleDAO.getRoleById(roleId) == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("Khong tim thay vai tro", 
                        "UTF-8"));
                return;
            }

            // Prevent toggling own role (safety check)
            if (currentUser.getRoleId() != null && 
                currentUser.getRoleId() == roleId) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode(
                        "Khong the vo hieu hoa vai tro cua chinh ban", 
                        "UTF-8"));
                return;
            }

            // Toggle the role status
            boolean success = roleDAO.toggleRoleStatus(
                roleId, 
                currentUser.getId()
            );

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?message=" + 
                    URLEncoder.encode(
                        "Da cap nhat trang thai vai tro", 
                        "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("Khong the cap nhat vai tro", 
                        "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles?error=" + 
                URLEncoder.encode("ID vai tro khong hop le", 
                    "UTF-8"));
        }
    }
}
