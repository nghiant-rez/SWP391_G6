package com.swp391.group6.dao;

import com.swp391.group6.model.Product;
import com.swp391.group6.util.DBContext;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    /**
     * Get all active products (not deleted) with category names
     */
    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as categoryName " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.categoryId = c.id " +
                    "WHERE p.isDeleted = 0 " +
                    "ORDER BY p.createdAt DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Search and filter products with multiple criteria
     */
    public List<Product> searchProducts(String keyword, Integer categoryId, String status) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, c.name as categoryName " +
            "FROM products p " +
            "LEFT JOIN categories c ON p.categoryId = c.id " +
            "WHERE p.isDeleted = 0"
        );

        List<Object> params = new ArrayList<>();

        // Add keyword search
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (p.name LIKE ? OR p.model LIKE ? OR p.brand LIKE ? OR p.description LIKE ?)");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        // Add category filter
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.categoryId = ?");
            params.add(categoryId);
        }

        // Add status filter
        if (status != null && !status.trim().isEmpty() && !status.equals("ALL")) {
            sql.append(" AND p.status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY p.createdAt DESC");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Get product by ID
     */
    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.name as categoryName " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.categoryId = c.id " +
                    "WHERE p.id = ? AND p.isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractProductFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create new product
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (categoryId, name, model, brand, description, " +
                    "specifications, basePrice, imageUrl, status, createdBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getModel());
            ps.setString(4, product.getBrand());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getSpecifications());
            
            if (product.getBasePrice() != null) {
                ps.setBigDecimal(7, product.getBasePrice());
            } else {
                ps.setNull(7, Types.DECIMAL);
            }
            
            ps.setString(8, product.getImageUrl());
            ps.setString(9, product.getStatus() != null ? product.getStatus() : "ACTIVE");
            
            if (product.getCreatedBy() != null) {
                ps.setInt(10, product.getCreatedBy());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update product
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET categoryId = ?, name = ?, model = ?, brand = ?, " +
                    "description = ?, specifications = ?, basePrice = ?, imageUrl = ?, status = ? " +
                    "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getModel());
            ps.setString(4, product.getBrand());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getSpecifications());
            
            if (product.getBasePrice() != null) {
                ps.setBigDecimal(7, product.getBasePrice());
            } else {
                ps.setNull(7, Types.DECIMAL);
            }
            
            ps.setString(8, product.getImageUrl());
            ps.setString(9, product.getStatus());
            ps.setInt(10, product.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete product
     */
    public boolean deleteProduct(int id) {
        String sql = "UPDATE products SET isDeleted = 1 WHERE id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get products count by category
     */
    public int getProductCountByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE categoryId = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Extract Product object from ResultSet
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setCategoryId(rs.getInt("categoryId"));
        product.setCategoryName(rs.getString("categoryName"));
        product.setName(rs.getString("name"));
        product.setModel(rs.getString("model"));
        product.setBrand(rs.getString("brand"));
        product.setDescription(rs.getString("description"));
        product.setSpecifications(rs.getString("specifications"));
        
        BigDecimal basePrice = rs.getBigDecimal("basePrice");
        product.setBasePrice(basePrice);
        
        product.setImageUrl(rs.getString("imageUrl"));
        product.setStatus(rs.getString("status"));
        product.setDeleted(rs.getBoolean("isDeleted"));
        
        Timestamp createdAtTs = rs.getTimestamp("createdAt");
        product.setCreatedAt(createdAtTs != null ? createdAtTs.toLocalDateTime() : null);
        Timestamp updatedAtTs = rs.getTimestamp("updatedAt");
        product.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);

        int createdBy = rs.getInt("createdBy");
        product.setCreatedBy(rs.wasNull() ? null : createdBy);

        return product;
    }
}
