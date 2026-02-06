package com.swp391.group6.controller;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.Role;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Lấy dữ liệu mới nhất từ DB (tránh session cũ)
        User profileUser = userDAO.getUserById(currentUser.getId());
        if (profileUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy role name để hiển thị đẹp
        String roleName = null;
        if (profileUser.getRoleId() != null) {
            Role role = roleDAO.getRoleById(profileUser.getRoleId());
            if (role != null) roleName = role.getName();
        }

        request.setAttribute("profileUser", profileUser);
        request.setAttribute("roleName", roleName);

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}
