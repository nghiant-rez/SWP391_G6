<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Chi tiết vai trò</title>
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
            max-width: 900px; 
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
            text-align: center; 
        }
        .header h1 { 
            font-size: 28px; 
            font-weight: 600; 
        }
        .content { 
            padding: 40px; 
        }
        .info-section { 
            background: #f8f9fa; 
            padding: 20px; 
            border-radius: 8px; 
            margin-bottom: 30px; 
            border-left: 4px solid #667eea; 
        }
        .info-row { 
            display: flex; 
            margin-bottom: 10px; 
        }
        .info-label { 
            font-weight: 600; 
            width: 150px; 
            color: #333; 
        }
        .info-value { 
            color: #666; 
        }
        .badge { 
            display: inline-block; 
            padding: 5px 12px; 
            border-radius: 12px; 
            font-size: 13px; 
            font-weight: 500; 
        }
        .badge-success { 
            background: #d4edda; 
            color: #155724; 
        }
        .badge-danger { 
            background: #f8d7da; 
            color: #721c24; 
        }
        .section-title { 
            font-size: 20px; 
            font-weight: 600; 
            color: #333; 
            margin-bottom: 20px; 
            padding-bottom: 10px; 
            border-bottom: 2px solid #dee2e6; 
        }
        .permission-grid { 
            display: grid; 
            grid-template-columns: repeat(auto-fill, 
                minmax(250px, 1fr)); 
            gap: 15px; 
            margin-bottom: 30px; 
        }
        .permission-item { 
            border: 2px solid #e9ecef; 
            border-radius: 8px; 
            padding: 15px; 
            background: #f8f9fa; 
            transition: all 0.3s; 
        }
        .permission-item:hover { 
            border-color: #667eea; 
            background: #fff; 
        }
        .permission-item label { 
            display: flex; 
            align-items: flex-start; 
            cursor: pointer; 
            font-size: 14px; 
        }
        .permission-item input[type="checkbox"] { 
            margin-right: 10px; 
            margin-top: 3px; 
            cursor: pointer; 
            width: 18px; 
            height: 18px; 
        }
        .permission-name { 
            font-weight: 600; 
            color: #333; 
            display: block; 
            margin-bottom: 5px; 
        }
        .permission-desc { 
            color: #6c757d; 
            font-size: 13px; 
            display: block; 
        }
        .btn { 
            padding: 12px 30px; 
            border: none; 
            border-radius: 8px; 
            font-size: 15px; 
            font-weight: 500; 
            cursor: pointer; 
            transition: all 0.3s; 
            text-decoration: none; 
            display: inline-block; 
            margin-right: 10px; 
        }
        .btn-primary { 
            background: linear-gradient(135deg, 
                #667eea 0%, #764ba2 100%); 
            color: white; 
        }
        .btn-secondary { 
            background: #6c757d; 
            color: white; 
        }
        .btn:hover { 
            transform: translateY(-2px); 
            box-shadow: 0 5px 15px rgba(0,0,0,0.2); 
        }
        .form-actions { 
            display: flex; 
            gap: 10px; 
            padding-top: 20px; 
            border-top: 2px solid #dee2e6; 
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
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Chi tiết vai trò</h1>
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

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">ID:</div>
                    <div class="info-value">${role.id}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tên vai trò:</div>
                    <div class="info-value">
                        <strong><c:out value="${role.name}"/></strong>
                    </div>
                </div>
                <div class="info-row">
                    <div class="info-label">Mô tả:</div>
                    <div class="info-value">
                        <c:out value="${role.description}"/>
                    </div>
                </div>
                <div class="info-row">
                    <div class="info-label">Trạng thái:</div>
                    <div class="info-value">
                        <c:choose>
                            <c:when test="${role.deleted}">
                                <span class="badge badge-danger">
                                    Đã vô hiệu hóa
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-success">
                                    Đang hoạt động
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="section-title">Phân quyền</div>

            <form method="post" 
                  action="${pageContext.request.contextPath}/admin/roles/permissions">
                <input type="hidden" name="roleId" 
                       value="${role.id}">

                <div class="permission-grid">
                    <c:forEach var="permission" 
                               items="${allPermissions}">
                        <div class="permission-item">
                            <label>
                                <input type="checkbox" 
                                       name="permissionIds" 
                                       value="${permission.id}"
                                       <c:if test="${assignedIds.contains(permission.id)}">
                                           checked
                                       </c:if>>
                                <div>
                                    <span class="permission-name">
                                        <c:out value="${permission.displayName}"/>
                                    </span>
                                    <span class="permission-desc">
                                        <c:out value="${permission.description}"/>
                                    </span>
                                </div>
                            </label>
                        </div>
                    </c:forEach>
                </div>

                <div class="form-actions">
                    <button type="submit" 
                            class="btn btn-primary">
                        Lưu quyền hạn
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/roles" 
                       class="btn btn-secondary">
                        Quay lại
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
