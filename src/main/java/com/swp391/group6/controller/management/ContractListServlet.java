package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ContractDAO;
import com.swp391.group6.model.Contract;
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

@WebServlet(name = "ContractListServlet",
            urlPatterns = {"/management/contracts"})
public class ContractListServlet extends HttpServlet {

    private final ContractDAO contractDAO = new ContractDAO();
    private final AuthorizationService authService = new AuthorizationService();
    private static final int PAGE_SIZE = 5;

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

        User currentUser = (User) session.getAttribute("user");

        // Filter / sort params
        String search = request.getParameter("search");
        String sortBy = request.getParameter("sortBy");
        String order  = request.getParameter("order");
        String creatorIdParam = request.getParameter("creatorId");

        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        if (order  == null || order.isEmpty())  order  = "ASC";

        Integer creatorId = null;
        if (creatorIdParam != null && !creatorIdParam.isEmpty()) {
            try {
                creatorId = Integer.parseInt(creatorIdParam);
            } catch (NumberFormatException ignored) {
                // ignore invalid param
            }
        }

        // Pagination
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException ignored) {
                currentPage = 1;
            }
        }

        List<Contract> contracts = contractDAO.findWithFilters(
            search, sortBy, order, creatorId, currentPage, PAGE_SIZE);

        int totalContracts = contractDAO.countWithFilters(
            search, sortBy, order, creatorId);

        int totalPages = (int) Math.ceil((double) totalContracts / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;

        // Permission checks for UI
        boolean canCreate  = authService.hasPermission(
            currentUser.getId(), "CONTRACT_CREATE");
        boolean canUpdate  = authService.hasPermission(
            currentUser.getId(), "CONTRACT_UPDATE");
        boolean canApprove = authService.hasPermission(
            currentUser.getId(), "CONTRACT_APPROVE");

        // Staff list for creator dropdown
        List<User> staffList = contractDAO.getAllStaff();

        // Bind attributes
        request.setAttribute("contracts",       contracts);
        request.setAttribute("currentPage",     currentPage);
        request.setAttribute("totalPages",      totalPages);
        request.setAttribute("totalContracts",  totalContracts);
        request.setAttribute("canCreate",       canCreate);
        request.setAttribute("canUpdate",       canUpdate);
        request.setAttribute("canApprove",      canApprove);
        request.setAttribute("staffList",       staffList);

        // Preserve filter values
        request.setAttribute("searchValue",    search);
        request.setAttribute("sortByValue",    sortBy);
        request.setAttribute("orderValue",     order);
        request.setAttribute("creatorIdValue", creatorId);

        request.getRequestDispatcher(
            "/WEB-INF/management/contract-list.jsp")
               .forward(request, response);
    }
}
