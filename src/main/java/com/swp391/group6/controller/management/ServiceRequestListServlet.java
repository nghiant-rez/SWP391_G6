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
import java.util.List;

@WebServlet(name = "ServiceRequestListServlet",
    urlPatterns = {"/management/service-requests"})
public class ServiceRequestListServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();
    private final AuthorizationService authService =
        new AuthorizationService();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);
        User currentUser =
            (User) session.getAttribute("user");

        // Get filter parameters
        String search =
            request.getParameter("search");
        String status =
            request.getParameter("status");
        String requestType =
            request.getParameter("requestType");
        String priority =
            request.getParameter("priority");

        // Pagination
        int currentPage = 1;
        String pageParam =
            request.getParameter("page");
        if (pageParam != null
                && !pageParam.isEmpty()) {
            try {
                currentPage =
                    Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Staff/Manager see all requests
        List<ServiceRequest> requests =
            srDAO.findWithFilters(
                search, status, requestType,
                priority, null,
                currentPage, PAGE_SIZE);

        int totalRequests =
            srDAO.countWithFilters(
                search, status, requestType,
                priority, null);

        int totalPages = (int) Math.ceil(
            (double) totalRequests / PAGE_SIZE);

        // Check permissions for UI buttons
        boolean canProcess =
            authService.hasPermission(
                currentUser.getId(),
                "SERVICE_REQUEST_PROCESS");
        boolean canDelete =
            authService.hasPermission(
                currentUser.getId(),
                "SERVICE_REQUEST_DELETE");

        // Set attributes for JSP
        request.setAttribute(
            "requests", requests);
        request.setAttribute(
            "currentPage", currentPage);
        request.setAttribute(
            "totalPages", totalPages);
        request.setAttribute(
            "totalRequests", totalRequests);
        request.setAttribute(
            "canProcess", canProcess);
        request.setAttribute(
            "canDelete", canDelete);

        // Preserve filter values
        request.setAttribute(
            "searchValue", search);
        request.setAttribute(
            "statusValue", status);
        request.setAttribute(
            "requestTypeValue", requestType);
        request.setAttribute(
            "priorityValue", priority);

        request.getRequestDispatcher(
            "/WEB-INF/management/"
            + "service-request-list.jsp")
            .forward(request, response);
    }
}
