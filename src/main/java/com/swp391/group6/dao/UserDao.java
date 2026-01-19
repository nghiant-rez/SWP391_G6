/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.group6.dao;

import com.swp391.group6.model.User;
/**
 *
 * @author Admin
 */
public interface UserDao {
    
    // tìm user theo email
    User findByEmail(String email);
    
}
