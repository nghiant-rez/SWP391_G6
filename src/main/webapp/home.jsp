<%-- 
    Document   : home
    Created on : Jan 16, 2026, 1:17:37 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    String email = (String) session.getAttribute("email");

    if (fullName == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>

<jsp:include page="/WEB-INF/includes/navbar.jsp" />

<h2>Welcome</h2>
<p>FullName: <%= fullName %></p>
<p>Email: <%= email %></p>
<p>You can view profile <a href="profile">here</a></p>
</body>
</html>

