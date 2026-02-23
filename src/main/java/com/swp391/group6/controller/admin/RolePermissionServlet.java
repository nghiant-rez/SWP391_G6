package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Update role permissions
 * Requires ROLE_UPDATE permission (checked by 
 * AuthorizationFilter)
 */
@WebServlet(name = "RolePermissionServlet", 
            urlPatterns = {"/admin/roles/permissions"})
public class RolePermissionServlet extends HttpServlet {

    private final RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doPost(HttpServletRequest request, 
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String roleIdStr = request.getParameter("roleId");
            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles");
                return;
            }

            // Validate roleId is a valid positive integer
            int roleId;
            try {
                roleId = Integer.parseInt(roleIdStr);
                if (roleId <= 0) {
                    response.sendRedirect(request.getContextPath() + 
                        "/admin/roles?error=" + 
                        URLEncoder.encode("ID vai tro khong hop le!", 
                            "UTF-8"));
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("ID vai tro khong hop le!", 
                        "UTF-8"));
                return;
            }

            Role role = roleDAO.getRoleById(roleId);
            
            if (role == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("Khong tim thay vai tro", 
                        "UTF-8"));
                return;
            }

            // Get selected permission IDs from checkboxes
            String[] permissionIdsStr = request.getParameterValues(
                "permissionIds"
            );
            
            List<Integer> permissionIds = new ArrayList<>();
            if (permissionIdsStr != null) {
                for (String idStr : permissionIdsStr) {
                    try {
                        int id = Integer.parseInt(idStr);
                        // Validate permission ID is positive
                        if (id <= 0) {
                            response.sendRedirect(
                                request.getContextPath() + 
                                "/admin/roles/view?id=" + roleId + 
                                "&error=" + URLEncoder.encode(
                                    "ID quyen han khong hop le!", 
                                    "UTF-8"));
                            return;
                        }
                        permissionIds.add(id);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(
                            request.getContextPath() + 
                            "/admin/roles/view?id=" + roleId + 
                            "&error=" + URLEncoder.encode(
                                "ID quyen han khong hop le!", 
                                "UTF-8"));
                        return;
                    }
                }
            }

            // Update permissions in database
            boolean success = roleDAO.updateRolePermissions(
                roleId, 
                permissionIds
            );

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles/view?id=" + roleId + "&message=" + 
                    URLEncoder.encode(
                        "Cap nhat quyen han thanh cong!", 
                        "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles/view?id=" + roleId + "&error=" + 
                    URLEncoder.encode(
                        "Co loi xay ra khi cap nhat quyen han!", 
                        "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles?error=" + 
                URLEncoder.encode("Loi he thong: " + e.getMessage(), 
                    "UTF-8"));
        }
    }
}
