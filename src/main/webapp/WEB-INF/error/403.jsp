<%-- File: src/main/webapp/WEB-INF/error/403.jsp --%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<%@ taglib prefix = "c" uri = "jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>403 - Access Denied</title>
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .error-container {
            background: white;
            padding: 50px;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            text-align: center;
            max-width: 500px;
        }

        .error-code {
            font-size: 100px;
            font-weight: bold;
            color: #dc3545;
            margin: 0;
            line-height: 1;
        }

        .error-title {
            font-size: 24px;
            color: #333;
            margin: 20px 0 10px 0;
        }

        .error-message {
            font-size: 16px;
            color: #666;
            margin: 20px 0;
            padding: 15px;
            background: #f8f9fa;
            border-left: 4px solid #dc3545;
            text-align: left;
        }

        .error-details {
            font-size: 14px;
            color: #999;
            margin-top: 10px;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            margin-top: 20px;
            transition: transform 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
<div class = "error-container">
    <p class = "error-code">403</p>
    <h2 class = "error-title">🔒 Access Denied</h2>

    <div class = "error-message">
        <c:choose>
            <c:when test = "${not empty errorMessage}">
                <%-- SECURITY: Use c:out to prevent Cross-Site Scripting (XSS) --%>
                <c:out value = "${errorMessage}"/>
            </c:when>
            <c:otherwise>
                You don't have permission to access this resource.
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test = "${not empty requestedUrl}">
        <p class = "error-details">
            Requested URL: <code><c:out value = "${requestedUrl}"/></code>
        </p>
    </c:if>

    <a href = "${pageContext.request.contextPath}/mock-login" class = "btn">
        Back to Login
    </a>
</div>
</body>
</html>