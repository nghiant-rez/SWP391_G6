package com.swp391.group6.controller.management;

import com.swp391.group6.dao.CategoryDAO;
import com.swp391.group6.model.Category;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryListServlet", urlPatterns = {"/management/categories"})
public class CategoryListServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get all categories
        List<Category> categories = categoryDAO.getAllCategories();

        // Set attributes
        request.setAttribute("categories", categories);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/management/category-list.jsp")
                .forward(request, response);
    }
}
