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

@WebServlet(name = "CategoryDetailServlet", urlPatterns = {"/management/categories/detail"})
public class CategoryDetailServlet extends HttpServlet {

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

        // Get category ID
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/management/categories");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Category category = categoryDAO.getCategoryById(id);

            if (category == null) {
                request.setAttribute("error", "Category not found.");
                request.getRequestDispatcher("/WEB-INF/management/category-list.jsp")
                        .forward(request, response);
                return;
            }

            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/management/category-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/management/categories");
        }
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

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/management/categories");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            if ("update".equals(action)) {
                // Update category
                String name = request.getParameter("name");
                String description = request.getParameter("description");

                if (name == null || name.trim().isEmpty()) {
                    Category category = categoryDAO.getCategoryById(id);
                    request.setAttribute("category", category);
                    request.setAttribute("error", "Category name is required.");
                    request.getRequestDispatcher("/WEB-INF/management/category-detail.jsp")
                            .forward(request, response);
                    return;
                }

                Category category = new Category();
                category.setId(id);
                category.setName(name.trim());
                category.setDescription(description != null ? description.trim() : "");

                boolean success = categoryDAO.updateCategory(category);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/management/categories/detail?id=" + id);
                } else {
                    category = categoryDAO.getCategoryById(id);
                    request.setAttribute("category", category);
                    request.setAttribute("error", "Failed to update category.");
                    request.getRequestDispatcher("/WEB-INF/management/category-detail.jsp")
                            .forward(request, response);
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/management/categories");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/management/categories");
        }
    }
}
