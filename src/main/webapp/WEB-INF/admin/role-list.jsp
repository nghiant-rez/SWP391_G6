<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Danh sach vai tro</title>
    <style>
        * { 
            margin: 0; 
            padding: 0; 
            box-sizing: border-box; 
        }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, 
                         Verdana, sans-serif; 
            background: linear-gradient(135deg, 
                #667eea 0%, #764ba2 100%); 
            min-height: 100vh; 
            padding: 20px; 
        }
        .container { 
            max-width: 1200px; 
            margin: 0 auto; 
            background: white; 
            border-radius: 15px; 
            box-shadow: 0 10px 40px rgba(0,0,0,0.2); 
            overflow: hidden; 
        }
        .header { 
            background: linear-gradient(135deg, 
                #667eea 0%, #764ba2 100%); 
            color: white; 
            padding: 30px; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
        }
        .header h1 { 
            font-size: 28px; 
            font-weight: 600; 
        }
        .toolbar { 
            background: #f8f9fa; 
            padding: 20px 30px; 
            border-bottom: 2px solid #e9ecef; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
        }
        .btn { 
            padding: 12px 24px; 
            border: none; 
            border-radius: 8px; 
            font-size: 15px; 
            font-weight: 500; 
            cursor: pointer; 
            transition: all 0.3s; 
            text-decoration: none; 
            display: inline-block; 
        }
        .btn-primary { 
            background: linear-gradient(135deg, 
                #667eea 0%, #764ba2 100%); 
            color: white; 
        }
        .btn-primary:hover { 
            transform: translateY(-2px); 
            box-shadow: 0 5px 15px 
                rgba(102, 126, 234, 0.4); 
        }
        .btn-warning { 
            background: #ffc107; 
            color: #333; 
        }
        .btn-success { 
            background: #28a745; 
            color: white; 
        }
        .btn-sm { 
            padding: 8px 16px; 
            font-size: 14px; 
        }
        .table-container { 
            padding: 30px; 
            overflow-x: auto; 
        }
        table { 
            width: 100%; 
            border-collapse: collapse; 
        }
        th { 
            background: #f8f9fa; 
            color: #333; 
            font-weight: 600; 
            text-align: left; 
            padding: 15px; 
            border-bottom: 2px solid #dee2e6; 
        }
        td { 
            padding: 15px; 
            border-bottom: 1px solid #dee2e6; 
        }
        tr:hover { 
            background: #f8f9fa; 
        }
        .badge { 
            padding: 6px 12px; 
            border-radius: 20px; 
            font-size: 13px; 
            font-weight: 500; 
            display: inline-block; 
        }
        .badge-success { 
            background: #d4edda; 
            color: #155724; 
        }
        .badge-danger { 
            background: #f8d7da; 
            color: #721c24; 
        }
        .action-buttons { 
            display: flex; 
            gap: 8px; 
        }
        .alert { 
            padding: 15px 20px; 
            margin: 20px 30px; 
            border-radius: 8px; 
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
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Quan ly vai tro</h1>
            <span>Xin chao, 
                <strong>
                    ${sessionScope.user.fullName}
                </strong>
            </span>
        </div>

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

        <div class="toolbar">
            <div>
                <strong>${roles.size()}</strong> vai tro
            </div>
            <a href="${pageContext.request.contextPath}/mock-login" 
               class="btn btn-primary btn-sm">
                Quay lai Mock Login
            </a>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty roles}">
                    <div style="text-align: center; 
                                padding: 50px; 
                                color: #6c757d;">
                        <h3>Khong co vai tro nao</h3>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Ten vai tro</th>
                                <th>Mo ta</th>
                                <th>Trang thai</th>
                                <th>Thao tac</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="role" items="${roles}">
                                <tr>
                                    <td>${role.id}</td>
                                    <td>
                                        <strong>
                                            <c:out 
                                                value="${role.name}"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <c:out 
                                            value="${role.description}"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when 
                                                test="${role.deleted}">
                                                <span 
                                                    class="badge badge-danger">
                                                    Da vo hieu hoa
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span 
                                                    class="badge badge-success">
                                                    Dang hoat dong
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/admin/roles/view?id=${role.id}" 
                                               class="btn btn-primary btn-sm">
                                                Xem
                                            </a>
                                            
                                            <c:choose>
                                                <c:when 
                                                    test="${role.deleted}">
                                                    <form method="post" 
                                                          action="${pageContext.request.contextPath}/admin/roles/toggle" 
                                                          style="display: inline;">
                                                        <input 
                                                            type="hidden" 
                                                            name="roleId" 
                                                            value="${role.id}">
                                                        <button 
                                                            type="submit" 
                                                            class="btn btn-success btn-sm"
                                                            onclick="return confirm('Kich hoat vai tro nay?');">
                                                            Kich hoat
                                                        </button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" 
                                                          action="${pageContext.request.contextPath}/admin/roles/toggle" 
                                                          style="display: inline;">
                                                        <input 
                                                            type="hidden" 
                                                            name="roleId" 
                                                            value="${role.id}">
                                                        <button 
                                                            type="submit" 
                                                            class="btn btn-warning btn-sm"
                                                            onclick="return confirm('Vo hieu hoa vai tro nay?');">
                                                            Vo hieu hoa
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
