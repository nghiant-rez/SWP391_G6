<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>403 - Truy cap bi tu choi</title>
    <style>
        * { 
            margin: 0; 
            padding: 0; 
            box-sizing: border-box; 
        }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background: #f5f5f5; 
            min-height: 100vh; 
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px; 
        }
        .container { 
            max-width: 600px; 
            width: 100%;
            background: white; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
            overflow: hidden; 
        }
        .header { 
            background: #e74c3c; 
            color: white; 
            padding: 30px; 
            text-align: center;
        }
        .header .error-code { 
            font-size: 72px; 
            font-weight: 700;
            line-height: 1;
            margin-bottom: 10px;
        }
        .header h1 { 
            font-size: 24px; 
            font-weight: 500; 
        }
        .content {
            padding: 40px 30px;
            text-align: center;
        }
        .error-message {
            background: #f8f9fa;
            border-left: 4px solid #e74c3c;
            padding: 20px;
            margin-bottom: 25px;
            text-align: left;
            color: #333;
            font-size: 15px;
            border-radius: 0 4px 4px 0;
        }
        .error-details {
            color: #6c757d;
            font-size: 13px;
            margin-bottom: 30px;
            word-break: break-all;
        }
        .error-details code {
            background: #e9ecef;
            padding: 3px 8px;
            border-radius: 4px;
            font-family: 'Consolas', monospace;
        }
        .btn { 
            padding: 12px 24px; 
            border: none; 
            border-radius: 4px; 
            font-size: 14px; 
            font-weight: 500; 
            cursor: pointer; 
            transition: all 0.2s; 
            text-decoration: none; 
            display: inline-block; 
            margin: 5px;
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
        .actions {
            display: flex;
            justify-content: center;
            gap: 10px;
            flex-wrap: wrap;
        }
        .user-info {
            background: #fff3cd;
            border: 1px solid #ffc107;
            border-radius: 4px;
            padding: 12px 20px;
            margin-bottom: 25px;
            font-size: 14px;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="error-code">403</div>
            <h1>Truy cap bi tu choi</h1>
        </div>
        
        <div class="content">
            <c:if test="${not empty sessionScope.user}">
                <div class="user-info">
                    Dang nhap voi: 
                    <strong>${sessionScope.user.fullName}</strong>
                    (${sessionScope.user.roleName})
                </div>
            </c:if>

            <div class="error-message">
                <c:choose>
                    <c:when test="${not empty errorMessage}">
                        <c:out value="${errorMessage}"/>
                    </c:when>
                    <c:otherwise>
                        Ban khong co quyen truy cap tai nguyen nay. 
                        Vui long lien he quan tri vien neu ban cho rang 
                        day la loi.
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${not empty requestedUrl}">
                <p class="error-details">
                    URL yeu cau: <code><c:out value="${requestedUrl}"/></code>
                </p>
            </c:if>

            <div class="actions">
                <a href="javascript:history.back()" class="btn btn-secondary">
                    Quay lai
                </a>
                <a href="${pageContext.request.contextPath}/" 
                   class="btn btn-primary">
                    Trang chu
                </a>
                <c:if test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/logout" 
                       class="btn btn-secondary">
                        Dang xuat
                    </a>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>
