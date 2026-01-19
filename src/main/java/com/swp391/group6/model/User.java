/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.group6.model;

/**
 *
 * @author Admin
 */
public class User {
    private int id;
    private String email;
    private String fullname;
    private String password;

    public User() {
    }

    public User(int id, String email, String fullname, String password) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString(){
        return "User{" +"Id=" + id 
                + ", email=" + email + '\''
                + ", fullname=" + fullname + '\'' 
                + '}';
    }
}
