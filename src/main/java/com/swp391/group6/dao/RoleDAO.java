package com.swp391.group6.dao;

import com.swp391.group6.model.Role;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    /**
     * Get all roles (including deleted for admin view)
     * @param includeDeleted if true, includes soft-deleted roles
     */
    public List<Role> getAllRoles(boolean includeDeleted) {
        List<Role> roles = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM roles"
        );
        
        if (!includeDeleted) {
            sql.append(" WHERE isDeleted = 0");
        }
        
        sql.append(" ORDER BY id ASC");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 sql.toString()
             );
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }

        } catch (SQLException e) {
            System.err.println(
                "RoleDAO.getAllRoles failed: " + e.getMessage()
            );
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Get a single role by ID
     */
    public Role getRoleById(int roleId) {
        String sql = "SELECT * FROM roles WHERE id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "RoleDAO.getRoleById failed: " + e.getMessage()
            );
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Toggle role active/inactive (soft delete)
     * @param roleId the role to toggle
     * @param deletedBy user performing the action
     * @return true if successful
     */
    public boolean toggleRoleStatus(int roleId, 
                                     Integer deletedBy) {
        // First get current status
        Role role = getRoleById(roleId);
        if (role == null) {
            return false;
        }

        String sql;
        if (role.isDeleted()) {
            // Reactivate: set isDeleted=0, deletedBy=NULL
            sql = "UPDATE roles SET isDeleted = 0, " +
                  "deletedBy = NULL, " +
                  "updatedAt = CURRENT_TIMESTAMP " +
                  "WHERE id = ?";
        } else {
            // Deactivate: set isDeleted=1, deletedBy=?
            sql = "UPDATE roles SET isDeleted = 1, " +
                  "deletedBy = ?, " +
                  "updatedAt = CURRENT_TIMESTAMP " +
                  "WHERE id = ?";
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (role.isDeleted()) {
                // Reactivating: only set roleId
                ps.setInt(1, roleId);
            } else {
                // Deactivating: set deletedBy and roleId
                ps.setInt(1, deletedBy);
                ps.setInt(2, roleId);
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(
                "RoleDAO.toggleRoleStatus failed: " + 
                e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update role information (name and description only)
     */
    public boolean updateRole(Role role) {
        String sql = "UPDATE roles SET name = ?, " +
                     "description = ?, " +
                     "updatedAt = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getName());
            ps.setString(2, role.getDescription());
            ps.setInt(3, role.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(
                "RoleDAO.updateRole failed: " + e.getMessage()
            );
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a role name already exists
     * @param name the role name to check
     * @param excludeRoleId exclude this role ID from check
     *                      (for edit mode)
     */
    public boolean isRoleNameExists(String name, 
                                     Integer excludeRoleId) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM roles WHERE name = ?"
        );
        
        if (excludeRoleId != null) {
            sql.append(" AND id != ?");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 sql.toString()
             )) {

            ps.setString(1, name);
            if (excludeRoleId != null) {
                ps.setInt(2, excludeRoleId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "RoleDAO.isRoleNameExists failed: " + 
                e.getMessage()
            );
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Map ResultSet to Role object
     */
    private Role mapResultSetToRole(ResultSet rs) 
            throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        role.setDeleted(rs.getBoolean("isDeleted"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            role.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        if (updatedAt != null) {
            role.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        int createdBy = rs.getInt("createdBy");
        role.setCreatedBy(rs.wasNull() ? null : createdBy);
        
        int deletedBy = rs.getInt("deletedBy");
        role.setDeletedBy(rs.wasNull() ? null : deletedBy);
        
        return role;
    }
}
