package com.swp391.group6.controller;

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
import java.util.List;

@WebServlet(name = "MyServiceRequestServlet",
    urlPatterns = {"/my-service-requests"})
public class MyServiceRequestServlet
        extends HttpServlet {

    private final ServiceRequestDAO srDAO =
        new ServiceRequestDAO();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);
        if (session == null
                || session.getAttribute("user")
                    == null) {
            response.sendRedirect(
                request.getContextPath()
                + "/login");
            return;
        }

        User currentUser =
            (User) session.getAttribute("user");

        // Only customers can access this page
        if (currentUser.getRoleId() == null
                || currentUser.getRoleId() != 4) {
            response.sendRedirect(
                request.getContextPath()
                + "/home");
            return;
        }

        // Get filter parameters
        String search =
            request.getParameter("search");
        String status =
            request.getParameter("status");
        String requestType =
            request.getParameter("requestType");

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

        // Customer always sees own requests only
        int customerId = currentUser.getId();

        List<ServiceRequest> requests =
            srDAO.findWithFilters(
                search, status, requestType,
                null, customerId,
                currentPage, PAGE_SIZE);

        int totalRequests =
            srDAO.countWithFilters(
                search, status, requestType,
                null, customerId);

        int totalPages = (int) Math.ceil(
            (double) totalRequests / PAGE_SIZE);

        // Set attributes for JSP
        request.setAttribute(
            "requests", requests);
        request.setAttribute(
            "currentPage", currentPage);
        request.setAttribute(
            "totalPages", totalPages);
        request.setAttribute(
            "totalRequests", totalRequests);

        // Preserve filter values
        request.setAttribute(
            "searchValue", search);
        request.setAttribute(
            "statusValue", status);
        request.setAttribute(
            "requestTypeValue", requestType);

        request.getRequestDispatcher(
            "/WEB-INF/my-service-requests.jsp")
            .forward(request, response);
    }
}
