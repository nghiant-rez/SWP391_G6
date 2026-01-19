/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.group6.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Admin
 */
public class PasswordUtil {
    
    // mã hóa password
    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    // kiểm tra password có khớp với hash không.
    public static boolean checkPassword(String plainPassword, String hashPassword){
        try{
            return BCrypt.checkpw(plainPassword, hashPassword);
        }catch(IllegalArgumentException e){
            System.out.println("Lỗi: chuỗi hash không đúng định dạnh BCrypt!");
            return false;
        }
    }
    
    //Test main
    public static void main(String[] args) {
        String plainPassword = "123456";
        String hashed = hashPassword(plainPassword);
        
        System.out.println("Plain Password: " + plainPassword);
        System.out.println("Hashed Password : " + hashed);
        System.out.println("Password Match: " + checkPassword(plainPassword, hashed));
    }
}
