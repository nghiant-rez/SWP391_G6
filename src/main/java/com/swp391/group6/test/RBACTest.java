package com.swp391.group6.test;

import com.swp391.group6.dao.PermissionDAO;
import com.swp391.group6.model.Permission;

import java.util.List;

/**
 * Standalone test for RBAC
 */
public class RBACTest {
    public static void main(String[] args) {
        System.out.println("=== RBAC Infrastructure Test ===\n");
        PermissionDAO permissionDAO = new PermissionDAO();
        // Test 1: Admin (userId=1) should have USER_DELETE
        testPermission(permissionDAO, 1, "USER_DELETE", true);
        // Test 2: Admin (userId=1) should have USER_READ
        testPermission(permissionDAO, 1, "USER_READ", true);
        // Test 3: Manager (userId=2) should have USER_READ
        testPermission(permissionDAO, 2, "USER_READ", true);
        // Test 4: Manager (userId=2) should NOT have USER_DELETE
        testPermission(permissionDAO, 2, "USER_DELETE", false);
        // Test 5: Sales Staff (userId=3) should NOT have USER_READ
        testPermission(permissionDAO, 3, "USER_READ", false);
        // Test 6: Get all permissions for Admin
        System.out.println(
                "\n--- All Permissions for Admin (userId=1) ---");
        List<Permission> adminPerms =
                permissionDAO.getPermissionsByUserId(1);
        if (adminPerms.isEmpty()) {
            System.out.println(
                    "FAIL: No permissions found for Admin!");
        } else {
            for (Permission p : adminPerms) {
                System.out.println("  [OK] " + p.getName() +
                        " (" + p.getDisplayName() + ")");
            }
            System.out.println("Total: " + adminPerms.size() +
                    " permissions");
        }
        // Test 7: Get all permissions for Manager
        System.out.println(
                "\n--- All Permissions for Manager (userId=2) ---");
        List<Permission> managerPerms =
                permissionDAO.getPermissionsByUserId(2);
        if (managerPerms.isEmpty()) {
            System.out.println(
                    "FAIL: No permissions found for Manager!");
        } else {
            for (Permission p : managerPerms) {
                System.out.println("  [OK] " + p.getName() +
                        " (" + p.getDisplayName() + ")");
            }
            System.out.println("Total: " + managerPerms.size() +
                    " permissions");
        }
        // Test 8: Get all permissions in system
        System.out.println("\n--- All Permissions in System ---");
        List<Permission> allPerms = permissionDAO.getAllPermissions();
        for (Permission p : allPerms) {
            System.out.println("  - " + p.getName() + ": " +
                    p.getDescription());
        }
        System.out.println("Total: " + allPerms.size() +
                " permissions");
        System.out.println("\n=== Test Complete ===");
    }

    private static void testPermission(PermissionDAO dao, int userId,
                                       String permission, boolean expected) {
        boolean result = dao.hasPermission(userId, permission);
        String status = (result == expected) ? "[PASS]" : "[FAIL]";
        String expectedStr = expected ? "SHOULD have" :
                "should NOT have";
        System.out.printf("%s User %d %s [%s] -> Got: %s%n",
                status, userId, expectedStr, permission, result);
    }
}