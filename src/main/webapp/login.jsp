<%-- 
    Document   : login.jsp
    Created on : Jan 16, 2026, 1:04:30 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
</head>
<body>
<h1>Login</h1>

<%-- Hiển thị lỗi --%>
<% String error = (String) request.getAttribute("error");%>
<% if (error != null) { %>
<p style="color: red;"><%= error %>
</p>
<% } %>

<%-- Hiển thị thông báo logout thành công --%>
<% String message = request.getParameter("message"); %>
<% if ("logout_success".equals(message)) { %>
<p style="color: green;">Đăng xuất thành công!</p>
<% } %>

<form action="login" method="post">
    <table>
        <tr>
            <td>Email:</td>
            <td><input type="email" name="email" required autofocus/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" required/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;">
                <a href="forgot-password">Quên mật khẩu?</a>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit">Login</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
