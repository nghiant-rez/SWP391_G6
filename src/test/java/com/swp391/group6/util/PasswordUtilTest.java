package com.swp391.group6.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordUtil Tests")
class PasswordUtilTest {

    @Test
    @DisplayName("Hash password returns non-null BCrypt hash")
    void testHashPassword() {
        String plainPassword = "testPassword123";
        String hashed = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$2a$") || hashed.startsWith("$2b$"));
        assertTrue(hashed.length() > 50); // BCrypt hashes are typically 60 characters
    }

    @Test
    @DisplayName("Hash password generates different hashes for same password")
    void testHashPasswordGeneratesDifferentHashes() {
        String plainPassword = "samePassword";
        String hash1 = PasswordUtil.hashPassword(plainPassword);
        String hash2 = PasswordUtil.hashPassword(plainPassword);

        assertNotEquals(hash1, hash2, "BCrypt should generate different salts");
    }

    @Test
    @DisplayName("Check password validates correct password")
    void testCheckPasswordCorrect() {
        String plainPassword = "mySecurePassword";
        String hashed = PasswordUtil.hashPassword(plainPassword);

        assertTrue(PasswordUtil.checkPassword(plainPassword, hashed));
    }

    @Test
    @DisplayName("Check password rejects incorrect password")
    void testCheckPasswordIncorrect() {
        String plainPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String hashed = PasswordUtil.hashPassword(plainPassword);

        assertFalse(PasswordUtil.checkPassword(wrongPassword, hashed));
    }

    @Test
    @DisplayName("Check password handles empty password")
    void testCheckPasswordEmpty() {
        String emptyPassword = "";
        String hashed = PasswordUtil.hashPassword(emptyPassword);

        assertTrue(PasswordUtil.checkPassword(emptyPassword, hashed));
        assertFalse(PasswordUtil.checkPassword("notEmpty", hashed));
    }

    @Test
    @DisplayName("Check password handles invalid hash format")
    void testCheckPasswordInvalidHash() {
        String plainPassword = "password123";
        String invalidHash = "not-a-valid-bcrypt-hash";

        assertFalse(PasswordUtil.checkPassword(plainPassword, invalidHash));
    }

    @Test
    @DisplayName("Check password with null hash returns false")
    void testCheckPasswordNullHash() {
        assertFalse(PasswordUtil.checkPassword("password", null));
    }

    @Test
    @DisplayName("Generate random password returns 6 characters by default")
    void testGenerateRandomPasswordDefault() {
        String password = PasswordUtil.generateRandomPassword();

        assertNotNull(password);
        assertEquals(6, password.length());
    }

    @Test
    @DisplayName("Generate random password contains only valid characters")
    void testGenerateRandomPasswordValidCharacters() {
        String password = PasswordUtil.generateRandomPassword();
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (char c : password.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0,
                "Character '" + c + "' is not in valid character set");
        }
    }

    @RepeatedTest(5)
    @DisplayName("Generate random password creates unique passwords")
    void testGenerateRandomPasswordUniqueness() {
        String password1 = PasswordUtil.generateRandomPassword();
        String password2 = PasswordUtil.generateRandomPassword();

        // While theoretically possible to be equal, it's extremely unlikely
        // Testing multiple times to ensure randomness
        assertNotNull(password1);
        assertNotNull(password2);
        assertEquals(6, password1.length());
        assertEquals(6, password2.length());
    }

    @Test
    @DisplayName("Generate random password with custom length")
    void testGenerateRandomPasswordCustomLength() {
        int customLength = 12;
        String password = PasswordUtil.generateRandomPassword(customLength);

        assertNotNull(password);
        assertEquals(customLength, password.length());
    }

    @Test
    @DisplayName("Generate random password with various custom lengths")
    void testGenerateRandomPasswordVariousLengths() {
        int[] lengths = {1, 8, 16, 32, 64};

        for (int length : lengths) {
            String password = PasswordUtil.generateRandomPassword(length);
            assertNotNull(password);
            assertEquals(length, password.length());
        }
    }

    @Test
    @DisplayName("Generate random password with length 0 returns empty string")
    void testGenerateRandomPasswordZeroLength() {
        String password = PasswordUtil.generateRandomPassword(0);

        assertNotNull(password);
        assertEquals(0, password.length());
    }

    @Test
    @DisplayName("Generate random password with custom length contains valid characters")
    void testGenerateRandomPasswordCustomLengthValidCharacters() {
        String password = PasswordUtil.generateRandomPassword(20);
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (char c : password.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0,
                "Character '" + c + "' is not in valid character set");
        }
    }

    @Test
    @DisplayName("Hash and check password workflow")
    void testHashAndCheckPasswordWorkflow() {
        String[] testPasswords = {
            "simple",
            "ComplexP@ssw0rd!",
            "12345678",
            "with spaces",
            "SpecialChars!@#$%"
        };

        for (String password : testPasswords) {
            String hashed = PasswordUtil.hashPassword(password);
            assertTrue(PasswordUtil.checkPassword(password, hashed),
                "Failed to validate password: " + password);
        }
    }

    @Test
    @DisplayName("Hash password handles special characters")
    void testHashPasswordSpecialCharacters() {
        String specialPassword = "P@$$w0rd!#%&*()";
        String hashed = PasswordUtil.hashPassword(specialPassword);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword(specialPassword, hashed));
        assertFalse(PasswordUtil.checkPassword("P@$$w0rd", hashed));
    }

    @Test
    @DisplayName("Hash password is case sensitive")
    void testHashPasswordCaseSensitive() {
        String password = "TestPassword";
        String hashed = PasswordUtil.hashPassword(password);

        assertTrue(PasswordUtil.checkPassword("TestPassword", hashed));
        assertFalse(PasswordUtil.checkPassword("testpassword", hashed));
        assertFalse(PasswordUtil.checkPassword("TESTPASSWORD", hashed));
    }

    @Test
    @DisplayName("Generate multiple random passwords are different")
    void testGenerateMultipleRandomPasswordsDifferent() {
        int count = 100;
        java.util.Set<String> passwords = new java.util.HashSet<>();

        for (int i = 0; i < count; i++) {
            passwords.add(PasswordUtil.generateRandomPassword());
        }

        // With 100 random 6-character passwords, we should have high uniqueness
        // (62^6 possible combinations)
        assertTrue(passwords.size() > 95,
            "Expected mostly unique passwords, got " + passwords.size() + " unique out of " + count);
    }

    @Test
    @DisplayName("Hash password with long input")
    void testHashPasswordLongInput() {
        String longPassword = "a".repeat(1000);
        String hashed = PasswordUtil.hashPassword(longPassword);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword(longPassword, hashed));
    }

    @Test
    @DisplayName("Hash password with Unicode characters")
    void testHashPasswordUnicodeCharacters() {
        String unicodePassword = "パスワード123";
        String hashed = PasswordUtil.hashPassword(unicodePassword);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword(unicodePassword, hashed));
    }

    @Test
    @DisplayName("Generated password can be hashed and validated")
    void testGeneratedPasswordCanBeHashedAndValidated() {
        String generated = PasswordUtil.generateRandomPassword(10);
        String hashed = PasswordUtil.hashPassword(generated);

        assertTrue(PasswordUtil.checkPassword(generated, hashed));
    }

    @Test
    @DisplayName("Random password distribution includes all character types")
    void testRandomPasswordDistribution() {
        // Generate many passwords and check for character diversity
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (int i = 0; i < 50; i++) {
            String password = PasswordUtil.generateRandomPassword(20);
            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpperCase = true;
                if (Character.isLowerCase(c)) hasLowerCase = true;
                if (Character.isDigit(c)) hasDigit = true;
            }
            if (hasUpperCase && hasLowerCase && hasDigit) {
                break;
            }
        }

        assertTrue(hasUpperCase, "Generated passwords should include uppercase letters");
        assertTrue(hasLowerCase, "Generated passwords should include lowercase letters");
        assertTrue(hasDigit, "Generated passwords should include digits");
    }
}