<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doi mat khau</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }
        .container {
            max-width: 520px;
            margin: 40px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .header {
            background: #2c3e50;
            color: white;
            padding: 20px 30px;
            text-align: center;
        }
        .header h1 {
            font-size: 24px;
            font-weight: 600;
        }
        .content { padding: 35px 40px; }
        .form-group { margin-bottom: 22px; }
        label {
            display: block;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            font-size: 15px;
        }
        label .required { color: #dc3545; }
        input[type="password"] {
            width: 100%;
            padding: 11px 14px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 15px;
            transition: border-color 0.2s;
        }
        input[type="password"]:focus {
            outline: none;
            border-color: #3498db;
        }
        .hint {
            font-size: 13px;
            color: #6c757d;
            margin-top: 5px;
        }
        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 28px;
            padding-top: 20px;
            border-top: 2px solid #dee2e6;
        }
        .btn {
            padding: 11px 22px;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            font-weight: 500;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.2s;
        }
        .btn-primary { background: #3498db; color: white; }
        .btn-primary:hover { background: #2980b9; }
        .btn-secondary { background: #95a5a6; color: white; }
        .btn-secondary:hover { background: #7f8c8d; }
        .alert {
            padding: 14px 18px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-size: 14px;
        }
        .alert-danger {
            background: #f8d7da;
            color: #842029;
            border: 1px solid #f5c2c7;
        }
        .alert-success {
            background: #d1e7dd;
            color: #0f5132;
            border: 1px solid #badbcc;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/includes/navbar.jsp" %>

<div class="container">
    <div class="header">
        <h1>Doi mat khau</h1>
    </div>
    <div class="content">

        <%-- Flash success message from redirect --%>
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <c:out value="${param.message}"/>
            </div>
        </c:if>

        <%-- Validation / server error --%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <form method="post"
              action="${pageContext.request.contextPath}/change-password"
              accept-charset="UTF-8">

            <div class="form-group">
                <label for="currentPassword">
                    Mat khau hien tai
                    <span class="required">*</span>
                </label>
                <input type="password"
                       id="currentPassword"
                       name="currentPassword"
                       required
                       autocomplete="current-password">
            </div>

            <div class="form-group">
                <label for="newPassword">
                    Mat khau moi
                    <span class="required">*</span>
                </label>
                <input type="password"
                       id="newPassword"
                       name="newPassword"
                       required
                       autocomplete="new-password">
                <div class="hint">Toi thieu 6 ky tu.</div>
            </div>

            <div class="form-group">
                <label for="confirmPassword">
                    Xac nhan mat khau moi
                    <span class="required">*</span>
                </label>
                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       required
                       autocomplete="new-password">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    Luu thay doi
                </button>
                <a href="${pageContext.request.contextPath}/home"
                   class="btn btn-secondary">
                    Huy bo
                </a>
            </div>
        </form>

    </div>
</div>

</body>
</html>
