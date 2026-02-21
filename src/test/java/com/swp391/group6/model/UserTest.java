package com.swp391.group6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Default constructor creates empty user")
    void testDefaultConstructor() {
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertNull(user.getEmail());
        assertNull(user.getFullName());
    }

    @Test
    @DisplayName("Parameterized constructor sets values correctly")
    void testParameterizedConstructor() {
        User testUser = new User(1, "test@example.com", "Test User", 2);

        assertEquals(1, testUser.getId());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("Test User", testUser.getFullName());
        assertEquals(2, testUser.getRoleId());
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void testSettersAndGetters() {
        user.setId(10);
        user.setEmail("user@example.com");
        user.setPassword("hashedPassword123");
        user.setFullName("John Doe");
        user.setGender("MALE");
        user.setPhone("0123456789");
        user.setAddress("123 Main St");
        user.setAvatarUrl("http://example.com/avatar.jpg");
        user.setRoleId(3);
        user.setRoleName("ADMIN");
        user.setStatus(true);
        user.setDeleted(false);
        user.setCreatedBy(1);
        user.setDeletedBy(null);

        assertEquals(10, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getPassword());
        assertEquals("John Doe", user.getFullName());
        assertEquals("MALE", user.getGender());
        assertEquals("0123456789", user.getPhone());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("http://example.com/avatar.jpg", user.getAvatarUrl());
        assertEquals(3, user.getRoleId());
        assertEquals("ADMIN", user.getRoleName());
        assertTrue(user.isStatus());
        assertFalse(user.isDeleted());
        assertEquals(1, user.getCreatedBy());
        assertNull(user.getDeletedBy());
    }

    @Test
    @DisplayName("Status setter and getter handle boolean values")
    void testStatusBoolean() {
        user.setStatus(true);
        assertTrue(user.isStatus());

        user.setStatus(false);
        assertFalse(user.isStatus());
    }

    @Test
    @DisplayName("Deleted setter and getter handle boolean values")
    void testDeletedBoolean() {
        user.setDeleted(false);
        assertFalse(user.isDeleted());

        user.setDeleted(true);
        assertTrue(user.isDeleted());
    }

    @Test
    @DisplayName("LocalDateTime fields work correctly")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now.plusDays(1));

        assertEquals(now, user.getCreatedAt());
        assertEquals(now.plusDays(1), user.getUpdatedAt());
    }

    @Test
    @DisplayName("Null values are handled correctly for nullable fields")
    void testNullableFields() {
        user.setRoleId(null);
        user.setAvatarUrl(null);
        user.setAddress(null);
        user.setCreatedBy(null);
        user.setDeletedBy(null);

        assertNull(user.getRoleId());
        assertNull(user.getAvatarUrl());
        assertNull(user.getAddress());
        assertNull(user.getCreatedBy());
        assertNull(user.getDeletedBy());
    }

    @Test
    @DisplayName("ToString method includes key fields")
    void testToString() {
        user.setId(5);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setRoleId(2);
        user.setStatus(true);

        String toString = user.toString();

        assertTrue(toString.contains("id=5"));
        assertTrue(toString.contains("email='test@example.com'"));
        assertTrue(toString.contains("fullName='Test User'"));
        assertTrue(toString.contains("roleId=2"));
        assertTrue(toString.contains("status=true"));
    }

    @Test
    @DisplayName("Gender field accepts different values")
    void testGenderValues() {
        user.setGender("MALE");
        assertEquals("MALE", user.getGender());

        user.setGender("FEMALE");
        assertEquals("FEMALE", user.getGender());

        user.setGender("OTHER");
        assertEquals("OTHER", user.getGender());
    }

    @Test
    @DisplayName("Phone number field stores string correctly")
    void testPhoneNumber() {
        user.setPhone("0987654321");
        assertEquals("0987654321", user.getPhone());

        // Test with different formats
        user.setPhone("+84 123 456 789");
        assertEquals("+84 123 456 789", user.getPhone());
    }

    @Test
    @DisplayName("Email field stores various email formats")
    void testEmailFormats() {
        String[] validEmails = {
            "user@example.com",
            "test.user@example.co.uk",
            "admin+test@domain.com"
        };

        for (String email : validEmails) {
            user.setEmail(email);
            assertEquals(email, user.getEmail());
        }
    }

    @Test
    @DisplayName("User with all fields populated")
    void testFullyPopulatedUser() {
        LocalDateTime now = LocalDateTime.now();

        user.setId(100);
        user.setEmail("complete@example.com");
        user.setPassword("$2a$10$hashedPassword");
        user.setFullName("Complete User");
        user.setGender("FEMALE");
        user.setPhone("0123456789");
        user.setAddress("456 Complete St, City");
        user.setAvatarUrl("http://example.com/complete-avatar.jpg");
        user.setRoleId(5);
        user.setRoleName("MANAGER");
        user.setStatus(true);
        user.setDeleted(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setCreatedBy(1);
        user.setDeletedBy(null);

        // Verify all fields
        assertEquals(100, user.getId());
        assertEquals("complete@example.com", user.getEmail());
        assertEquals("$2a$10$hashedPassword", user.getPassword());
        assertEquals("Complete User", user.getFullName());
        assertEquals("FEMALE", user.getGender());
        assertEquals("0123456789", user.getPhone());
        assertEquals("456 Complete St, City", user.getAddress());
        assertEquals("http://example.com/complete-avatar.jpg", user.getAvatarUrl());
        assertEquals(5, user.getRoleId());
        assertEquals("MANAGER", user.getRoleName());
        assertTrue(user.isStatus());
        assertFalse(user.isDeleted());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(1, user.getCreatedBy());
        assertNull(user.getDeletedBy());
    }
}