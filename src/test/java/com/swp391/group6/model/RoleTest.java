package com.swp391.group6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Role Model Tests")
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    @DisplayName("Default constructor creates empty role")
    void testDefaultConstructor() {
        assertNotNull(role);
        assertEquals(0, role.getId());
        assertNull(role.getName());
        assertNull(role.getDescription());
    }

    @Test
    @DisplayName("Parameterized constructor sets values correctly")
    void testParameterizedConstructor() {
        Role testRole = new Role(1, "ADMIN", "Administrator role");

        assertEquals(1, testRole.getId());
        assertEquals("ADMIN", testRole.getName());
        assertEquals("Administrator role", testRole.getDescription());
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void testSettersAndGetters() {
        role.setId(5);
        role.setName("MANAGER");
        role.setDescription("Manager role with permissions");
        role.setDeleted(false);
        role.setCreatedBy(1);
        role.setDeletedBy(null);

        assertEquals(5, role.getId());
        assertEquals("MANAGER", role.getName());
        assertEquals("Manager role with permissions", role.getDescription());
        assertFalse(role.isDeleted());
        assertEquals(1, role.getCreatedBy());
        assertNull(role.getDeletedBy());
    }

    @Test
    @DisplayName("Deleted field handles boolean values")
    void testDeletedField() {
        role.setDeleted(false);
        assertFalse(role.isDeleted());

        role.setDeleted(true);
        assertTrue(role.isDeleted());
    }

    @Test
    @DisplayName("DateTime fields work correctly")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(1);

        role.setCreatedAt(now);
        role.setUpdatedAt(later);

        assertEquals(now, role.getCreatedAt());
        assertEquals(later, role.getUpdatedAt());
    }

    @Test
    @DisplayName("Nullable fields accept null values")
    void testNullableFields() {
        role.setCreatedBy(null);
        role.setDeletedBy(null);
        role.setCreatedAt(null);
        role.setUpdatedAt(null);

        assertNull(role.getCreatedBy());
        assertNull(role.getDeletedBy());
        assertNull(role.getCreatedAt());
        assertNull(role.getUpdatedAt());
    }

    @Test
    @DisplayName("ToString method includes key fields")
    void testToString() {
        role.setId(10);
        role.setName("STAFF");
        role.setDescription("Staff member role");

        String toString = role.toString();

        assertTrue(toString.contains("id=10"));
        assertTrue(toString.contains("name='STAFF'"));
        assertTrue(toString.contains("description='Staff member role'"));
    }

    @Test
    @DisplayName("Role name accepts various formats")
    void testRoleNameFormats() {
        String[] roleNames = {"ADMIN", "MANAGER", "STAFF", "CUSTOMER", "GUEST"};

        for (String name : roleNames) {
            role.setName(name);
            assertEquals(name, role.getName());
        }
    }

    @Test
    @DisplayName("Role description can be long text")
    void testLongDescription() {
        String longDescription = "This is a very long description ".repeat(10);
        role.setDescription(longDescription);

        assertEquals(longDescription, role.getDescription());
    }

    @Test
    @DisplayName("Fully populated role object")
    void testFullyPopulatedRole() {
        LocalDateTime now = LocalDateTime.now();

        role.setId(100);
        role.setName("SUPER_ADMIN");
        role.setDescription("Super administrator with all permissions");
        role.setDeleted(false);
        role.setCreatedAt(now);
        role.setUpdatedAt(now);
        role.setCreatedBy(1);
        role.setDeletedBy(null);

        assertEquals(100, role.getId());
        assertEquals("SUPER_ADMIN", role.getName());
        assertEquals("Super administrator with all permissions", role.getDescription());
        assertFalse(role.isDeleted());
        assertEquals(now, role.getCreatedAt());
        assertEquals(now, role.getUpdatedAt());
        assertEquals(1, role.getCreatedBy());
        assertNull(role.getDeletedBy());
    }

    @Test
    @DisplayName("Deleted role has deletedBy set")
    void testDeletedRoleWithDeletedBy() {
        role.setId(5);
        role.setName("OLD_ROLE");
        role.setDeleted(true);
        role.setDeletedBy(10);

        assertTrue(role.isDeleted());
        assertEquals(10, role.getDeletedBy());
    }

    @Test
    @DisplayName("Active role has null deletedBy")
    void testActiveRoleNullDeletedBy() {
        role.setId(3);
        role.setName("ACTIVE_ROLE");
        role.setDeleted(false);
        role.setDeletedBy(null);

        assertFalse(role.isDeleted());
        assertNull(role.getDeletedBy());
    }

    @Test
    @DisplayName("Role ID can be positive integer")
    void testRoleIdPositive() {
        role.setId(12345);
        assertEquals(12345, role.getId());
    }

    @Test
    @DisplayName("CreatedBy and DeletedBy can have different values")
    void testCreatedByAndDeletedByDifferent() {
        role.setCreatedBy(1);
        role.setDeletedBy(5);

        assertEquals(1, role.getCreatedBy());
        assertEquals(5, role.getDeletedBy());
        assertNotEquals(role.getCreatedBy(), role.getDeletedBy());
    }

    @Test
    @DisplayName("Role timestamps can be in the past")
    void testTimestampsInPast() {
        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 0, 0);

        role.setCreatedAt(pastDate);
        role.setUpdatedAt(pastDate.plusDays(10));

        assertEquals(pastDate, role.getCreatedAt());
        assertTrue(role.getUpdatedAt().isAfter(role.getCreatedAt()));
    }

    @Test
    @DisplayName("Multiple roles can have different properties")
    void testMultipleRolesIndependent() {
        Role role1 = new Role(1, "ADMIN", "Admin");
        Role role2 = new Role(2, "USER", "User");

        assertEquals(1, role1.getId());
        assertEquals(2, role2.getId());
        assertNotEquals(role1.getName(), role2.getName());
        assertNotEquals(role1.getDescription(), role2.getDescription());
    }
}