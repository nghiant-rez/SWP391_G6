<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa vai trò</title>
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
            max-width: 800px; 
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
        .form-group { 
            margin-bottom: 25px; 
        }
        label { 
            display: block; 
            font-weight: 600; 
            color: #333; 
            margin-bottom: 8px; 
            font-size: 15px; 
        }
        label .required { 
            color: #dc3545; 
        }
        input[type="text"], textarea { 
            width: 100%; 
            padding: 12px 15px; 
            border: 2px solid #ddd; 
            border-radius: 8px; 
            font-size: 15px; 
            transition: border-color 0.3s; 
            font-family: inherit; 
        }
        input:focus, textarea:focus { 
            outline: none; 
            border-color: #667eea; 
        }
        textarea { 
            resize: vertical; 
            min-height: 100px; 
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
            margin-top: 30px; 
            padding-top: 20px; 
            border-top: 2px solid #dee2e6; 
        }
        .alert { 
            padding: 15px 20px; 
            margin-bottom: 20px; 
            border-radius: 8px; 
            font-size: 15px; 
        }
        .alert-danger { 
            background: #f8d7da; 
            color: #721c24; 
            border: 1px solid #f5c6cb; 
        }
        .info-box { 
            background: #e7f3ff; 
            padding: 15px; 
            border-radius: 8px; 
            margin-bottom: 20px; 
            border-left: 4px solid #667eea; 
        }
        .info-box p { 
            margin: 5px 0; 
            color: #333; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Chỉnh sửa thông tin vai trò</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <div class="info-box">
                <p><strong>ID:</strong> ${role.id}</p>
                <p><strong>Trạng thái:</strong> 
                    <c:choose>
                        <c:when test="${role.deleted}">
                            Đã vô hiệu hóa
                        </c:when>
                        <c:otherwise>
                            Đang hoạt động
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <form method="post" 
                  action="${pageContext.request.contextPath}/admin/roles/edit">
                <input type="hidden" name="id" value="${role.id}">

                <div class="form-group">
                    <label for="name">
                        Tên vai trò 
                        <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           value="<c:out value='${role.name}'/>" 
                           required 
                           maxlength="50"
                           placeholder="Ví dụ: ADMINISTRATOR, MANAGER">
                    <small style="color: #6c757d;">
                        Tên vai trò phải là duy nhất
                    </small>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" 
                              name="description" 
                              maxlength="255"
                              placeholder="Mô tả chức năng của vai trò này..."><c:out value="${role.description}"/></textarea>
                    <small style="color: #6c757d;">
                        Tối đa 255 ký tự
                    </small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Cập nhật
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/roles" 
                       class="btn btn-secondary">
                        Hủy bỏ
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
