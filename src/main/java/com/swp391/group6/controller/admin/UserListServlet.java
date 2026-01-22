package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserListServlet", urlPatterns = {"/admin/users"})
public class UserListServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchKeyword = request.getParameter("search");
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        List<User> users = userDAO.getAllUsers(searchKeyword, currentPage, PAGE_SIZE);
        int totalUsers = userDAO.getTotalUsers(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);

        request.setAttribute("users", users);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUsers", totalUsers);

        request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp").forward(request, response);
    }
}