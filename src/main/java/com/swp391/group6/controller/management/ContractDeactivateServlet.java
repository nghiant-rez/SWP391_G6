package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ContractDAO;
import com.swp391.group6.model.User;
import com.swp391.group6.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ContractDeactivateServlet",
            urlPatterns = {"/contracts/deactivate"})
public class ContractDeactivateServlet extends HttpServlet {

    private final ContractDAO contractDAO = new ContractDAO();
    private final AuthorizationService authService = new AuthorizationService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException{

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User)session.getAttribute("user");

        if (!authService.hasPermission(
                currentUser.getId(), "CONTRACT_APPROVE")){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage", "Access Denied: You need permission 'CONTRACT_APPROVE'.");
            request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                    .forward(request, response);
            return;
        }

        String contractIdParam = request.getParameter("contractId");
        if (contractIdParam == null || contractIdParam.isEmpty()) {
            response.sendRedirect(
                    request.getContextPath() +
                            "/management/contracts?error=Invalid+contract+ID");
            return;
        }

        String creatorIdParam = request.getParameter("creatorId");
        if (creatorIdParam == null || creatorIdParam.isEmpty()) {
            response.sendRedirect(
                    request.getContextPath() + "/management/contracts?error=Invalid+creator+ID");
            return;
        }

        try {
            int creatorId = Integer.parseInt(creatorIdParam);
            boolean success = contractDAO.deactivate(
                    creatorId, currentUser.getId());

            if (success) {
                response.sendRedirect(
                        request.getContextPath() + "/management/contracts?message=Contract+deactivated");
            }else {
                response.sendRedirect(
                        request.getContextPath() + "/management/contracts?error=Deactivate+failed");
            }
        }catch (NumberFormatException e){
            response.sendRedirect(
                    request.getContextPath() + "/management/contracts?error=Invalid+contract+ID");
        }
    }
}
