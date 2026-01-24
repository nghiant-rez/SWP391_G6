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

/**
 * Update role information (name and description)
 * Requires ROLE_UPDATE permission (checked by 
 * AuthorizationFilter)
 */
@WebServlet(name = "RoleUpdateServlet", 
            urlPatterns = {"/admin/roles/edit"})
public class RoleUpdateServlet extends HttpServlet {

    private final RoleDAO roleDAO = new RoleDAO();

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
            
            request.setAttribute("role", role);
            request.getRequestDispatcher(
                "/WEB-INF/admin/role-form.jsp"
            ).forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + 
                "/admin/roles");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, 
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String roleIdStr = request.getParameter("id");
            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles");
                return;
            }

            int roleId = Integer.parseInt(roleIdStr);
            Role existingRole = roleDAO.getRoleById(roleId);
            
            if (existingRole == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?error=" + 
                    URLEncoder.encode("Khong tim thay vai tro", 
                        "UTF-8"));
                return;
            }

            String name = request.getParameter("name");
            String description = request.getParameter(
                "description"
            );

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", 
                    "Ten vai tro khong duoc de trong!");
                request.setAttribute("role", existingRole);
                request.getRequestDispatcher(
                    "/WEB-INF/admin/role-form.jsp"
                ).forward(request, response);
                return;
            }

            // Check if name already exists (exclude current role)
            if (roleDAO.isRoleNameExists(name.trim(), roleId)) {
                request.setAttribute("error", 
                    "Ten vai tro da ton tai!");
                request.setAttribute("role", existingRole);
                request.getRequestDispatcher(
                    "/WEB-INF/admin/role-form.jsp"
                ).forward(request, response);
                return;
            }

            existingRole.setName(name.trim());
            existingRole.setDescription(
                description != null ? description.trim() : null
            );

            boolean success = roleDAO.updateRole(existingRole);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/roles?message=" + 
                    URLEncoder.encode(
                        "Cap nhat vai tro thanh cong!", 
                        "UTF-8"));
            } else {
                request.setAttribute("error", 
                    "Co loi xay ra khi cap nhat vai tro!");
                request.setAttribute("role", existingRole);
                request.getRequestDispatcher(
                    "/WEB-INF/admin/role-form.jsp"
                ).forward(request, response);
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
