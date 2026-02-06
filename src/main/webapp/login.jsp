<%--&lt;%&ndash; --%>
<%--    Document   : login.jsp--%>
<%--    Created on : Jan 16, 2026, 1:04:30 PM--%>
<%--    Author     : Admin--%>
<%--&ndash;%&gt;--%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
<%--    <title>Login</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h1>Login</h1>--%>

<%--&lt;%&ndash; Hiển thị lỗi &ndash;%&gt;--%>
<%--<% String error = (String) request.getAttribute("error");%>--%>
<%--<% if (error != null) { %>--%>
<%--<p style="color: red;"><%= error %>--%>
<%--</p>--%>
<%--<% } %>--%>

<%--&lt;%&ndash; Hiển thị thông báo logout thành công &ndash;%&gt;--%>
<%--<% String message = request.getParameter("message"); %>--%>
<%--<% if ("logout_success".equals(message)) { %>--%>
<%--<p style="color: green;">Đăng xuất thành công!</p>--%>
<%--<% } %>--%>

<%--<form action="login" method="post">--%>
<%--    <table>--%>
<%--        <tr>--%>
<%--            <td>Email:</td>--%>
<%--            <td><input type="email" name="email" required autofocus/></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td>Password:</td>--%>
<%--            <td><input type="password" name="password" required/></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td colspan="2" style="text-align: center;">--%>
<%--                <a href="forgot-password">Quên mật khẩu?</a>--%>
<%--            </td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td colspan="2">--%>
<%--                <button type="submit">Login</button>--%>
<%--            </td>--%>
<%--        </tr>--%>
<%--    </table>--%>
<%--</form>--%>
<%--</body>--%>
<%--</html>--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Đồng bộ màu với màn User Management -->
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        primary: '#2c3e50',   // màu header xanh đậm
                        primaryLight: '#34495e'
                    }
                }
            }
        }
    </script>
</head>
<body class="min-h-screen bg-gray-100 flex items-center justify-center">

<div class="w-full max-w-md bg-white rounded-lg shadow-lg overflow-hidden">

    <!-- Header -->
    <div class="bg-primary px-6 py-4">
        <h1 class="text-white text-xl font-semibold text-center">
            System Login
        </h1>
    </div>

    <!-- Body -->
    <div class="p-6 space-y-4">

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
        <div class="bg-red-100 text-red-700 px-4 py-2 rounded text-sm">
            <%= error %>
        </div>
        <% } %>

        <% String message = request.getParameter("message"); %>
        <% if ("logout_success".equals(message)) { %>
        <div class="bg-green-100 text-green-700 px-4 py-2 rounded text-sm">
            Đăng xuất thành công
        </div>
        <% } %>

        <form action="login" method="post" class="space-y-4">

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Email
                </label>
                <input type="email" name="email" required autofocus
                       class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"/>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Password
                </label>
                <input type="password" name="password" required
                       class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-primary"/>
            </div>

            <div class="text-right">
                <a href="forgot-password"
                   class="text-sm text-primary hover:underline">
                    Quên mật khẩu?
                </a>
            </div>

            <button type="submit"
                    class="w-full bg-primary text-white py-2 rounded hover:bg-primaryLight transition">
                Login
            </button>

        </form>
    </div>
</div>

</body>
</html>
