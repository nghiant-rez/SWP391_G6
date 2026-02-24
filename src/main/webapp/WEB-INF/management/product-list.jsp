<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management - View Products</title>
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
            max-width: 1400px;
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
        .btn-warning {
            background: #f39c12;
            color: white;
        }
        .btn-danger {
            background: #e74c3c;
            color: white;
        }
        .toolbar {
            background: #f8f9fa;
            padding: 20px 30px;
            border-bottom: 2px solid #e9ecef;
        }
        .search-form {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            align-items: center;
        }
        .search-form input[type="text"],
        .search-form select {
            padding: 10px 15px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
        }
        .search-form input[type="text"] {
            width: 250px;
        }
        .search-form select {
            background: white;
            min-width: 150px;
        }
        .search-form input:focus,
        .search-form select:focus {
            outline: none;
            border-color: #667eea;
        }
        .alert {
            padding: 12px 20px;
            margin: 20px 30px;
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
        .table-container {
            padding: 30px;
            overflow-x: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        thead {
            background: #f8f9fa;
        }
        th {
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #2c3e50;
            border-bottom: 2px solid #dee2e6;
        }
        td {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
            color: #495057;
        }
        tbody tr:hover {
            background: #f8f9fa;
        }
        .product-image {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 6px;
        }
        .badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
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
            font-weight: 600;
            color: #27ae60;
        }
        .actions {
            display: flex;
            gap: 8px;
        }
        .btn-sm {
            padding: 6px 12px;
            font-size: 13px;
        }
        .no-data {
            text-align: center;
            padding: 60px 30px;
            color: #6c757d;
            font-size: 16px;
        }
        .stats {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
        }
        .stat-badge {
            background: white;
            border: 2px solid #e9ecef;
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 14px;
            color: #495057;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="page-header">
            <h1>📦 Product Management</h1>
            <a href="${pageContext.request.contextPath}/management/products/create" 
               class="btn btn-success">
                + Add New Product
            </a>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <div class="toolbar">
            <div class="stats">
                <div class="stat-badge">
                    <strong>${products.size()}</strong> Products Found
                </div>
            </div>
            
            <form method="GET" action="${pageContext.request.contextPath}/management/products" 
                  class="search-form">
                <input type="text" 
                       name="keyword" 
                       placeholder="Search by name, model, brand..."
                       value="${keyword}">
                
                <select name="categoryId">
                    <option value="">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id}" 
                                ${selectedCategory == cat.id ? 'selected' : ''}>
                            ${cat.name}
                        </option>
                    </c:forEach>
                </select>
                
                <select name="status">
                    <option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>
                        All Status
                    </option>
                    <option value="ACTIVE" ${selectedStatus == 'ACTIVE' ? 'selected' : ''}>
                        Active
                    </option>
                    <option value="DISCONTINUED" ${selectedStatus == 'DISCONTINUED' ? 'selected' : ''}>
                        Discontinued
                    </option>
                </select>
                
                <button type="submit" class="btn btn-primary">🔍 Search</button>
                <a href="${pageContext.request.contextPath}/management/products" 
                   class="btn" style="background: #95a5a6; color: white;">
                    Clear
                </a>
            </form>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty products}">
                    <div class="no-data">
                        <p>No products found. Try adjusting your search criteria.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Image</th>
                                <th>Product Name</th>
                                <th>Category</th>
                                <th>Model</th>
                                <th>Brand</th>
                                <th>Price</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td>${product.id}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty product.imageUrl}">
                                                <img src="${product.imageUrl}" 
                                                     alt="${product.name}" 
                                                     class="product-image">
                                            </c:when>
                                            <c:otherwise>
                                                <div style="width: 60px; height: 60px; background: #e9ecef; 
                                                            border-radius: 6px; display: flex; 
                                                            align-items: center; justify-content: center;">
                                                    📦
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><strong>${product.name}</strong></td>
                                    <td>${product.categoryName}</td>
                                    <td>${product.model != null ? product.model : '-'}</td>
                                    <td>${product.brand != null ? product.brand : '-'}</td>
                                    <td class="price">
                                        <c:choose>
                                            <c:when test="${product.basePrice != null}">
                                                <fmt:formatNumber value="${product.basePrice}" 
                                                                 type="currency" 
                                                                 currencySymbol="$"/>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge ${product.status == 'ACTIVE' ? 'badge-active' : 'badge-discontinued'}">
                                            ${product.status}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/management/products/detail?id=${product.id}" 
                                               class="btn btn-primary btn-sm">
                                                View/Edit
                                            </a>
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
