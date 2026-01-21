package com.swp391.group6.service;

import com.swp391.group6.dao.PermissionDAO;
import com.swp391.group6.model.Permission;

import java.util.List;

public class AuthorizationService {
    private final PermissionDAO permissionDAO;

    public AuthorizationService() {
        this.permissionDAO = new PermissionDAO();
    }

    /**
     * Check if a user has a specific permission
     *
     * @param userId         The user ID to check
     * @param permissionName The permission name (e.g., "USER_CREATE")
     * @return true if user has permission, false otherwise
     */
    public boolean hasPermission(int userId, String permissionName) {
        if (userId <= 0 || permissionName == null ||
                permissionName.trim().isEmpty()) {
            return false;
        }
        return permissionDAO.hasPermission(userId, permissionName);
    }

    /**
     * Get all permissions for a user
     *
     * @param userId The user ID
     * @return List of permissions
     */
    public List<Permission> getUserPermissions(int userId) {
        return permissionDAO.getPermissionsByUserId(userId);
    }
}