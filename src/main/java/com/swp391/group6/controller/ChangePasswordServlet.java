package com.swp391.group6.controller;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import com.swp391.group6.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles user-initiated password change (all roles).
 * GET  /change-password  -> show form
 * POST /change-password  -> validate & update password
 */
@WebServlet(
    name = "ChangePasswordServlet",
    urlPatterns = {"/change-password"}
)
public class ChangePasswordServlet extends HttpServlet {

    private static final String FORM_JSP =
        "/WEB-INF/change-password.jsp";

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(FORM_JSP)
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String currentPassword =
            request.getParameter("currentPassword");
        String newPassword =
            request.getParameter("newPassword");
        String confirmPassword =
            request.getParameter("confirmPassword");

        // ---- Validation ----
        StringBuilder errors = new StringBuilder();

        if (currentPassword == null
                || currentPassword.trim().isEmpty()) {
            errors.append(
                "Mat khau hien tai khong duoc de trong. ");
        }
        if (newPassword == null
                || newPassword.trim().isEmpty()) {
            errors.append(
                "Mat khau moi khong duoc de trong. ");
        } else if (newPassword.length() < 6) {
            errors.append(
                "Mat khau moi phai co it nhat 6 ky tu. ");
        }
        if (confirmPassword == null
                || confirmPassword.trim().isEmpty()) {
            errors.append(
                "Xac nhan mat khau khong duoc de trong. ");
        }

        if (errors.length() == 0) {
            // Verify current password against the stored hash
            if (!PasswordUtil.checkPassword(
                    currentPassword, currentUser.getPassword())) {
                errors.append(
                    "Mat khau hien tai khong chinh xac. ");
            } else if (!newPassword.equals(confirmPassword)) {
                errors.append(
                    "Mat khau moi va xac nhan khong khop. ");
            } else if (newPassword.equals(currentPassword)) {
                errors.append(
                    "Mat khau moi phai khac mat khau hien tai. ");
            }
        }

        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString().trim());
            request.getRequestDispatcher(FORM_JSP)
                   .forward(request, response);
            return;
        }

        // ---- Update password ----
        String hashed = PasswordUtil.hashPassword(newPassword);
        boolean success =
            userDAO.updatePassword(currentUser.getId(), hashed);

        if (success) {
            // Update the password in the session object so
            // subsequent checks are consistent within the session
            currentUser.setPassword(hashed);
            session.setAttribute("user", currentUser);

            String msg = URLEncoder.encode(
                "Doi mat khau thanh cong",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/change-password?message=" + msg);
        } else {
            request.setAttribute("error",
                "Khong the doi mat khau. Vui long thu lai.");
            request.getRequestDispatcher(FORM_JSP)
                   .forward(request, response);
        }
    }
}
