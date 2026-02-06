package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Displays list of all system roles
 * Requires ROLE_READ permission (checked by
 * AuthorizationFilter)
 */
@WebServlet(name = "RoleListServlet", urlPatterns = {"/admin/roles"})
public class RoleListServlet extends HttpServlet {

    private RoleDAO roleDAO;

    @Override
    public void init() {
        roleDAO = new RoleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Fetch all roles (include deleted for admin view)
        List<Role> roles = roleDAO.getAllRoles(true);

        // Set attributes for JSP
        request.setAttribute("roles", roles);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/admin/role-list.jsp").forward(request, response);
    }
}
