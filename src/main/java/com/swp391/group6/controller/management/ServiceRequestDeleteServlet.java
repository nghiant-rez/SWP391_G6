package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ServiceRequestDAO;
import com.swp391.group6.model.ServiceRequest;
import com.swp391.group6.model.User;
import com.swp391.group6.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ServiceRequestDeleteServlet",
    urlPatterns = {
        "/management/service-requests/delete"})
public class ServiceRequestDeleteServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();
    private final AuthorizationService authService =
        new AuthorizationService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);
        if (session == null
                || session.getAttribute("user") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login");
            return;
        }
        User currentUser =
            (User) session.getAttribute("user");

        // Defense in depth permission check
        if (!authService.hasPermission(
                currentUser.getId(),
                "SERVICE_REQUEST_DELETE")) {
            response.setStatus(
                HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen xoa yeu cau");
            request.getRequestDispatcher(
                "/WEB-INF/error/403.jsp")
                .forward(request, response);
            return;
        }

        // Get request ID
        String idStr =
            request.getParameter("requestId");
        if (idStr == null
                || idStr.trim().isEmpty()) {
            String error = URLEncoder.encode(
                "Khong tim thay yeu cau",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "?error=" + error);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            String error = URLEncoder.encode(
                "ID yeu cau khong hop le",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "?error=" + error);
            return;
        }

        // Verify request exists
        ServiceRequest sr = srDAO.findById(id);
        if (sr == null) {
            String error = URLEncoder.encode(
                "Khong tim thay yeu cau",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "?error=" + error);
            return;
        }

        // Soft delete
        boolean success = srDAO.softDelete(id);

        if (success) {
            String message = URLEncoder.encode(
                "Xoa yeu cau thanh cong",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "?message=" + message);
        } else {
            String error = URLEncoder.encode(
                "Khong the xoa yeu cau",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "?error=" + error);
        }
    }
}
