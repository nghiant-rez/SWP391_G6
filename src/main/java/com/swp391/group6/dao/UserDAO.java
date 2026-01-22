package com.swp391.group6.dao;

import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;
import com.swp391.group6.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Get a user by ID (for mock login and general use)
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?  AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.getUserById failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all users with search and pagination
     */
    public List<User> getAllUsers(String searchKeyword, int page, int pageSize) {
        List<User> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE isDeleted = 0");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (fullName LIKE ? OR email LIKE ? OR phone LIKE ?)");
        }

        sql.append(" ORDER BY createdAt DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.getAllUsers failed: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Get total count of users
     */
    public int getTotalUsers(String searchKeyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM users WHERE isDeleted = 0");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (fullName LIKE ? OR email LIKE ? OR phone LIKE ?)");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword + "%";
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.getTotalUsers failed: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Create a new user
     */
    public boolean createUser(User user, int createdBy) {
        String sql = "INSERT INTO users (email, password, fullName, gender, phone, address, " +
                "avatarUrl, roleId, status, createdBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, PasswordUtil.hashPassword(user.getPassword()));
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getGender());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getAvatarUrl());
            ps.setObject(8, user.getRoleId());
            ps.setBoolean(9, user.isStatus());
            ps.setInt(10, createdBy);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UserDAO.createUser failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET fullName = ?, gender = ?, phone = ?, " +
                "address = ?, avatarUrl = ?, roleId = ?, status = ?, updatedAt = NOW() " +
                "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getGender());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getAvatarUrl());
            ps.setObject(6, user.getRoleId());
            ps.setBoolean(7, user.isStatus());
            ps.setInt(8, user.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UserDAO.updateUser failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update user status (active/deactive)
     */
    public boolean updateUserStatus(int userId, boolean status) {
        String sql = "UPDATE users SET status = ?, updatedAt = NOW() WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, status);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UserDAO.updateUserStatus failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Check if email already exists
     */
    public boolean isEmailExists(String email, Integer excludeUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND isDeleted = 0";

        if (excludeUserId != null) {
            sql += " AND id != ?";
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            if (excludeUserId != null) {
                ps.setInt(2, excludeUserId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.isEmailExists failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public User findByEmail(String email) {
        return getUserByEmail(email);
    }

    /**
     * Get a user by email (for real login - Member 2 will use this)
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.getUserByEmail failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("fullName"));
        user.setGender(rs.getString("gender"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        
        // avatarUrl might not exist in database
        try {
            user.setAvatarUrl(rs.getString("avatarUrl"));
        } catch (SQLException e) {
            user.setAvatarUrl(null);
        }
        
        user.setRoleId(rs.getObject("roleId") != null ? rs.getInt("roleId") : null);
        user.setStatus(rs.getBoolean("status"));
        user.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        user.setCreatedBy(rs.getObject("createdBy") != null ? rs.getInt("createdBy") : null);
        user.setDeletedBy(rs.getObject("deletedBy") != null ? rs.getInt("deletedBy") : null);

        return user;
    }
}
