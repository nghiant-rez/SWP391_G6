package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.PermissionDAO;
import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.model.Permission;
import com.swp391.group6.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * View role details with assigned permissions
 * Requires ROLE_READ permission (checked by AuthorizationFilter)
 */
@WebServlet(name = "RoleDetailServlet", 
            urlPatterns = {"/admin/roles/view"})
public class RoleDetailServlet extends HttpServlet {

    private final RoleDAO roleDAO = new RoleDAO();
    private final PermissionDAO permissionDAO = new PermissionDAO();

    @Override
    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response)
            throws ServletException, IOException {

        String roleIdStr = request.getParameter("id");
        
        if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles");
            return;
        }

        try {
            int roleId = Integer.parseInt(roleIdStr);
            Role role = roleDAO.getRoleById(roleId);
            
            if (role == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("Khong tim thay vai tro", 
                        "UTF-8"));
                return;
            }
            
            // Get all permissions (for checkboxes)
            List<Permission> allPermissions = 
                permissionDAO.getAllPermissions();
            
            // Get assigned permissions for this role
            List<Permission> assignedPermissions = 
                permissionDAO.getPermissionsByRoleId(roleId);
            
            // Create a list of assigned permission IDs for easy lookup
            List<Integer> assignedIds = assignedPermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());
            
            request.setAttribute("role", role);
            request.setAttribute("allPermissions", allPermissions);
            request.setAttribute("assignedIds", assignedIds);
            
            request.getRequestDispatcher(
                "/WEB-INF/admin/role-detail.jsp"
            ).forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles");
        }
    }
}
