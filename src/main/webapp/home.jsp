<%-- 
    Document   : home
    Created on : Jan 16, 2026, 1:17:37 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    String fullName= (String) session.getAttribute("fullName");
    String email = (String) session.getAttribute("email");
   
    if(fullName == null){
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
        <p>FullName: <%= fullName %></p>
        <p>Email: <%= email %></p>
        <a href="logout">Logout</a>
    </body>
</html>
