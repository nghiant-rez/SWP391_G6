<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách người dùng</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }
        .container { max-width: 1400px; margin: 0 auto; background: white; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { font-size: 28px; font-weight: 600; }
        .toolbar { background: #f8f9fa; padding: 20px 30px; border-bottom: 2px solid #e9ecef; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; }
        .search-box { display: flex; gap: 10px; flex: 1; max-width: 500px; }
        .search-box input { flex: 1; padding: 12px 20px; border: 2px solid #ddd; border-radius: 8px; font-size: 15px; }
        .search-box input:focus { outline: none; border-color: #667eea; }
        .btn { padding: 12px 24px; border: none; border-radius: 8px; font-size: 15px; font-weight: 500; cursor: pointer; transition: all 0.3s; text-decoration: none; display: inline-block; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4); }
        .btn-success { background: #28a745; color: white; }
        .btn-warning { background: #ffc107; color: #333; }
        .btn-danger { background: #dc3545; color: white; }
        .btn-secondary { background: #6c757d; color: white; }
        .btn-sm { padding: 8px 16px; font-size: 14px; }
        .table-container { padding: 30px; overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; }
        th { background: #f8f9fa; color: #333; font-weight: 600; text-align: left; padding: 15px; border-bottom: 2px solid #dee2e6; }
        td { padding: 15px; border-bottom: 1px solid #dee2e6; }
        tr:hover { background: #f8f9fa; }
        .badge { padding: 6px 12px; border-radius: 20px; font-size: 13px; font-weight: 500; display: inline-block; }
        .badge-success { background: #d4edda; color: #155724; }
        .badge-danger { background: #f8d7da; color: #721c24; }
        .badge-primary { background: #cfe2ff; color: #084298; }
        .action-buttons { display: flex; gap: 8px; }
        .pagination { display: flex; justify-content: center; align-items: center; padding: 20px; gap: 10px; }
        .pagination a, .pagination span { padding: 8px 14px; border: 1px solid #dee2e6; border-radius: 5px; text-decoration: none; color: #667eea; }
        .pagination a:hover { background: #667eea; color: white; }
        .pagination .active { background: #667eea; color: white; border-color: #667eea; }
        .alert { padding: 15px 20px; margin: 20px 30px; border-radius: 8px; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>👥 Quản lý người dùng</h1>
            <span>Xin chào, <strong>${sessionScope.user.fullName}</strong></span>
        </div>

        <c:if test="${not empty param.message}">
            <div class="alert alert-success">✓ ${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">✗ ${param.error}</div>
        </c:if>

        <div class="toolbar">
            <form method="get" action="${pageContext.request.contextPath}/admin/users" class="search-box">
                <input type="text" name="search" placeholder="🔍 Tìm kiếm theo tên, email, số điện thoại..." value="${param.search}">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </form>
            <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-success">➕ Thêm người dùng mới</a>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty users}">
                    <div style="text-align: center; padding: 50px; color: #6c757d;">
                        <h3>Không tìm thấy người dùng nào</h3>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Email</th>
                                <th>Họ và tên</th>
                                <th>Giới tính</th>
                                <th>Số điện thoại</th>
                                <th>Role ID</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.email}</td>
                                    <td>${user.fullName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.gender == 'Male'}">👨 Nam</c:when>
                                            <c:when test="${user.gender == 'Female'}">👩 Nữ</c:when>
                                            <c:otherwise>⚧ Khác</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${user.phone}</td>
                                    <td><span class="badge badge-primary">Role ${user.roleId}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.status}">
                                                <span class="badge badge-success">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Bị khóa</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/admin/users/detail?id=${user.id}" class="btn btn-primary btn-sm">👁️ Xem</a>
                                            <a href="${pageContext.request.contextPath}/admin/users/edit?id=${user.id}" class="btn btn-warning btn-sm">✏️ Sửa</a>
                                            <c:choose>
                                                <c:when test="${user.status}">
                                                    <form method="post" action="${pageContext.request.contextPath}/admin/users/toggle-status" style="display: inline;">
                                                        <input type="hidden" name="userId" value="${user.id}">
                                                        <input type="hidden" name="status" value="false">
                                                        <button type="submit" class="btn btn-danger btn-sm">🔒 Khóa</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" action="${pageContext.request.contextPath}/admin/users/toggle-status" style="display: inline;">
                                                        <input type="hidden" name="userId" value="${user.id}">
                                                        <input type="hidden" name="status" value="true">
                                                        <button type="submit" class="btn btn-success btn-sm">🔓 Mở</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&search=${param.search}">« Trước</a>
                            </c:if>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="active">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?page=${i}&search=${param.search}">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&search=${param.search}">Sau »</a>
                            </c:if>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
