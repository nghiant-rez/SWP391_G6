package com.swp391.group6.controller;

import com.swp391.group6.dao.CategoryDAO;
import com.swp391.group6.dao.ProductDAO;
import com.swp391.group6.model.Category;
import com.swp391.group6.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Public Product Catalog Servlet
 * Customer views available products (public). Search by name, category, etc.
 * Level 2 - BaPX
 */
@WebServlet(name = "PublicProductCatalogServlet", urlPatterns = {"/products"})
public class PublicProductCatalogServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get search/filter parameters
        String keyword = request.getParameter("keyword");
        String categoryIdParam = request.getParameter("categoryId");

        Integer categoryId = null;
        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdParam);
            } catch (NumberFormatException e) {
                // Invalid category ID, ignore
            }
        }

        // Get products based on filters (only show ACTIVE products to public)
        List<Product> products;
        if (keyword != null || categoryId != null) {
            // Search with filters, force status to ACTIVE for public view
            products = productDAO.searchProducts(keyword, categoryId, "ACTIVE");
        } else {
            // Get all active products
            products = productDAO.getAllActiveProducts();
        }

        // Get all active categories for filter dropdown
        List<Category> categories = categoryDAO.getAllActiveCategories();

        // Set attributes for JSP
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("selectedCategory", categoryId);

        // Forward to public products page
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}
