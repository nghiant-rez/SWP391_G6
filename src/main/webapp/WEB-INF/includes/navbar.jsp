<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.user}"/>

<div style="background:#2c3e50;padding:10px 14px;display:flex;
            gap:14px;align-items:center;flex-wrap:wrap">
    <a href="${ctx}/home"
       style="color:#fff;text-decoration:none;font-weight:600">
        Home
    </a>

    <c:choose>
        <%-- Admin: Quan ly nguoi dung, Quan ly vai tro --%>
        <c:when test="${user.roleId == 1}">
            <a href="${ctx}/admin/users"
               style="color:#fff;text-decoration:none">
                User Management
            </a>
            <a href="${ctx}/admin/roles"
               style="color:#fff;text-decoration:none">
                Role Management
            </a>
            <a href="${ctx}/admin/password-reset"
               style="color:#fff;text-decoration:none">
                Password Resets
            </a>
        </c:when>

        <%-- Manager: Quan ly cong viec, Yeu cau dich vu --%>
        <c:when test="${user.roleId == 2}">
            <a href="${ctx}/management/tasks"
               style="color:#fff;text-decoration:none">
                Task Management
            </a>
        </c:when>

        <%-- Staff: Cong viec cua toi, Yeu cau dich vu --%>
        <c:when test="${user.roleId == 3}">
            <a href="${ctx}/management/tasks"
               style="color:#fff;text-decoration:none">
                My Tasks
            </a>
        </c:when>

        <%-- Customer --%>
        <c:otherwise>
            <a href="${ctx}/demo"
               style="color:#fff;text-decoration:none">
                Customer Page
            </a>
            <a href="${ctx}/management/service-requests/create"
               style="color:#fff;
                      text-decoration:none">
                Tao yeu cau moi
            </a>
        </c:otherwise>
    </c:choose>

    <div style="margin-left:auto;display:flex;
                gap:12px;align-items:center">
        <span style="color:#fff;opacity:.9">
            ${user.fullName} (${user.roleName})
        </span>
        <a href="${ctx}/change-password"
           style="color:#fff;
                  text-decoration:none">
            Doi mat khau
        </a>
        <a href="${ctx}/logout"
           onclick="return confirm('Xác nhận đăng xuất tài khoản?')"
           style="color:#fff;text-decoration:none">
            Logout
        </a>
    </div>
</div>
