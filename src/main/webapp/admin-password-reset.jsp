<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/26/2026
  Time: 10:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.swp391.group6.model.PasswordResetRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    //kiem tra quyen admin
    com.swp391.group6.model.User currentUser = (com.swp391.group6.model.User) session.getAttribute("user");
    if (currentUser == null || currentUser.getRoleId() == null || currentUser.getRoleId() != 1) {
        response.sendRedirect("/login");
        return;
    }

    @SuppressWarnings("unchecked")
    List<PasswordResetRequest> requests = (List<PasswordResetRequest>) request.getAttribute("requests");

    String successMsg = request.getParameter("success");
    String errorMsg = request.getParameter("error");
    String warningMsg = request.getParameter("warning");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Yêu Cầu Đặt Lại Mặt Khẩu - admin</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        table {
            border-collapse: collapse;
            width: 100%
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #dc3545;
            color: white;
        }

        .approve-bth {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border: none;
            cursor: pointer;
        }

        .reject-bth {
            background-color: #dc3545;
            color: white;
            padding: 5px 10px;
            border: none;
            cursor: pointer;
        }

        .success {
            color: red;
            font-weight: bold;
        }

        .error {
            color: red;
            font-weight: bold;
        }

        .warning {
            color: orange;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h1>Quản Lý Yêu Cầu Đặt Lại Mật Khẩu</h1>
<p> Xin Chao, <strong><%= currentUser.getFullName()%> (Admin) </strong> | <a href="${pageContext.request.contextPath}/home.jsp">Về trang chủ</a></p>
<%--Thông Báo --%>
<% if (successMsg != null) { %>
<% if ("approved".equals(successMsg)) { %>
<p class="success">Đã chấp nhận yêu cầu và gửi email thành công!</p>
<% } else if ("rejected".equals(successMsg)) { %>
<p class="success">Đã từ chối yêu cầu!</p>
<% } %>
<% } %>

<% if (errorMsg != null) { %>
<p class="error"Lỗi:<%= errorMsg %></p>
<% } %>

<% if (warningMsg != null) { %>
<% if ("email_failed".equals(warningMsg)) { %>
<p class="warning">Đã cập nhật mật khẩu nhưng gửi email thất bại. Kiểm tra EmailUtil!</p>
<% } %>
<% } %>

<h2>Danh Sách Yêu Cầu Đang Chờ Duyệt (<%= requests != null ? requests.size() : 0 %>)</h2>
<% if (requests == null || requests.isEmpty()) { %>
<p>Không có yêu cầu nào đang chờ xử lý.</p>
<% } else { %>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Email</th>
        <th>Tên User</th>
        <th>Ngày Yêu Cầu</th>
        <th>Hành Động</th>
    </tr>
    </thead>
    <tbody>
    <%
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (PasswordResetRequest req : requests) {
    %>
    <tr>
        <td><%= req.getId() %>
        </td>
        <td><%= req.getEmail() %>
        </td>
        <td><%= req.getUserFullName() %>
        </td>
        <td><%= req.getRequestDate().format(formatter) %>
        </td>
        <td>
            <form action="password-reset" method="post" style="display: inline;">
                <input type="hidden" name="requestId" value="<%= req.getId() %>"/>
                <input type="hidden" name="action" value="approve"/>
                <button type="submit" class="approve-btn"
                        onclick="return confirm('Chấp nhận yêu cầu cho <%= req.getEmail() %>?')">
                    Chấp Nhận
                </button>
            </form>

            <form action="password-reset" method="post" style="display: inline;">
                <input type="hidden" name="requestId" value="<%= req.getId() %>"/>
                <input type="hidden" name="action" value="reject"/>
                <input type="text" name="reason" placeholder="Lý do từ chối (optional)" size="20"/>
                <button type="submit" class="reject-btn"
                        onclick="return confirm('Từ chối yêu cầu cho <%= req.getEmail() %>?')">
                    Từ Chối
                </button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
</body>
</html>
