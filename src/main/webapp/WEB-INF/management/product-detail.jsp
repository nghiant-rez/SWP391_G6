<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details - ${product.name}</title>
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
            max-width: 1000px;
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
            margin-bottom: 30px;
        }
        .tab {
            padding: 12px 24px;
            cursor: pointer;
            border: none;
            background: none;
            font-size: 15px;
            font-weight: 600;
            color: #6c757d;
            border-bottom: 3px solid transparent;
            transition: all 0.3s;
        }
        .tab:hover {
            color: #667eea;
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
        .detail-view {
            display: grid;
            grid-template-columns: 200px 1fr;
            gap: 30px;
        }
        .product-image {
            width: 200px;
            height: 200px;
            object-fit: cover;
            border-radius: 8px;
            border: 2px solid #e9ecef;
        }
        .no-image {
            width: 200px;
            height: 200px;
            background: #f8f9fa;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 60px;
            border: 2px solid #e9ecef;
        }
        .detail-info {
            flex: 1;
        }
        .info-group {
            margin-bottom: 20px;
        }
        .info-label {
            font-weight: 600;
            color: #6c757d;
            font-size: 13px;
            text-transform: uppercase;
            margin-bottom: 5px;
        }
        .info-value {
            font-size: 16px;
            color: #2c3e50;
        }
        .badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            display: inline-block;
        }
        .badge-active {
            background: #d4edda;
            color: #155724;
        }
        .badge-discontinued {
            background: #f8d7da;
            color: #721c24;
        }
        .price {
            font-size: 24px;
            font-weight: 700;
            color: #27ae60;
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
        input[type="number"],
        select,
        textarea {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            font-family: inherit;
        }
        input:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
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
            background: #3498db;
            color: white;
        }
        .btn-primary:hover {
            background: #2980b9;
        }
        .btn-success {
            background: #27ae60;
            color: white;
        }
        .btn-success:hover {
            background: #229954;
        }
        .btn-danger {
            background: #e74c3c;
            color: white;
        }
        .btn-danger:hover {
            background: #c0392b;
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
        .delete-section {
            margin-top: 40px;
            padding: 20px;
            border: 2px solid #f8d7da;
            border-radius: 8px;
            background: #fff5f5;
        }
        .delete-section h3 {
            color: #e74c3c;
            margin-bottom: 10px;
        }
        .delete-section p {
            color: #6c757d;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="page-header">
            <h1>Product Details</h1>
            <a href="${pageContext.request.contextPath}/management/products" 
               class="btn btn-secondary">
                ← Back to List
            </a>
        </div>

        <div class="content">
            <c:if test="${not empty param.success}">
                <div class="alert alert-success"><c:out value="${param.success}"/></div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <div class="tabs">
                <button class="tab active" onclick="switchTab('view')">
                    View Details
                </button>
                <button class="tab" onclick="switchTab('edit')">
                    Edit Product
                </button>
            </div>

            <!-- View Tab -->
            <div id="view-tab" class="tab-content active">
                <div class="detail-view">
                    <div>
                        <c:choose>
                            <c:when test="${not empty product.imageUrl}">
                                <img src="${product.imageUrl}" 
                                     alt="${product.name}" 
                                     class="product-image">
                            </c:when>
                            <c:otherwise>
                                <div class="no-image">No Image</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="detail-info">
                        <div class="info-group">
                            <div class="info-label">Product ID</div>
                            <div class="info-value">#${product.id}</div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Product Name</div>
                            <div class="info-value"><strong>${product.name}</strong></div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Category</div>
                            <div class="info-value">${product.categoryName}</div>
                        </div>

                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                            <div class="info-group">
                                <div class="info-label">Model</div>
                                <div class="info-value">
                                    ${product.model != null ? product.model : '-'}
                                </div>
                            </div>

                            <div class="info-group">
                                <div class="info-label">Brand</div>
                                <div class="info-value">
                                    ${product.brand != null ? product.brand : '-'}
                                </div>
                            </div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Base Price</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${product.basePrice != null}">
                                        <span class="price">
                                            <fmt:formatNumber value="${product.basePrice}" 
                                                            type="currency" 
                                                            currencySymbol="$"/>
                                        </span>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Status</div>
                            <div class="info-value">
                                <span class="badge ${product.status == 'ACTIVE' ? 'badge-active' : 'badge-discontinued'}">
                                    ${product.status}
                                </span>
                            </div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Description</div>
                            <div class="info-value">
                                ${product.description != null ? product.description : 'No description available'}
                            </div>
                        </div>

                        <div class="info-group">
                            <div class="info-label">Specifications</div>
                            <div class="info-value" style="white-space: pre-wrap;">
                                ${product.specifications != null ? product.specifications : 'No specifications available'}
                            </div>
                        </div>

                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px;">
                            <div class="info-group">
                                <div class="info-label">Created At</div>
                                <div class="info-value">
                                    <fmt:parseDate value="${product.createdAt}" 
                                                   pattern="yyyy-MM-dd'T'HH:mm"
                                                   var="parsedCreated" type="both"/>
                                    <fmt:formatDate value="${parsedCreated}" 
                                                   pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                            </div>

                            <div class="info-group">
                                <div class="info-label">Last Updated</div>
                                <div class="info-value">
                                    <fmt:parseDate value="${product.updatedAt}" 
                                                   pattern="yyyy-MM-dd'T'HH:mm"
                                                   var="parsedUpdated" type="both"/>
                                    <fmt:formatDate value="${parsedUpdated}" 
                                                   pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Edit Tab -->
            <div id="edit-tab" class="tab-content">
                <form method="POST" 
                      action="${pageContext.request.contextPath}/management/products/detail">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${product.id}">

                    <div class="form-group">
                        <label for="categoryId">
                            Category <span class="required">*</span>
                        </label>
                        <select id="categoryId" name="categoryId" required>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.id}" 
                                        ${cat.id == product.categoryId ? 'selected' : ''}>
                                    ${cat.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="name">
                            Product Name <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="name" 
                               name="name" 
                               value="${product.name}"
                               required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="model">Model</label>
                            <input type="text" 
                                   id="model" 
                                   name="model" 
                                   value="${product.model}">
                        </div>

                        <div class="form-group">
                            <label for="brand">Brand</label>
                            <input type="text" 
                                   id="brand" 
                                   name="brand" 
                                   value="${product.brand}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="basePrice">Base Price ($)</label>
                            <input type="number" 
                                   id="basePrice" 
                                   name="basePrice" 
                                   step="0.01"
                                   min="0"
                                   value="${product.basePrice}">
                        </div>

                        <div class="form-group">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>
                                    Active
                                </option>
                                <option value="DISCONTINUED" ${product.status == 'DISCONTINUED' ? 'selected' : ''}>
                                    Discontinued
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="imageUrl">Image URL</label>
                        <input type="text" 
                               id="imageUrl" 
                               name="imageUrl" 
                               value="${product.imageUrl}">
                    </div>

                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" 
                                  name="description">${product.description}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="specifications">Specifications</label>
                        <textarea id="specifications" 
                                  name="specifications">${product.specifications}</textarea>
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

                <div class="delete-section">
                    <h3>Danger Zone</h3>
                    <p>Once you delete this product, there is no going back. Please be certain.</p>
                    <form method="POST" 
                          action="${pageContext.request.contextPath}/management/products/detail"
                          onsubmit="return confirm('Are you sure you want to delete this product? This action cannot be undone.');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${product.id}">
                        <button type="submit" class="btn btn-danger">
                            Delete Product
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function switchTab(tabName) {
            // Hide all tabs
            document.querySelectorAll('.tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            document.querySelectorAll('.tab').forEach(tab => {
                tab.classList.remove('active');
            });

            // Show selected tab
            document.getElementById(tabName + '-tab').classList.add('active');
            event.target.classList.add('active');
        }
    </script>
</body>
</html>
