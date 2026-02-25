package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ServiceRequestDAO;
import com.swp391.group6.model.ServiceRequest;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ServiceRequestProcessServlet",
    urlPatterns = {
        "/management/service-requests/process"})
public class ServiceRequestProcessServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

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

        // Load staff list for dropdown
        List<User> staffList =
            srDAO.getActiveStaffList();

        // Build allowed statuses
        List<String> allowedStatuses =
            getAllowedStatuses(sr.getStatus());

        request.setAttribute(
            "serviceRequest", sr);
        request.setAttribute(
            "staffList", staffList);
        request.setAttribute(
            "allowedStatuses", allowedStatuses);

        request.getRequestDispatcher(
            "/WEB-INF/management/"
            + "service-request-process.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Parse ID
        String idStr = request.getParameter("id");
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

        // Get form data
        String newStatus =
            request.getParameter("status");
        String assignedToStr =
            request.getParameter("assignedTo");
        String resolution =
            request.getParameter("resolution");

        // Validate status transition
        if (!ServiceRequest.isValidTransition(
                sr.getStatus(), newStatus)) {
            returnToFormWithError(request, response,
                sr,
                "Khong the chuyen trang thai tu '"
                + sr.getStatusDisplay()
                + "' sang trang thai da chon.");
            return;
        }

        // Parse assignedTo
        Integer assignedTo = null;
        if (assignedToStr != null
                && !assignedToStr.trim().isEmpty()) {
            try {
                assignedTo =
                    Integer.parseInt(assignedToStr);
            } catch (NumberFormatException e) {
                returnToFormWithError(
                    request, response, sr,
                    "Nguoi xu ly khong hop le.");
                return;
            }
        }

        // Resolution required when RESOLVED
        if (ServiceRequest.STATUS_RESOLVED
                .equals(newStatus)) {
            if (resolution == null
                    || resolution.trim().isEmpty()) {
                returnToFormWithError(
                    request, response, sr,
                    "Ket qua xu ly bat buoc "
                    + "khi giai quyet yeu cau.");
                return;
            }
        }

        // Set resolvedAt timestamp
        LocalDateTime resolvedAt = null;
        if (ServiceRequest.STATUS_RESOLVED
                .equals(newStatus)
                && !ServiceRequest.STATUS_RESOLVED
                    .equals(sr.getStatus())) {
            resolvedAt = LocalDateTime.now();
        } else if (ServiceRequest.STATUS_RESOLVED
                .equals(sr.getStatus())) {
            // Keep existing resolvedAt
            resolvedAt = sr.getResolvedAt();
        }

        boolean success = srDAO.updateProcess(
            id, newStatus, assignedTo,
            resolution, resolvedAt);

        if (success) {
            String message = URLEncoder.encode(
                "Cap nhat yeu cau thanh cong",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/management/service-requests"
                + "/view?id=" + id
                + "&message=" + message);
        } else {
            returnToFormWithError(
                request, response, sr,
                "Khong the cap nhat yeu cau. "
                + "Vui long thu lai.");
        }
    }

    private void returnToFormWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            ServiceRequest sr, String error)
            throws ServletException, IOException {

        List<User> staffList =
            srDAO.getActiveStaffList();
        List<String> allowedStatuses =
            getAllowedStatuses(sr.getStatus());

        request.setAttribute(
            "serviceRequest", sr);
        request.setAttribute(
            "staffList", staffList);
        request.setAttribute(
            "allowedStatuses", allowedStatuses);
        request.setAttribute("error", error);

        request.getRequestDispatcher(
            "/WEB-INF/management/"
            + "service-request-process.jsp")
            .forward(request, response);
    }

    private List<String> getAllowedStatuses(
            String currentStatus) {
        List<String> allowed = new ArrayList<>();
        allowed.add(currentStatus);

        switch (currentStatus) {
            case ServiceRequest.STATUS_OPEN:
                allowed.add(
                    ServiceRequest
                        .STATUS_IN_PROGRESS);
                break;
            case ServiceRequest.STATUS_IN_PROGRESS:
                allowed.add(
                    ServiceRequest.STATUS_RESOLVED);
                break;
            case ServiceRequest.STATUS_RESOLVED:
                allowed.add(
                    ServiceRequest.STATUS_CLOSED);
                break;
            default:
                break;
        }

        return allowed;
    }
}
