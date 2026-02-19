<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx"
    value="${pageContext.request.contextPath}"/>
<c:set var="user"
    value="${sessionScope.user}"/>

<div style="background:#2c3e50;padding:10px 14px;
            display:flex;gap:14px;
            align-items:center;flex-wrap:wrap">
    <a href="${ctx}/home"
       style="color:#fff;text-decoration:none;
              font-weight:600">
        Trang chu
    </a>

    <c:choose>
        <%-- Admin: Quan ly nguoi dung, Quan ly vai tro --%>
        <c:when test="${user.roleId == 1}">
            <a href="${ctx}/admin/users"
               style="color:#fff;
                      text-decoration:none">
                Quan ly nguoi dung
            </a>
            <a href="${ctx}/admin/roles"
               style="color:#fff;
                      text-decoration:none">
                Quan ly vai tro
            </a>
        </c:when>

        <%-- Manager: Quan ly cong viec, Yeu cau dich vu --%>
        <c:when test="${user.roleId == 2}">
            <a href="${ctx}/management/tasks"
               style="color:#fff;
                      text-decoration:none">
                Quan ly cong viec
            </a>
            <a href="${ctx}/management/service-requests"
               style="color:#fff;
                      text-decoration:none">
                Yeu cau dich vu
            </a>
        </c:when>

        <%-- Staff: Cong viec cua toi, Yeu cau dich vu --%>
        <c:when test="${user.roleId == 3}">
            <a href="${ctx}/management/tasks"
               style="color:#fff;
                      text-decoration:none">
                Cong viec cua toi
            </a>
            <a href="${ctx}/management/service-requests"
               style="color:#fff;
                      text-decoration:none">
                Yeu cau dich vu
            </a>
        </c:when>

        <%-- Customer: Yeu cau dich vu, Tao yeu cau moi --%>
        <c:when test="${user.roleId == 4}">
            <a href="${ctx}/my-service-requests"
               style="color:#fff;
                      text-decoration:none">
                Yeu cau dich vu
            </a>
            <a href="${ctx}/management/service-requests/create"
               style="color:#fff;
                      text-decoration:none">
                Tao yeu cau moi
            </a>
        </c:when>
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
           onclick="return confirm('Ban co muon dang xuat khong?')"
           style="color:#fff;
                  text-decoration:none">
            Dang xuat
        </a>
    </div>
</div>
