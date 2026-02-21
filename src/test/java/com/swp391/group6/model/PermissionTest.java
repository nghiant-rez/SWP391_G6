package com.swp391.group6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Permission Model Tests")
class PermissionTest {

    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission();
    }

    @Test
    @DisplayName("Default constructor creates empty permission")
    void testDefaultConstructor() {
        assertNotNull(permission);
        assertEquals(0, permission.getId());
        assertNull(permission.getName());
        assertNull(permission.getDisplayName());
        assertNull(permission.getDescription());
    }

    @Test
    @DisplayName("Parameterized constructor sets values correctly")
    void testParameterizedConstructor() {
        Permission testPermission = new Permission(
            1, "USER_READ", "Read Users", "Permission to view users");

        assertEquals(1, testPermission.getId());
        assertEquals("USER_READ", testPermission.getName());
        assertEquals("Read Users", testPermission.getDisplayName());
        assertEquals("Permission to view users", testPermission.getDescription());
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void testSettersAndGetters() {
        permission.setId(10);
        permission.setName("USER_CREATE");
        permission.setDisplayName("Create User");
        permission.setDescription("Permission to create new users");
        permission.setDeleted(false);
        permission.setCreatedBy(1);
        permission.setDeletedBy(null);

        assertEquals(10, permission.getId());
        assertEquals("USER_CREATE", permission.getName());
        assertEquals("Create User", permission.getDisplayName());
        assertEquals("Permission to create new users", permission.getDescription());
        assertFalse(permission.isDeleted());
        assertEquals(1, permission.getCreatedBy());
        assertNull(permission.getDeletedBy());
    }

    @Test
    @DisplayName("Deleted field handles boolean values")
    void testDeletedField() {
        permission.setDeleted(false);
        assertFalse(permission.isDeleted());

        permission.setDeleted(true);
        assertTrue(permission.isDeleted());
    }

    @Test
    @DisplayName("DateTime fields work correctly")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(1);

        permission.setCreatedAt(now);
        permission.setUpdatedAt(later);

        assertEquals(now, permission.getCreatedAt());
        assertEquals(later, permission.getUpdatedAt());
    }

    @Test
    @DisplayName("Nullable fields accept null values")
    void testNullableFields() {
        permission.setCreatedBy(null);
        permission.setDeletedBy(null);
        permission.setCreatedAt(null);
        permission.setUpdatedAt(null);

        assertNull(permission.getCreatedBy());
        assertNull(permission.getDeletedBy());
        assertNull(permission.getCreatedAt());
        assertNull(permission.getUpdatedAt());
    }

    @Test
    @DisplayName("ToString method includes key fields")
    void testToString() {
        permission.setId(5);
        permission.setName("USER_DELETE");
        permission.setDisplayName("Delete User");

        String toString = permission.toString();

        assertTrue(toString.contains("id=5"));
        assertTrue(toString.contains("name='USER_DELETE'"));
        assertTrue(toString.contains("displayName='Delete User'"));
    }

    @Test
    @DisplayName("Permission name follows naming convention")
    void testPermissionNamingConvention() {
        String[] permissionNames = {
            "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
            "ROLE_READ", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE",
            "SERVICE_REQUEST_READ", "SERVICE_REQUEST_PROCESS",
            "TASK_READ", "TASK_CREATE", "TASK_UPDATE"
        };

        for (String name : permissionNames) {
            permission.setName(name);
            assertEquals(name, permission.getName());
            assertTrue(name.contains("_"), "Permission name should contain underscore");
        }
    }

    @Test
    @DisplayName("Display name is more readable than name")
    void testDisplayNameReadability() {
        permission.setName("USER_DELETE");
        permission.setDisplayName("Delete User");

        assertNotEquals(permission.getName(), permission.getDisplayName());
        assertTrue(permission.getDisplayName().contains(" "));
        assertFalse(permission.getName().contains(" "));
    }

    @Test
    @DisplayName("Description provides detailed information")
    void testDetailedDescription() {
        String detailedDescription =
            "This permission allows the user to delete other user accounts from the system. " +
            "It should only be granted to administrators.";

        permission.setDescription(detailedDescription);
        assertEquals(detailedDescription, permission.getDescription());
        assertTrue(permission.getDescription().length() > 50);
    }

    @Test
    @DisplayName("Fully populated permission object")
    void testFullyPopulatedPermission() {
        LocalDateTime now = LocalDateTime.now();

        permission.setId(100);
        permission.setName("ADMIN_FULL_ACCESS");
        permission.setDisplayName("Full Administrator Access");
        permission.setDescription("Complete access to all system features");
        permission.setDeleted(false);
        permission.setCreatedAt(now);
        permission.setUpdatedAt(now);
        permission.setCreatedBy(1);
        permission.setDeletedBy(null);

        assertEquals(100, permission.getId());
        assertEquals("ADMIN_FULL_ACCESS", permission.getName());
        assertEquals("Full Administrator Access", permission.getDisplayName());
        assertEquals("Complete access to all system features", permission.getDescription());
        assertFalse(permission.isDeleted());
        assertEquals(now, permission.getCreatedAt());
        assertEquals(now, permission.getUpdatedAt());
        assertEquals(1, permission.getCreatedBy());
        assertNull(permission.getDeletedBy());
    }

    @Test
    @DisplayName("Deleted permission has deletedBy set")
    void testDeletedPermissionWithDeletedBy() {
        permission.setId(50);
        permission.setName("DEPRECATED_PERMISSION");
        permission.setDeleted(true);
        permission.setDeletedBy(10);

        assertTrue(permission.isDeleted());
        assertEquals(10, permission.getDeletedBy());
    }

    @Test
    @DisplayName("Active permission has null deletedBy")
    void testActivePermissionNullDeletedBy() {
        permission.setId(30);
        permission.setName("ACTIVE_PERMISSION");
        permission.setDeleted(false);
        permission.setDeletedBy(null);

        assertFalse(permission.isDeleted());
        assertNull(permission.getDeletedBy());
    }

    @Test
    @DisplayName("Permission ID can be any positive integer")
    void testPermissionIdPositive() {
        permission.setId(99999);
        assertEquals(99999, permission.getId());
    }

    @Test
    @DisplayName("CreatedBy and DeletedBy can be different users")
    void testCreatedByAndDeletedByDifferent() {
        permission.setCreatedBy(1);
        permission.setDeletedBy(5);

        assertEquals(1, permission.getCreatedBy());
        assertEquals(5, permission.getDeletedBy());
        assertNotEquals(permission.getCreatedBy(), permission.getDeletedBy());
    }

    @Test
    @DisplayName("Permission timestamps can be updated")
    void testTimestampsUpdate() {
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime updated = LocalDateTime.of(2023, 6, 15, 14, 30);

        permission.setCreatedAt(created);
        permission.setUpdatedAt(updated);

        assertEquals(created, permission.getCreatedAt());
        assertEquals(updated, permission.getUpdatedAt());
        assertTrue(permission.getUpdatedAt().isAfter(permission.getCreatedAt()));
    }

    @Test
    @DisplayName("Multiple permissions are independent")
    void testMultiplePermissionsIndependent() {
        Permission perm1 = new Permission(1, "READ", "Read", "Read access");
        Permission perm2 = new Permission(2, "WRITE", "Write", "Write access");

        assertEquals(1, perm1.getId());
        assertEquals(2, perm2.getId());
        assertNotEquals(perm1.getName(), perm2.getName());
        assertNotEquals(perm1.getDisplayName(), perm2.getDisplayName());
    }

    @Test
    @DisplayName("Permission name can be in uppercase")
    void testPermissionNameUppercase() {
        permission.setName("SERVICE_REQUEST_DELETE");
        assertEquals("SERVICE_REQUEST_DELETE", permission.getName());
        assertEquals(permission.getName(), permission.getName().toUpperCase());
    }

    @Test
    @DisplayName("Empty description is valid")
    void testEmptyDescription() {
        permission.setDescription("");
        assertEquals("", permission.getDescription());
        assertNotNull(permission.getDescription());
    }
}