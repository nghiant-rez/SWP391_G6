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

        <%-- Nếu là Admin → Hiển thị link quản lý password reset --%>
        <% if (user.getRoleId() != null && user.getRoleId() == 1) { %>
        <p><a href="admin/password-reset" style="background: #667eea; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">🔐 Quản Lý Yêu Cầu Đặt Lại Mật Khẩu</a></p>
        <% } %>
        <a href="logout" onclick="return confirm('bạn có muốn đang xuất không?')">Logout</a>
    </body>
</html>
