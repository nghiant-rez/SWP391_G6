<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Ma trận phân quyền</title>
    <style>
        * { 
            margin: 0; 
            padding: 0; 
            box-sizing: border-box; 
        }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, 
                         Verdana, sans-serif; 
            background: #f5f5f5; 
            min-height: 100vh; 
            padding: 20px; 
        }
        .container { 
            max-width: 1600px; 
            margin: 0 auto; 
            background: white; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
            overflow: hidden; 
        }
        .header { 
            background: #2c3e50; 
            color: white; 
            padding: 20px 30px; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
        }
        .header h1 { 
            font-size: 28px; 
            font-weight: 600; 
        }
        .header-right { 
            display: flex; 
            align-items: center; 
            gap: 15px; 
        }
        .btn-logout { 
            background: rgba(255,255,255,0.15); 
            color: white; 
            padding: 8px 16px; 
            border-radius: 4px; 
            border: 1px solid rgba(255,255,255,0.3); 
            text-decoration: none; 
            font-size: 14px; 
            transition: all 0.2s; 
        }
        .btn-logout:hover { 
            background: rgba(255,255,255,0.25); 
            border-color: rgba(255,255,255,0.5); 
        }
        .content { 
            padding: 30px; 
        }
        .alert { 
            padding: 15px 20px; 
            margin-bottom: 20px; 
            border-radius: 8px; 
            font-size: 15px; 
        }
        .alert-success { 
            background: #d4edda; 
            color: #155724; 
            border: 1px solid #c3e6cb; 
        }
        .alert-danger { 
            background: #f8d7da; 
            color: #721c24; 
            border: 1px solid #f5c6cb; 
        }
        .matrix-wrapper { 
            overflow-x: auto; 
            margin-bottom: 20px; 
            border: 1px solid #dee2e6; 
            border-radius: 8px; 
        }
        .matrix-table { 
            width: 100%; 
            border-collapse: collapse; 
            min-width: 800px; 
        }
        .matrix-table thead th { 
            background: #2c3e50; 
            color: white; 
            padding: 15px 12px; 
            font-weight: 600; 
            text-align: center; 
            border: 1px solid #34495e; 
            position: sticky; 
            top: 0; 
            z-index: 10; 
            min-width: 100px; 
        }
        .matrix-table thead th:first-child { 
            text-align: left; 
            min-width: 200px; 
            position: sticky; 
            left: 0; 
            z-index: 11; 
        }
        .matrix-table tbody th { 
            background: #f8f9fa; 
            padding: 12px 15px; 
            text-align: left; 
            font-weight: 600; 
            color: #333; 
            border: 1px solid #dee2e6; 
            position: sticky; 
            left: 0; 
            z-index: 5; 
        }
        .matrix-table tbody td { 
            padding: 12px; 
            text-align: center; 
            border: 1px solid #dee2e6; 
            background: white; 
        }
        .matrix-table tbody tr:hover td { 
            background: #f8f9fa; 
        }
        .matrix-table tbody tr:hover th { 
            background: #e9ecef; 
        }
        .matrix-table input[type="checkbox"] { 
            cursor: pointer; 
            width: 20px; 
            height: 20px; 
        }
        .role-header { 
            white-space: nowrap; 
            font-size: 14px; 
            font-weight: 600; 
        }
        .permission-name { 
            font-weight: 600; 
            color: #333; 
        }
        .permission-desc { 
            display: block; 
            font-size: 12px; 
            color: #6c757d; 
            font-weight: normal; 
            margin-top: 3px; 
        }
        .btn { 
            padding: 10px 20px; 
            border: none; 
            border-radius: 4px; 
            font-size: 14px; 
            font-weight: 500; 
            cursor: pointer; 
            transition: all 0.2s; 
            text-decoration: none; 
            display: inline-block; 
            margin-right: 10px; 
        }
        .btn-primary { 
            background: #3498db; 
            color: white; 
        }
        .btn-primary:hover { 
            background: #2980b9; 
        }
        .btn-secondary { 
            background: #95a5a6; 
            color: white; 
        }
        .btn-secondary:hover { 
            background: #7f8c8d; 
        }
        .form-actions { 
            display: flex; 
            gap: 10px; 
            padding-top: 20px; 
            border-top: 2px solid #dee2e6; 
        }
        .info-text { 
            color: #6c757d; 
            font-size: 14px; 
            margin-bottom: 20px; 
            padding: 15px; 
            background: #e7f3ff; 
            border-radius: 8px; 
            border-left: 4px solid #3498db; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Ma trận phân quyền</h1>
            <div class="header-right">
                <span>Xin chào, 
                    <strong>
                        ${sessionScope.user.fullName}
                    </strong>
                </span>
                <a href="${pageContext.request.contextPath}/mock-login?logout=true" 
                   class="btn-logout">
                    Đăng xuất
                </a>
            </div>
        </div>

        <div class="content">
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    <c:out value="${param.message}"/>
                </div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger">
                    <c:out value="${param.error}"/>
                </div>
            </c:if>

            <div class="info-text">
                <strong>Hướng dẫn:</strong> 
                Bảng bên dưới hiển thị tất cả vai trò 
                (hàng ngang) và quyền hạn (cột dọc). 
                Đánh dấu checkbox để gán quyền cho vai trò.
            </div>

            <form method="post" 
                  action="${pageContext.request.contextPath}/admin/roles/matrix">
                
                <div class="matrix-wrapper">
                    <table class="matrix-table">
                        <thead>
                            <tr>
                                <th>Quyền hạn / Vai trò</th>
                                <c:forEach var="role" 
                                           items="${allRoles}">
                                    <th>
                                        <div class="role-header">
                                            <c:out 
                                                value="${role.name}"/>
                                        </div>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="permission" 
                                       items="${allPermissions}">
                                <tr>
                                    <th>
                                        <span 
                                            class="permission-name">
                                            <c:out 
                                                value="${permission.displayName}"/>
                                        </span>
                                        <span 
                                            class="permission-desc">
                                            <c:out 
                                                value="${permission.description}"/>
                                        </span>
                                    </th>
                                    <c:forEach var="role" 
                                               items="${allRoles}">
                                        <td>
                                            <input 
                                                type="checkbox" 
                                                name="role_${role.id}" 
                                                value="${permission.id}"
                                                <c:if test="${matrix[role.id][permission.id] != null and matrix[role.id][permission.id]}">
                                                    checked
                                                </c:if>>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="form-actions">
                    <button type="submit" 
                            class="btn btn-primary">
                        Lưu tất cả thay đổi
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/roles" 
                       class="btn btn-secondary">
                        Quay lại danh sách
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
