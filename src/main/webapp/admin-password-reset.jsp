<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/26/2026
  Time: 10:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.swp391.group6.model.PasswordResetRequest"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%
    // Kiểm tra quyền admin
    com.swp391.group6.model.User currentUser = (com.swp391.group6.model.User) session.getAttribute("user");
    if (currentUser == null || currentUser.getRoleId() == null || currentUser.getRoleId() != 1) {
        response.sendRedirect("login");
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
    <meta charset="UTF-8">
    <title>Quản Lý Yêu Cầu Đặt Lại Mật Khẩu - Admin</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #667eea; color: white; }
        .approve-btn { background-color: #28a745; color: white; padding: 5px 10px; border: none; cursor: pointer; }
        .reject-btn { background-color: #dc3545; color: white; padding: 5px 10px; border: none; cursor: pointer; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
        .warning { color: orange; font-weight: bold; }
    </style>
    <script>
        function confirmReject(form, email) {
            var reason = form.querySelector('input[name="reason"]').value.trim();
            if (!reason) {
                alert('Vui lòng nhập lý do từ chối!');
                return false;
            }
            return confirm('Từ chối yêu cầu cho ' + email + '?\n\nLý do: ' + reason + '\n\nEmail thông báo sẽ được gửi cho user.');
        }
    </script>
</head>
<body>
<h1>Quản Lý Yêu Cầu Đặt Lại Mật Khẩu</h1>

<p>Xin chào, <strong><%= currentUser.getFullName() %></strong> (Admin) | <a href="home.jsp">Về trang chủ</a></p>

<%-- Thông báo --%>
<% if (successMsg != null) { %>
<% if ("approved".equals(successMsg)) { %>
<p class="success">✅ Đã chấp nhận yêu cầu và gửi email password mới thành công!</p>
<% } else if ("rejected".equals(successMsg)) { %>
<p class="success">✅ Đã từ chối yêu cầu và gửi email thông báo cho user!</p>
<% } %>
<% } %>

<% if (errorMsg != null) { %>
<p class="error">❌ Lỗi: <%= errorMsg %></p>
<% } %>

<% if (warningMsg != null) { %>
<% if ("email_failed".equals(warningMsg)) { %>
<p class="warning">⚠️ Đã cập nhật request nhưng gửi email thất bại. Kiểm tra EmailUtil!</p>
<% } %>
<% } %>

<hr/>

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
        <td><%= req.getId() %></td>
        <td><%= req.getEmail() %></td>
        <td><%= req.getUserFullName() %></td>
        <td><%= req.getRequestDate().format(formatter) %></td>
        <td>
            <form action="password-reset" method="post" style="display: inline;">
                <input type="hidden" name="requestId" value="<%= req.getId() %>" />
                <input type="hidden" name="action" value="approve" />
                <button type="submit" class="approve-btn"
                        onclick="return confirm('Chấp nhận yêu cầu cho <%= req.getEmail() %>?\n\nPassword mới sẽ được gửi qua email.')">
                    ✅ Chấp Nhận
                </button>
            </form>

            <form action="password-reset" method="post" style="display: inline;"
                  onsubmit="return confirmReject(this, '<%= req.getEmail() %>')">
                <input type="hidden" name="requestId" value="<%= req.getId() %>" />
                <input type="hidden" name="action" value="reject" />
                <input type="text" name="reason" placeholder="Lý do từ chối (bắt buộc)"
                       size="30" required
                       style="padding: 5px; border: 1px solid #dc3545;" />
                <button type="submit" class="reject-btn">
                    ❌ Từ Chối
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