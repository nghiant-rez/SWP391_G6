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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View and edit full permission matrix (all roles x 
 * all permissions)
 * Requires ROLE_UPDATE permission 
 * (checked by AuthorizationFilter)
 */
@WebServlet(name = "PermissionMatrixServlet", 
            urlPatterns = {"/admin/roles/matrix"})
public class PermissionMatrixServlet extends HttpServlet {

    private final RoleDAO roleDAO = new RoleDAO();
    private final PermissionDAO permissionDAO = 
        new PermissionDAO();

    @Override
    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get all roles and permissions
            // Include deleted roles (true) for complete matrix
            List<Role> allRoles = roleDAO.getAllRoles(true);
            List<Permission> allPermissions = 
                permissionDAO.getAllPermissions();
            
            // Build matrix: Map<roleId, 
            //     Map<permissionId, assigned>>
            Map<Integer, Map<Integer, Boolean>> matrix = 
                new HashMap<>();
            
            for (Role role : allRoles) {
                List<Permission> assignedPerms = 
                    permissionDAO.getPermissionsByRoleId(
                        role.getId()
                    );
                Map<Integer, Boolean> permMap = 
                    new HashMap<>();
                for (Permission perm : assignedPerms) {
                    permMap.put(perm.getId(), true);
                }
                matrix.put(role.getId(), permMap);
            }
            
            request.setAttribute("allRoles", allRoles);
            request.setAttribute("allPermissions", 
                allPermissions);
            request.setAttribute("matrix", matrix);
            
            request.getRequestDispatcher(
                "/WEB-INF/admin/permission-matrix.jsp"
            ).forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles?error=" + 
                URLEncoder.encode("Loi he thong: " + 
                    e.getMessage(), 
                    "UTF-8"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, 
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Get all roles (include deleted for update)
            List<Role> allRoles = roleDAO.getAllRoles(true);
            
            // For each role, update its permissions
            for (Role role : allRoles) {
                String[] permIds = request.getParameterValues(
                    "role_" + role.getId()
                );
                
                List<Integer> permissionIds = 
                    new java.util.ArrayList<>();
                if (permIds != null) {
                    for (String idStr : permIds) {
                        try {
                            int id = Integer.parseInt(idStr);
                            if (id > 0) {
                                permissionIds.add(id);
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid IDs
                        }
                    }
                }
                
                // Update permissions for this role
                roleDAO.updateRolePermissions(
                    role.getId(), 
                    permissionIds
                );
            }
            
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles/matrix?message=" + 
                URLEncoder.encode(
                    "Cap nhat ma tran quyen han thanh cong!", 
                    "UTF-8"));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles/matrix?error=" + 
                URLEncoder.encode("Loi he thong: " + 
                    e.getMessage(), 
                    "UTF-8"));
        }
    }
}
