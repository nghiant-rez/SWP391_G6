<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<div class="box">
    <h1>My Profile</h1>

    <c:if test="${not empty profileUser.avatarUrl}">
        <img src="${profileUser.avatarUrl}" alt="Avatar"
             style="width:120px;height:120px;border-radius:50%;object-fit:cover;margin:10px 0;">
    </c:if>

    <table style="width:100%;text-align:left;margin-top:10px">
        <tr>
            <th style="width:160px;">Full Name</th>
            <td><c:out value="${profileUser.fullName}" /></td>
        </tr>

        <tr>
            <th>Email</th>
            <td><c:out value="${profileUser.email}" /></td>
        </tr>

        <tr>
            <th>Phone</th>
            <td><c:out value="${empty profileUser.phone ? 'Chưa cập nhật' : profileUser.phone}" /></td>
        </tr>

        <tr>
            <th>Gender</th>
            <td><c:out value="${empty profileUser.gender ? 'Chưa cập nhật' : profileUser.gender}" /></td>
        </tr>

        <tr>
            <th>Address</th>
            <td><c:out value="${empty profileUser.address ? 'Chưa cập nhật' : profileUser.address}" /></td>
        </tr>

        <tr>
            <th>Role</th>
            <td><c:out value="${empty roleName ? profileUser.roleId : roleName}" /></td>
        </tr>
    </table>

    <div style="margin-top:18px;display:flex;gap:12px;justify-content:center">
        <a href="${pageContext.request.contextPath}/home">Back to Home</a>
        <a href="${pageContext.request.contextPath}/logout"
           onclick="return confirm('Bạn có muốn đăng xuất không?')">Logout</a>
    </div>
</div>

</body>
</html>
