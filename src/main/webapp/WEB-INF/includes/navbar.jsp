<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="roleId" value="${sessionScope.role}" />

<div style="background:#2c3e50;padding:10px 14px;display:flex;gap:14px;align-items:center;flex-wrap:wrap">
    <a href="${ctx}/home" style="color:#fff;text-decoration:none;font-weight:600">Home</a>

    <c:choose>
        <c:when test="${roleId == 1}">
            <a href="${ctx}/admin/users" style="color:#fff;text-decoration:none">User Management</a>
            <a href="${ctx}/admin/roles" style="color:#fff;text-decoration:none">Role Management</a>
        </c:when>

        <c:when test="${roleId == 2}">
            <a href="${ctx}/demo" style="color:#fff;text-decoration:none">Manager Page</a>
        </c:when>

        <c:when test="${roleId == 3}">
            <a href="${ctx}/demo" style="color:#fff;text-decoration:none">Staff Page</a>
        </c:when>

        <c:otherwise>
            <a href="${ctx}/demo" style="color:#fff;text-decoration:none">Customer Page</a>
        </c:otherwise>
    </c:choose>

    <div style="margin-left:auto;display:flex;gap:12px;align-items:center">
        <span style="color:#fff;opacity:.9">
            ${sessionScope.fullName}
        </span>
        <a href="${ctx}/logout"
           onclick="return confirm('Bạn có muốn đăng xuất không?')"
           style="color:#fff;text-decoration:none">
            Logout
        </a>
    </div>
</div>
