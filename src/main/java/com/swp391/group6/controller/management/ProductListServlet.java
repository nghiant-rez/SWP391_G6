package com.swp391.group6.controller.management;

import com.swp391.group6.dao.CategoryDAO;
import com.swp391.group6.dao.ProductDAO;
import com.swp391.group6.model.Category;
import com.swp391.group6.model.Product;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductListServlet", urlPatterns = {"/management/products"})
public class ProductListServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get filter parameters
        String keyword = request.getParameter("keyword");
        String categoryIdParam = request.getParameter("categoryId");
        String status = request.getParameter("status");

        Integer categoryId = null;
        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdParam);
            } catch (NumberFormatException e) {
                // Invalid category ID, ignore
            }
        }

        // Get products based on filters
        List<Product> products;
        if (keyword != null || categoryId != null || status != null) {
            products = productDAO.searchProducts(keyword, categoryId, status);
        } else {
            products = productDAO.getAllActiveProducts();
        }

        // Get all categories for filter dropdown
        List<Category> categories = categoryDAO.getAllActiveCategories();

        // Set attributes
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("selectedStatus", status != null ? status : "ALL");

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/management/product-list.jsp")
                .forward(request, response);
    }
}
