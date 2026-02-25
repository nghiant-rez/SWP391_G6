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

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/management/products/detail"})
public class ProductDetailServlet extends HttpServlet {

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

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/management/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            Product product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/management/products?error=Product not found");
                return;
            }

            // Get all categories for dropdown (for edit mode)
            List<Category> categories = categoryDAO.getAllActiveCategories();

            request.setAttribute("product", product);
            request.setAttribute("categories", categories);

            // Forward to detail page
            request.getRequestDispatcher("/WEB-INF/management/product-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/management/products");
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
        
        if ("update".equals(action)) {
            updateProduct(request, response, currentUser);
        } else if ("delete".equals(action)) {
            deleteProduct(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/management/products");
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response, 
                               User currentUser)
            throws ServletException, IOException {

        try {
            // Get form parameters
            String idParam = request.getParameter("id");
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
            if (idParam == null || categoryIdParam == null || 
                name == null || name.trim().isEmpty()) {
                
                request.setAttribute("error", "Required fields are missing");
                int productId = Integer.parseInt(idParam);
                Product product = productDAO.getProductById(productId);
                List<Category> categories = categoryDAO.getAllActiveCategories();
                request.setAttribute("product", product);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/management/product-detail.jsp")
                        .forward(request, response);
                return;
            }

            // Create Product object
            Product product = new Product();
            product.setId(Integer.parseInt(idParam));
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
                    Product existingProduct = productDAO.getProductById(product.getId());
                    List<Category> categories = categoryDAO.getAllActiveCategories();
                    request.setAttribute("product", existingProduct);
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/WEB-INF/management/product-detail.jsp")
                            .forward(request, response);
                    return;
                }
            }
            
            product.setImageUrl(imageUrl != null ? imageUrl.trim() : null);
            
            // Validate status field
            String safeStatus = "ACTIVE";
            if ("DISCONTINUED".equals(status)) {
                safeStatus = "DISCONTINUED";
            }
            product.setStatus(safeStatus);

            // Update in database
            boolean success = productDAO.updateProduct(product);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/management/products/detail?id=" + product.getId() + 
                    "&success=Product updated successfully");
            } else {
                request.setAttribute("error", "Failed to update product");
                Product existingProduct = productDAO.getProductById(product.getId());
                List<Category> categories = categoryDAO.getAllActiveCategories();
                request.setAttribute("product", existingProduct);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/management/product-detail.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/management/products?error=An error occurred");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                int productId = Integer.parseInt(idParam);
                boolean success = productDAO.deleteProduct(productId);

                if (success) {
                    response.sendRedirect(request.getContextPath() + 
                        "/management/products?success=Product deleted successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + 
                        "/management/products?error=Failed to delete product");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/management/products");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/management/products?error=An error occurred");
        }
    }
}
