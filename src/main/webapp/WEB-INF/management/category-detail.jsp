<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Details - ${category.name}</title>
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
            max-width: 900px;
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
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .page-header h1 {
            font-size: 28px;
            font-weight: 600;
        }
        .content {
            padding: 30px;
        }
        .alert {
            padding: 12px 20px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-size: 14px;
        }
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .tabs {
            display: flex;
            border-bottom: 2px solid #e9ecef;
            margin-bottom: 20px;
        }
        .tab {
            padding: 12px 24px;
            border: none;
            background: transparent;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            color: #6c757d;
            border-bottom: 3px solid transparent;
            transition: all 0.3s;
        }
        .tab.active {
            color: #667eea;
            border-bottom-color: #667eea;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        .detail-grid {
            display: grid;
            gap: 20px;
        }
        .detail-item {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
        }
        .detail-item label {
            display: block;
            font-size: 12px;
            color: #6c757d;
            text-transform: uppercase;
            margin-bottom: 5px;
        }
        .detail-item .value {
            font-size: 16px;
            color: #2c3e50;
        }
        .badge {
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }
        .badge-active {
            background: #d4edda;
            color: #155724;
        }
        .badge-deleted {
            background: #f8d7da;
            color: #721c24;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
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
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }
        .btn-secondary {
            background: #95a5a6;
            color: white;
        }
        .btn-secondary:hover {
            background: #7f8c8d;
        }
        .btn-success {
            background: #27ae60;
            color: white;
        }
        .btn-success:hover {
            background: #229954;
        }
        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
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
            <h1>Category Details</h1>
            <a href="${pageContext.request.contextPath}/management/categories" 
               class="btn btn-secondary">
                ← Back to List
            </a>
        </div>

        <div class="content">
            <c:if test="${not empty success}">
                <div class="alert alert-success"><c:out value="${success}"/></div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <div class="tabs">
                <button class="tab active" onclick="switchTab('view')">
                    View Details
                </button>
                <button class="tab" onclick="switchTab('edit')">
                    Edit Category
                </button>
            </div>

            <!-- View Tab -->
            <div id="view-tab" class="tab-content active">
                <div class="detail-grid">
                    <div class="detail-item">
                        <label>Category ID</label>
                        <div class="value">#${category.id}</div>
                    </div>

                    <div class="detail-item">
                        <label>Category Name</label>
                        <div class="value"><strong>${category.name}</strong></div>
                    </div>

                    <div class="detail-item">
                        <label>Description</label>
                        <div class="value">
                            <c:choose>
                                <c:when test="${not empty category.description}">
                                    ${category.description}
                                </c:when>
                                <c:otherwise>
                                    <span style="color: #6c757d;">No description</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="detail-item">
                        <label>Status</label>
                        <div class="value">
                            <c:choose>
                                <c:when test="${category.deleted}">
                                    <span class="badge badge-deleted">Deleted</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-active">Active</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="detail-item">
                        <label>Created At</label>
                        <div class="value">
                            ${category.createdAt != null ? category.createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : ""}
                        </div>
                    </div>

                    <div class="detail-item">
                        <label>Last Updated</label>
                        <div class="value">
                            ${category.updatedAt != null ? category.updatedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : ""}
                        </div>
                    </div>
                </div>
            </div>

            <!-- Edit Tab -->
            <div id="edit-tab" class="tab-content">
                <form method="POST" 
                      action="${pageContext.request.contextPath}/management/categories/detail">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${category.id}">

                    <div class="form-group">
                        <label for="name">
                            Category Name <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="name" 
                               name="name" 
                               value="${category.name}"
                               required
                               minlength="2"
                               maxlength="100"
                               pattern="^[a-zA-Z0-9\s\-&]+$"
                               title="Category name must be 2-100 characters">
                        <div class="help-text">2-100 characters, letters, numbers, spaces, hyphens, and &</div>
                    </div>

                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" 
                                  name="description"
                                  maxlength="500">${category.description}</textarea>
                        <div class="help-text">Max 500 characters</div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-success">
                            Save Changes
                        </button>
                        <button type="button" 
                                class="btn btn-secondary" 
                                onclick="switchTab('view')">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function switchTab(tab) {
            // Hide all tabs
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            // Show selected tab
            if (tab === 'view') {
                document.querySelector('.tab:nth-child(1)').classList.add('active');
                document.getElementById('view-tab').classList.add('active');
            } else {
                document.querySelector('.tab:nth-child(2)').classList.add('active');
                document.getElementById('edit-tab').classList.add('active');
            }
        }
    </script>
</body>
</html>
