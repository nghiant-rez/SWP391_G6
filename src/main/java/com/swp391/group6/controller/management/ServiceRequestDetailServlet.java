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

@WebServlet(name = "ServiceRequestDetailServlet",
    urlPatterns = {
        "/management/service-requests/view"})
public class ServiceRequestDetailServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();
    private final AuthorizationService authService =
        new AuthorizationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);
        User currentUser =
            (User) session.getAttribute("user");

        // Parse ID
        String idStr = request.getParameter("id");
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

        // Customer can only view own requests
        boolean isCustomer =
            "CUSTOMER".equals(
                currentUser.getRoleName());
        if (isCustomer && sr.getCustomerId()
                != currentUser.getId()) {
            response.setStatus(
                HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen xem "
                + "yeu cau nay");
            request.getRequestDispatcher(
                "/WEB-INF/error/403.jsp")
                .forward(request, response);
            return;
        }

        // Check permissions for action buttons
        boolean canProcess =
            authService.hasPermission(
                currentUser.getId(),
                "SERVICE_REQUEST_PROCESS");
        boolean canDelete =
            authService.hasPermission(
                currentUser.getId(),
                "SERVICE_REQUEST_DELETE");

        request.setAttribute(
            "serviceRequest", sr);
        request.setAttribute(
            "canProcess", canProcess);
        request.setAttribute(
            "canDelete", canDelete);
        request.setAttribute(
            "isCustomer", isCustomer);

        request.getRequestDispatcher(
            "/WEB-INF/management/"
            + "service-request-detail.jsp")
            .forward(request, response);
    }
}
