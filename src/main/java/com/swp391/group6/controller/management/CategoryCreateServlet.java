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

@WebServlet(name = "CategoryCreateServlet", urlPatterns = {"/management/categories/create"})
public class CategoryCreateServlet extends HttpServlet {

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

        // Forward to create form
        request.getRequestDispatcher("/WEB-INF/management/category-form.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        // Get form data
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Category name is required.");
            request.getRequestDispatcher("/WEB-INF/management/category-form.jsp")
                    .forward(request, response);
            return;
        }

        // Create category object
        Category category = new Category();
        category.setName(name.trim());
        category.setDescription(description != null ? description.trim() : "");
        category.setCreatedBy(currentUser.getId());

        // Save to database
        boolean success = categoryDAO.createCategory(category);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/management/categories");
        } else {
            request.setAttribute("error", "Failed to create category. Please try again.");
            request.getRequestDispatcher("/WEB-INF/management/category-form.jsp")
                    .forward(request, response);
        }
    }
}
