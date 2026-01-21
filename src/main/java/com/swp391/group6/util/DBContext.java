package com.swp391.group6.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    private static final String USER = "root";
    private static final String PASS = "123456";
    private static final String URL = "jdbc:mysql://localhost:3306/swp391_demo";

    public static Connection getConnection() {
        Logger logger = Logger.getLogger(DBContext.class.getName());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL Driver not found! Check pom.xml", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Connection failed! Check XAMPP/User/Pass", e);
        }
        return null;
    }


    public static void main(String[] args) {
        System.out.println("Testing connection...");
        if (getConnection() != null) {
            System.out.println("✅ Connection Successful!");
        } else {
            System.out.println("❌ Connection Failed.");
        }
    }
}