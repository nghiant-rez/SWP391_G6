<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Category</title>
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
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 30px;
        }
        .page-header h1 {
            font-size: 28px;
            font-weight: 600;
        }
        .form-container {
            padding: 30px;
        }
        .alert {
            padding: 12px 20px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-size: 14px;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
        }
        .required {
            color: #e74c3c;
        }
        input[type="text"],
        textarea {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            font-family: inherit;
        }
        input:focus,
        textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }
        .btn-primary {
            background: #27ae60;
            color: white;
        }
        .btn-primary:hover {
            background: #229954;
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
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
        }
        .help-text {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="page-header">
            <h1>Add New Category</h1>
        </div>

        <div class="form-container">
            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/management/categories/create">
                <div class="form-group">
                    <label for="name">
                        Category Name <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           placeholder="Enter category name"
                           required
                           minlength="2"
                           maxlength="100"
                           pattern="^[a-zA-Z0-9\s\-&]+$"
                           title="Category name must be 2-100 characters (letters, numbers, spaces, hyphens, and & only)">
                    <div class="help-text">2-100 characters, letters, numbers, spaces, hyphens, and &</div>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" 
                              name="description" 
                              placeholder="Enter category description"
                              maxlength="500"></textarea>
                    <div class="help-text">Max 500 characters</div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Create Category
                    </button>
                    <a href="${pageContext.request.contextPath}/management/categories" 
                       class="btn btn-secondary">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
