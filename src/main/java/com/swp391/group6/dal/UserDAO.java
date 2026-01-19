package com.swp391.group6.dal;

import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;

import java.sql.*;

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
            System.err.println("❌ UserDAO.getUserById failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
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
            System.err.println("❌ UserDAO.getUserByEmail failed: " + e.getMessage());
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
        user.setAvatarUrl(rs.getString("avatarUrl"));
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