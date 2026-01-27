package com.swp391.group6.controller;

import com.swp391.group6.util.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DemoServlet", urlPatterns = {"/demo"})
public class DemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String statusMessage;
        String cssClass;
        Connection conn = DBContext.getConnection();
        if (conn != null) {
            statusMessage = "SUCCESS: Database Connected via XAMPP MySQL!";
            cssClass = "success";
        } else {
            statusMessage = "ERROR: Could not connect to Database. Check Console Logs.";
            cssClass = "error";
        }

        request.setAttribute("message", statusMessage);
        request.setAttribute("statusClass", cssClass);
        request.getRequestDispatcher("demo.jsp").forward(request, response);
    }
}