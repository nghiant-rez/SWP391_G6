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

@WebServlet(name = "contractListServlet",
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
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User)session.getAttribute("user");
        int userId = currentUser.getId();

        // -- xác định role để phân quyền ---
        boolean isManager = authService.hasPermission(userId,"CONTRACT_APPROVE");
        boolean isStaff = !isManager && authService.hasPermission(userId,"CONTRACT_CREATE");
        boolean isCustomer = !isManager && !isStaff && authService.hasPermission(userId,"CONTRACT_READ");

        // ownerFilter dùng để giới hạn data
        // MANAGER : null (xem tất cả)
        //STAFF :    "staff"(chỉ hợp đồng mình tạo ra)
        // CUSTOMER: "customer" (chỉ xem hợp dồng của chính mình)
        String ownerFilter = null;
        if(isStaff){
            ownerFilter = "staff";
        } else if(isCustomer){
            ownerFilter = "customer";
        }

        // --- Doc params filter/sort ---
        //Filter / sort params
        String search = request.getParameter("search");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String creatorIdParam = request.getParameter("creatorId");

        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        if (order == null || order.isEmpty()) order = "ASC";

        // creatorId chi co hieu luc khi la MANAGER
        // STAFF/CUSTOMER khong duoc filter nay
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
            }catch (NumberFormatException ignored) {
                currentPage = 1;
            }
        }

        List<Contract> contracts = contractDAO.findWithFilters(search,
                sortBy, order, creatorId, ownerFilter, userId,
                currentPage, PAGE_SIZE);

        int totalContracts = contractDAO.countWithFilters(search,
                sortBy, order, creatorId, ownerFilter, userId);

        int totalPages = (int) Math.ceil((double) totalContracts / PAGE_SIZE);

        if (totalPages < 1) totalPages = 1;

        // --- Quyền UI ---
        boolean canCreate = authService.hasPermission(userId, "CONTRACT_CREATE");
        boolean canUpdate = authService.hasPermission(userId, "CONTRACT_UPDATE");
        boolean canApprove = isManager;

        // Staff list for creator dropdown
        List<User> staffList = contractDAO.getAllStaff();

        // Bind attributes
        request.setAttribute("contracts", contracts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalContracts", totalContracts);
        request.setAttribute("canCreate", canCreate);
        request.setAttribute("canUpdate", canUpdate);
        request.setAttribute("canApprove", canApprove);
        request.setAttribute("staffList", staffList);

        // isManager de JSP quyet dinh hien dropdown
        request.setAttribute("isManager", isManager);
        request.setAttribute("isStaff", isStaff);
        request.setAttribute("currentUserId", userId);

        // Preserve filter values
        request.setAttribute("searchValue", search);
        request.setAttribute("sortByValue", sortBy);
        request.setAttribute("orderValue", order);
        request.setAttribute("creatorIdValue", creatorId);

        request.getRequestDispatcher("/WEB-INF/management/contract-list.jsp")
                .forward(request, response);
    }
}
