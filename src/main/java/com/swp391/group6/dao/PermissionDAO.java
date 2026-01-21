package com.swp391.group6.dao;

import com.swp391.group6.model.Permission;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {

    /**
     * Check if a user has a specific permission
     * (You already have this method)
     */
    public boolean hasPermission(int userId, String permissionName) {
        String sql = """
                SELECT COUNT(*) as count
                FROM permissions p
                JOIN rolePermissions rp ON p.id = rp.permissionId
                JOIN users u ON u.roleId = rp.roleId
                WHERE u.id = ? 
                  AND p.name = ?
                  AND p.isDeleted = 0
                  AND u.isDeleted = 0
                  AND u.status = 1
                """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, permissionName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("PermissionDAO.hasPermission failed: " +
                    e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get all permissions for a specific user (through their role)
     * THIS IS THE MISSING METHOD
     */
    public List<Permission> getPermissionsByUserId(int userId) {
        List<Permission> permissions = new ArrayList<>();
        String sql = """
                SELECT DISTINCT p.id, p.name, p.displayName, 
                       p.description, p.isDeleted, p.createdAt, 
                       p.updatedAt, p.createdBy, p.deletedBy
                FROM permissions p
                JOIN rolePermissions rp ON p.id = rp.permissionId
                JOIN users u ON u.roleId = rp.roleId
                WHERE u.id = ?
                  AND p.isDeleted = 0
                  AND u.isDeleted = 0
                  AND u.status = 1
                ORDER BY p.name
                """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permissions.add(mapResultSetToPermission(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "PermissionDAO.getPermissionsByUserId failed: " +
                            e.getMessage());
            e.printStackTrace();
        }

        return permissions;
    }

    /**
     * Get all permissions (for admin UI - role permission assignment)
     */
    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT * FROM permissions WHERE isDeleted = 0 " +
                "ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                permissions.add(mapResultSetToPermission(rs));
            }

        } catch (SQLException e) {
            System.err.println(
                    "PermissionDAO.getAllPermissions failed: " +
                            e.getMessage());
            e.printStackTrace();
        }

        return permissions;
    }

    /**
     * Get permissions assigned to a specific role
     */
    public List<Permission> getPermissionsByRoleId(int roleId) {
        List<Permission> permissions = new ArrayList<>();
        String sql = """
                SELECT p.id, p.name, p.displayName, p.description,
                       p.isDeleted, p.createdAt, p.updatedAt, 
                       p.createdBy, p.deletedBy
                FROM permissions p
                JOIN rolePermissions rp ON p.id = rp.permissionId
                WHERE rp.roleId = ? 
                  AND p.isDeleted = 0
                ORDER BY p.name
                """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permissions.add(mapResultSetToPermission(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "PermissionDAO.getPermissionsByRoleId failed: " +
                            e.getMessage());
            e.printStackTrace();
        }

        return permissions;
    }

    /**
     * Map ResultSet to Permission object
     */
    private Permission mapResultSetToPermission(ResultSet rs)
            throws SQLException {
        Permission permission = new Permission();
        permission.setId(rs.getInt("id"));
        permission.setName(rs.getString("name"));
        permission.setDisplayName(rs.getString("displayName"));
        permission.setDescription(rs.getString("description"));
        permission.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            permission.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        if (updatedAt != null) {
            permission.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        permission.setCreatedBy(rs.getObject("createdBy") != null ?
                rs.getInt("createdBy") : null);
        permission.setDeletedBy(rs.getObject("deletedBy") != null ?
                rs.getInt("deletedBy") : null);

        return permission;
    }
}