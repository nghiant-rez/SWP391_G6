<%@ page import="com.swp391.group6.model.User" %><%--
    Document   : home
    Created on : Jan 16, 2026, 1:17:37 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User user = (User) session.getAttribute("user");

    //kiem tra login chua
    if(user == null){
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h2>Welcome</h2>
        <p>FullName: <%= user.getFullName() %></p>
        <p>Email: <%= user.getEmail() %></p>

        <%-- Admin: Password Reset Management --%>
        <% if (user.getRoleId() != null && user.getRoleId() == 1) { %>
        <p>
            <a href="admin/users" 
               style="background: #3498db; color: white; padding: 10px 20px; 
                      text-decoration: none; border-radius: 5px; 
                      margin-right: 10px;">
                User Management
            </a>
            <a href="admin/roles" 
               style="background: #9b59b6; color: white; padding: 10px 20px; 
                      text-decoration: none; border-radius: 5px; 
                      margin-right: 10px;">
                Role Management
            </a>
            <a href="admin/password-reset" 
               style="background: #667eea; color: white; padding: 10px 20px; 
                      text-decoration: none; border-radius: 5px;">
                Password Reset Requests
            </a>
        </p>
        <% } %>
        
        <%-- Manager: Task Management --%>
        <% if (user.getRoleId() != null && user.getRoleId() == 2) { %>
        <p>
            <a href="management/tasks" 
               style="background: #27ae60; color: white; padding: 10px 20px; 
                      text-decoration: none; border-radius: 5px; 
                      margin-right: 10px;">
                Task Management
            </a>
        </p>
        <% } %>
        
        <%-- Staff: Task Management (view/update only) --%>
        <% if (user.getRoleId() != null && user.getRoleId() == 3) { %>
        <p>
            <a href="management/tasks" 
               style="background: #27ae60; color: white; padding: 10px 20px; 
                      text-decoration: none; border-radius: 5px;">
                My Tasks
            </a>
        </p>
        <% } %>
        
        <br/>
        <a href="logout" 
           onclick="return confirm('Ban co muon dang xuat khong?')">
            Logout
        </a>
    </body>
</html>
