package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ServiceRequestDAO;
import com.swp391.group6.model.ServiceRequest;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "ServiceRequestCreateServlet",
    urlPatterns = {
        "/management/service-requests/create"})
public class ServiceRequestCreateServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request,
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

        // Load customer's devices for dropdown
        List<String[]> devices =
            srDAO.getCustomerDevices(
                currentUser.getId());
        request.setAttribute("devices", devices);

        request.getRequestDispatcher(
            "/WEB-INF/management/"
            + "service-request-form.jsp")
            .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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

        // Get form data
        String requestType =
            request.getParameter("requestType");
        String subject =
            request.getParameter("subject");
        String description =
            request.getParameter("description");
        String deviceIdStr =
            request.getParameter("deviceId");

        // Priority always defaults to MEDIUM
        // (Manager decides priority later)
        String priority =
            ServiceRequest.PRIORITY_MEDIUM;

        // Validation
        StringBuilder errors = new StringBuilder();

        if (subject == null
                || subject.trim().isEmpty()) {
            errors.append(
                "Chu de khong duoc de trong. ");
        } else if (subject.trim().length() > 200) {
            errors.append(
                "Chu de khong duoc "
                + "vuot qua 200 ky tu. ");
        }

        if (description == null
                || description.trim().isEmpty()) {
            errors.append(
                "Mo ta khong duoc de trong. ");
        } else if (description.length() > 2000) {
            errors.append(
                "Mo ta khong duoc "
                + "vuot qua 2000 ky tu. ");
        }

        if (requestType == null
                || requestType.trim().isEmpty()) {
            errors.append(
                "Vui long chon loai yeu cau. ");
        }

        Integer deviceId = null;
        if (deviceIdStr != null
                && !deviceIdStr.trim().isEmpty()) {
            try {
                deviceId =
                    Integer.parseInt(deviceIdStr);
            } catch (NumberFormatException e) {
                errors.append(
                    "Thiet bi khong hop le. ");
            }
        }

        // If validation errors, return to form
        if (errors.length() > 0) {
            ServiceRequest sr =
                new ServiceRequest();
            sr.setRequestType(requestType);
            sr.setSubject(subject);
            sr.setDescription(description);
            sr.setPriority(priority);
            sr.setDeviceId(deviceId);

            request.setAttribute(
                "serviceRequest", sr);
            request.setAttribute(
                "error", errors.toString());
            request.setAttribute("devices",
                srDAO.getCustomerDevices(
                    currentUser.getId()));

            request.getRequestDispatcher(
                "/WEB-INF/management/"
                + "service-request-form.jsp")
                .forward(request, response);
            return;
        }

        // Create service request
        ServiceRequest sr = new ServiceRequest();
        sr.setCustomerId(currentUser.getId());
        sr.setRequestType(requestType);
        sr.setSubject(subject.trim());
        sr.setDescription(description.trim());
        sr.setPriority(priority);
        sr.setDeviceId(deviceId);

        boolean success = srDAO.create(sr);

        if (success) {
            String message = URLEncoder.encode(
                "Tao yeu cau dich vu thanh cong",
                StandardCharsets.UTF_8);
            response.sendRedirect(
                request.getContextPath()
                + "/my-service-requests"
                + "?message=" + message);
        } else {
            request.setAttribute(
                "serviceRequest", sr);
            request.setAttribute("error",
                "Khong the tao yeu cau. "
                + "Vui long thu lai.");
            request.setAttribute("devices",
                srDAO.getCustomerDevices(
                    currentUser.getId()));

            request.getRequestDispatcher(
                "/WEB-INF/management/"
                + "service-request-form.jsp")
                .forward(request, response);
        }
    }
}
