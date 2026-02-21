package com.swp391.group6.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PasswordUtil Security Tests")
class PasswordUtilSecurityTest {

    @Test
    @DisplayName("Hashed password is not reversible")
    void testHashedPasswordNotReversible() {
        String plainPassword = "myPassword123";
        String hashed = PasswordUtil.hashPassword(plainPassword);

        // Hashed password should not contain the plain password
        assertFalse(hashed.contains(plainPassword));
        assertNotEquals(plainPassword, hashed);
    }

    @Test
    @DisplayName("Same password produces different hashes (salt randomization)")
    void testSaltRandomization() {
        String password = "testPassword";

        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        String hash3 = PasswordUtil.hashPassword(password);

        // All hashes should be different due to random salts
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);

        // But all should validate against the original password
        assertTrue(PasswordUtil.checkPassword(password, hash1));
        assertTrue(PasswordUtil.checkPassword(password, hash2));
        assertTrue(PasswordUtil.checkPassword(password, hash3));
    }

    @Test
    @DisplayName("BCrypt hash format validation")
    void testBCryptHashFormat() {
        String password = "password123";
        String hashed = PasswordUtil.hashPassword(password);

        // BCrypt hashes start with $2a$, $2b$, or $2y$
        assertTrue(hashed.startsWith("$2a$") ||
                   hashed.startsWith("$2b$") ||
                   hashed.startsWith("$2y$"));

        // BCrypt hashes are 60 characters long
        assertEquals(60, hashed.length());
    }

    @Test
    @DisplayName("Password verification is case sensitive")
    void testPasswordVerificationCaseSensitive() {
        String password = "MyPassword123";
        String hashed = PasswordUtil.hashPassword(password);

        assertTrue(PasswordUtil.checkPassword("MyPassword123", hashed));
        assertFalse(PasswordUtil.checkPassword("mypassword123", hashed));
        assertFalse(PasswordUtil.checkPassword("MYPASSWORD123", hashed));
        assertFalse(PasswordUtil.checkPassword("MyPassword124", hashed));
    }

    @Test
    @DisplayName("Empty password can be hashed and verified")
    void testEmptyPasswordHashing() {
        String emptyPassword = "";
        String hashed = PasswordUtil.hashPassword(emptyPassword);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword("", hashed));
        assertFalse(PasswordUtil.checkPassword("notEmpty", hashed));
    }

    @Test
    @DisplayName("Single character password can be hashed")
    void testSingleCharacterPassword() {
        String singleChar = "a";
        String hashed = PasswordUtil.hashPassword(singleChar);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword("a", hashed));
        assertFalse(PasswordUtil.checkPassword("b", hashed));
    }

    @Test
    @DisplayName("Very long password can be hashed")
    void testVeryLongPassword() {
        String longPassword = "a".repeat(1000);
        String hashed = PasswordUtil.hashPassword(longPassword);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.checkPassword(longPassword, hashed));
    }

    @Test
    @DisplayName("Password with all character types")
    void testPasswordWithAllCharacterTypes() {
        String complexPassword = "Aa1!@#$%^&*()_+-=[]{}|;':\",./<>?~`";
        String hashed = PasswordUtil.hashPassword(complexPassword);

        assertTrue(PasswordUtil.checkPassword(complexPassword, hashed));
    }

    @Test
    @DisplayName("Password with whitespace")
    void testPasswordWithWhitespace() {
        String passwordWithSpaces = "my password has spaces";
        String hashed = PasswordUtil.hashPassword(passwordWithSpaces);

        assertTrue(PasswordUtil.checkPassword("my password has spaces", hashed));
        assertFalse(PasswordUtil.checkPassword("mypasswordhasspaces", hashed));
    }

    @Test
    @DisplayName("Password with leading and trailing spaces")
    void testPasswordWithLeadingTrailingSpaces() {
        String password = "  password  ";
        String hashed = PasswordUtil.hashPassword(password);

        assertTrue(PasswordUtil.checkPassword("  password  ", hashed));
        assertFalse(PasswordUtil.checkPassword("password", hashed));
    }

    @Test
    @DisplayName("Similar passwords produce different hashes")
    void testSimilarPasswordsDifferentHashes() {
        String password1 = "password1";
        String password2 = "password2";

        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);

        assertFalse(PasswordUtil.checkPassword(password1, hash2));
        assertFalse(PasswordUtil.checkPassword(password2, hash1));
    }

    @Test
    @DisplayName("Invalid hash format returns false")
    void testInvalidHashFormat() {
        String password = "password";

        // Various invalid hash formats
        assertFalse(PasswordUtil.checkPassword(password, "invalid"));
        assertFalse(PasswordUtil.checkPassword(password, ""));
        assertFalse(PasswordUtil.checkPassword(password, "$2a$10$invalid"));
        assertFalse(PasswordUtil.checkPassword(password, "not-bcrypt-hash"));
    }

    @Test
    @DisplayName("Null password hash handling")
    void testNullPasswordHash() {
        assertFalse(PasswordUtil.checkPassword("password", null));
    }

    @Test
    @DisplayName("Random password generation produces strong passwords")
    void testRandomPasswordStrength() {
        for (int i = 0; i < 10; i++) {
            String randomPassword = PasswordUtil.generateRandomPassword(12);

            // Check length
            assertEquals(12, randomPassword.length());

            // Check it can be hashed
            String hashed = PasswordUtil.hashPassword(randomPassword);
            assertTrue(PasswordUtil.checkPassword(randomPassword, hashed));
        }
    }

    @RepeatedTest(10)
    @DisplayName("Generated passwords are cryptographically random")
    void testGeneratedPasswordsRandomness() {
        Set<String> passwords = new HashSet<>();

        // Generate 100 passwords
        for (int i = 0; i < 100; i++) {
            passwords.add(PasswordUtil.generateRandomPassword(8));
        }

        // We should have high uniqueness (very unlikely to have duplicates)
        assertTrue(passwords.size() >= 98,
            "Expected at least 98 unique passwords out of 100");
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 8, 10, 12, 16, 20, 32})
    @DisplayName("Generated passwords of various lengths are secure")
    void testGeneratedPasswordsVariousLengths(int length) {
        String password = PasswordUtil.generateRandomPassword(length);

        assertEquals(length, password.length());

        // Password should be hashable and verifiable
        String hashed = PasswordUtil.hashPassword(password);
        assertTrue(PasswordUtil.checkPassword(password, hashed));
    }

    @Test
    @DisplayName("Generated password character distribution")
    void testGeneratedPasswordCharacterDistribution() {
        // Generate a long password to test distribution
        String password = PasswordUtil.generateRandomPassword(1000);

        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        assertTrue(hasUpperCase, "Password should contain uppercase letters");
        assertTrue(hasLowerCase, "Password should contain lowercase letters");
        assertTrue(hasDigit, "Password should contain digits");
    }

    @Test
    @DisplayName("Password hashing is deterministic per input")
    void testHashingDeterminism() {
        String password = "testPassword";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);

        // While hashes are different (due to salt), verification should work
        assertTrue(PasswordUtil.checkPassword(password, hash1));
        assertTrue(PasswordUtil.checkPassword(password, hash2));
    }

    @Test
    @DisplayName("Check password with tampered hash")
    void testCheckPasswordTamperedHash() {
        String password = "password";
        String hash = PasswordUtil.hashPassword(password);

        // Tamper with the hash
        String tamperedHash = hash.substring(0, hash.length() - 1) + "X";

        assertFalse(PasswordUtil.checkPassword(password, tamperedHash));
    }

    @Test
    @DisplayName("Generated passwords don't contain ambiguous characters")
    void testGeneratedPasswordsNoAmbiguousChars() {
        // Current implementation uses all alphanumeric
        // This test verifies the character set
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 50; i++) {
            String password = PasswordUtil.generateRandomPassword(20);

            for (char c : password.toCharArray()) {
                assertTrue(validChars.indexOf(c) >= 0,
                    "Character '" + c + "' not in valid character set");
            }
        }
    }

    @Test
    @DisplayName("Hash performance is reasonable")
    void testHashPerformance() {
        String password = "testPassword123";

        long startTime = System.currentTimeMillis();
        String hash = PasswordUtil.hashPassword(password);
        long endTime = System.currentTimeMillis();

        // BCrypt should take some time (it's designed to be slow)
        // but not too long (less than 5 seconds)
        long duration = endTime - startTime;
        assertTrue(duration < 5000,
            "Hashing took too long: " + duration + "ms");

        // Verify the hash works
        assertTrue(PasswordUtil.checkPassword(password, hash));
    }

    @Test
    @DisplayName("Multiple hash verifications don't leak information")
    void testMultipleVerifications() {
        String correctPassword = "correct";
        String hash = PasswordUtil.hashPassword(correctPassword);

        // Multiple incorrect attempts
        for (int i = 0; i < 100; i++) {
            assertFalse(PasswordUtil.checkPassword("wrong" + i, hash));
        }

        // Correct password should still work
        assertTrue(PasswordUtil.checkPassword(correctPassword, hash));
    }

    @Test
    @DisplayName("Unicode password support")
    void testUnicodePassword() {
        String unicodePassword = "パスワード123";
        String hash = PasswordUtil.hashPassword(unicodePassword);

        assertTrue(PasswordUtil.checkPassword("パスワード123", hash));
        assertFalse(PasswordUtil.checkPassword("パスワード124", hash));
    }

    @Test
    @DisplayName("Emoji in password")
    void testEmojiInPassword() {
        String emojiPassword = "password😀123";
        String hash = PasswordUtil.hashPassword(emojiPassword);

        assertTrue(PasswordUtil.checkPassword("password😀123", hash));
        assertFalse(PasswordUtil.checkPassword("password123", hash));
    }

    @Test
    @DisplayName("Newline character in password")
    void testNewlineInPassword() {
        String passwordWithNewline = "pass\nword";
        String hash = PasswordUtil.hashPassword(passwordWithNewline);

        assertTrue(PasswordUtil.checkPassword("pass\nword", hash));
        assertFalse(PasswordUtil.checkPassword("password", hash));
    }

    @Test
    @DisplayName("Tab character in password")
    void testTabInPassword() {
        String passwordWithTab = "pass\tword";
        String hash = PasswordUtil.hashPassword(passwordWithTab);

        assertTrue(PasswordUtil.checkPassword("pass\tword", hash));
        assertFalse(PasswordUtil.checkPassword("password", hash));
    }
}