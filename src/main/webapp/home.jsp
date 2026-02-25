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
        <meta http-equiv="Content-Type"
            content="text/html; charset=UTF-8">
        <title>Trang chu</title>
    </head>
    <body>
        <h2>Xin chao</h2>
        <p>Ho ten: <%= user.getFullName() %></p>
        <p>Email: <%= user.getEmail() %></p>

        <%-- Admin: Quan ly nguoi dung, Quan ly vai tro --%>
        <% if (user.getRoleId() != null
                && user.getRoleId() == 1) { %>
        <p>
            <a href="admin/users"
               style="background:#3498db;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Quan ly nguoi dung
            </a>
            <a href="admin/roles"
               style="background:#9b59b6;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Quan ly vai tro
            </a>
            <a href="change-password"
               style="background:#e67e22;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;">
                Doi mat khau
            </a>
            <a href="admin/password-reset"
               style="background:#e74c3c;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;">
                thiết lập lại mật khẩu
            </a>
        </p>
        <% } %>

        <%-- Manager: Quan ly cong viec, Yeu cau dich vu --%>
        <% if (user.getRoleId() != null
                && user.getRoleId() == 2) { %>
        <p>
            <a href="management/tasks"
               style="background:#27ae60;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Quan ly cong viec
            </a>
            <a href="management/service-requests"
               style="background:#e67e22;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Yeu cau dich vu
            </a>
            <a href="management/products"
               style="background:#16a085;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Quan ly san pham
            </a>
            <a href="change-password"
               style="background:#8e44ad;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;">
                Doi mat khau
            </a>
        </p>
        <% } %>

        <%-- Staff: Cong viec cua toi, Yeu cau dich vu --%>
        <% if (user.getRoleId() != null
                && user.getRoleId() == 3) { %>
        <p>
            <a href="management/tasks"
               style="background:#27ae60;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Cong viec cua toi
            </a>
            <a href="management/service-requests"
               style="background:#e67e22;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Yeu cau dich vu
            </a>
            <a href="change-password"
               style="background:#8e44ad;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;">
                Doi mat khau
            </a>
        </p>
        <% } %>

        <%-- Customer: Yeu cau dich vu, Tao yeu cau moi --%>
        <% if (user.getRoleId() != null
                && user.getRoleId() == 4) { %>
        <p>
            <a href="my-service-requests"
               style="background:#e67e22;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Yeu cau dich vu
            </a>
            <a href="management/service-requests/create"
               style="background:#3498db;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;
                      margin-right:10px;">
                Tao yeu cau moi
            </a>
            <a href="change-password"
               style="background:#8e44ad;
                      color:white;
                      padding:10px 20px;
                      text-decoration:none;
                      border-radius:5px;">
                Doi mat khau
            </a>
        </p>
        <% } %>

        <br/>
        <a href="logout"
           onclick="return confirm('Ban co muon dang xuat khong?')">
            Dang xuat
        </a>
    </body>
</html>
