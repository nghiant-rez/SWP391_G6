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
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ProductCreateServlet", urlPatterns = {"/management/products/create"})
public class ProductCreateServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
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

        // Get all categories for dropdown
        List<Category> categories = categoryDAO.getAllActiveCategories();
        request.setAttribute("categories", categories);

        // Forward to form
        request.getRequestDispatcher("/WEB-INF/management/product-form.jsp")
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

        try {
            // Get form parameters
            String categoryIdParam = request.getParameter("categoryId");
            String name = request.getParameter("name");
            String model = request.getParameter("model");
            String brand = request.getParameter("brand");
            String description = request.getParameter("description");
            String specifications = request.getParameter("specifications");
            String basePriceParam = request.getParameter("basePrice");
            String imageUrl = request.getParameter("imageUrl");
            String status = request.getParameter("status");

            // Validate required fields
            if (categoryIdParam == null || categoryIdParam.trim().isEmpty() ||
                name == null || name.trim().isEmpty()) {
                
                request.setAttribute("error", "Category and Product Name are required");
                List<Category> categories = categoryDAO.getAllActiveCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/management/product-form.jsp")
                        .forward(request, response);
                return;
            }

            // Create Product object
            Product product = new Product();
            product.setCategoryId(Integer.parseInt(categoryIdParam));
            product.setName(name.trim());
            product.setModel(model != null ? model.trim() : null);
            product.setBrand(brand != null ? brand.trim() : null);
            product.setDescription(description != null ? description.trim() : null);
            product.setSpecifications(specifications != null ? specifications.trim() : null);
            
            // Parse base price
            if (basePriceParam != null && !basePriceParam.trim().isEmpty()) {
                try {
                    product.setBasePrice(new BigDecimal(basePriceParam.trim()));
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid price format");
                    List<Category> categories = categoryDAO.getAllActiveCategories();
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/WEB-INF/management/product-form.jsp")
                            .forward(request, response);
                    return;
                }
            }
            
            product.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            product.setStatus(status != null && !status.trim().isEmpty() ? status : "ACTIVE");
            product.setCreatedBy(currentUser.getId());

            // Save to database
            boolean success = productDAO.createProduct(product);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/management/products?success=Product created successfully");
            } else {
                request.setAttribute("error", "Failed to create product");
                List<Category> categories = categoryDAO.getAllActiveCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/management/product-form.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            List<Category> categories = categoryDAO.getAllActiveCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/management/product-form.jsp")
                    .forward(request, response);
        }
    }
}
