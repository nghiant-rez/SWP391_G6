<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/26/2026
  Time: 8:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forgot Password - SWP391</title>
</head>
<body>
<h2>Quên mật khẩu</h2>
<%-- Hiên thi lỗi--%>
<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) {%>
<p style="color: red"><%= error %>
</p>
<% } %>

<%-- Hiên thi thông báo thành công--%>
<% String success = (String) request.getAttribute("success"); %>
<% if (error != null) {%>
<p style="color: green"><%= success %>
</p>
<% } %>

<p>Nhập email của bạn để yêu cầu đặt lại mật khẩu. Admin sẽ xét duyệt yêu cầu của bạn.</p>

<form action="forgot-password" method="post">
    <table>
        <tr>
            <td>Email:</td>
            <td><input type="email" name="email" required placeholder="example@gmail.com"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit">Gửi Yêu Cầu</button>
                <a href="login">
                    <button type="button">Quay lại Login</button>
                </a>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
