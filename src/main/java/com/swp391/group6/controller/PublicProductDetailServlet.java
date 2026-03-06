package com.swp391.group6.controller;

import com.swp391.group6.dao.ProductDAO;
import com.swp391.group6.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Public Product Detail Servlet
 * Customer views product specifications and images
 * Level 2 - BaPX
 */
@WebServlet(name = "PublicProductDetailServlet", urlPatterns = {"/products/detail"})
public class PublicProductDetailServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        
        // Validate product ID parameter
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            Product product = productDAO.getProductById(productId);

            // Check if product exists and is active
            if (product == null) {
                request.setAttribute("error", "Product not found");
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            // Only show active products to public
            if (!"ACTIVE".equals(product.getStatus())) {
                request.setAttribute("error", "Product is not available");
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            // Set product attribute for JSP
            request.setAttribute("product", product);

            // Forward to public product detail page
            request.getRequestDispatcher("/product-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Invalid product ID format
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
