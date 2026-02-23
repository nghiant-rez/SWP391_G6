/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.group6.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

/**
 *
 * @author Admin
 */
public class PasswordUtil {

    private static final String CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 6;

    // mã hóa pass
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // kiểm tra password có khớp với hash không.
    public static boolean checkPassword(String plainPassword, String hashPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashPassword);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: chuỗi hash không đúng định dạnh BCrypt!");
            return false;
        }
    }

    //Generate random password
    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTER.length());
            password.append(CHARACTER.charAt(index));
        }

        return password.toString();
    }

    //Generate password ngẫu nhiên với độ dài tùy chỉnh
    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTER.length());
            password.append(CHARACTER.charAt(index));
        }
        return password.toString();
    }

    //Test main
    public static void main(String[] args) {
        String plainPassword = "123456";
        String hashed = hashPassword(plainPassword);

        System.out.println("Plain Password: " + plainPassword);
        System.out.println("Hashed Password : " + hashed);
        System.out.println("Password Match: " + checkPassword(plainPassword, hashed));

        System.out.println();
        for (int i = 0; i < 5; i++) {
            String randomPassword = generateRandomPassword();
            System.out.println("Random Password: " + (i+1) + "." + randomPassword);
        }
        System.out.println();

        //Test 3: Generate với độ dài tùy chỉnh
        System.out.println("3. Random Passwords (12 ký tự):");
        for (int i = 0; i < 3; i++) {
            String randomPassword = generateRandomPassword(12);
            System.out.println("Random Password: " + (i+1) + "." + randomPassword);
        }
    }
}
