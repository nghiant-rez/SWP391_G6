<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Catalog</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .header h1 {
            font-size: 36px;
            margin-bottom: 10px;
            font-weight: 700;
        }
        .header p {
            font-size: 16px;
            opacity: 0.9;
        }
        .search-section {
            padding: 30px;
            background: #f8f9fa;
            border-bottom: 2px solid #e9ecef;
        }
        .search-form {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            align-items: center;
            justify-content: center;
        }
        .search-form input[type="text"],
        .search-form select {
            padding: 12px 20px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 15px;
            transition: all 0.3s;
        }
        .search-form input[type="text"] {
            min-width: 300px;
        }
        .search-form select {
            background: white;
            min-width: 200px;
            cursor: pointer;
        }
        .search-form input:focus,
        .search-form select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 600;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .products-grid {
            padding: 30px;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 25px;
        }
        .product-card {
            background: white;
            border: 2px solid #e9ecef;
            border-radius: 12px;
            overflow: hidden;
            transition: all 0.3s;
            cursor: pointer;
        }
        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
            border-color: #667eea;
        }
        .product-image {
            width: 100%;
            height: 220px;
            object-fit: cover;
            background: #f8f9fa;
        }
        .product-image-placeholder {
            width: 100%;
            height: 220px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 72px;
        }
        .product-info {
            padding: 20px;
        }
        .product-category {
            display: inline-block;
            padding: 4px 12px;
            background: #e9ecef;
            color: #495057;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 10px;
            text-transform: uppercase;
        }
        .product-name {
            font-size: 20px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 8px;
            min-height: 48px;
        }
        .product-details {
            font-size: 14px;
            color: #6c757d;
            margin-bottom: 15px;
        }
        .product-price {
            font-size: 24px;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 15px;
        }
        .btn-view {
            width: 100%;
            padding: 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: block;
            text-align: center;
        }
        .btn-view:hover {
            background: #5568d3;
        }
        .empty-state {
            text-align: center;
            padding: 80px 20px;
            color: #6c757d;
        }
        .empty-state-icon {
            font-size: 72px;
            margin-bottom: 20px;
        }
        .empty-state h2 {
            font-size: 24px;
            margin-bottom: 10px;
            color: #495057;
        }
        .empty-state p {
            font-size: 16px;
        }
        .home-link {
            position: absolute;
            top: 20px;
            left: 20px;
            padding: 10px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s;
        }
        .home-link:hover {
            background: rgba(255,255,255,0.3);
        }
        .results-count {
            padding: 20px 30px;
            background: #f8f9fa;
            border-bottom: 1px solid #e9ecef;
            color: #495057;
            font-size: 15px;
        }
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/home.jsp" class="home-link">← Trang chủ</a>

    <div class="container">
        <div class="header">
            <h1>📦 Product Catalog</h1>
            <p>Browse our available products</p>
        </div>

        <div class="search-section">
            <form method="get" action="${pageContext.request.contextPath}/products" class="search-form">
                <input type="text" 
                       name="keyword" 
                       placeholder="Search by product name..." 
                       value="${keyword}">
                
                <select name="categoryId">
                    <option value="">All Categories</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}" 
                                ${selectedCategory == category.id ? 'selected' : ''}>
                            ${category.name}
                        </option>
                    </c:forEach>
                </select>
                
                <button type="submit" class="btn btn-primary">🔍 Search</button>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">Clear</a>
            </form>
        </div>

        <c:if test="${not empty products}">
            <div class="results-count">
                <strong>${products.size()}</strong> product(s) found
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty products}">
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h2>No Products Found</h2>
                    <p>Try adjusting your search or filter criteria</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="products-grid">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card">
                            <c:choose>
                                <c:when test="${not empty product.imageUrl}">
                                    <img src="${product.imageUrl}" 
                                         alt="${product.name}" 
                                         class="product-image"
                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                    <div class="product-image-placeholder" style="display:none;">📦</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="product-image-placeholder">📦</div>
                                </c:otherwise>
                            </c:choose>
                            
                            <div class="product-info">
                                <div class="product-category">${product.categoryName}</div>
                                <div class="product-name">${product.name}</div>
                                
                                <div class="product-details">
                                    <c:if test="${not empty product.brand}">
                                        <strong>Brand:</strong> ${product.brand}<br>
                                    </c:if>
                                    <c:if test="${not empty product.model}">
                                        <strong>Model:</strong> ${product.model}
                                    </c:if>
                                </div>
                                
                                <c:if test="${not empty product.basePrice}">
                                    <div class="product-price">
                                        <fmt:formatNumber value="${product.basePrice}" 
                                                         type="currency" 
                                                         currencySymbol="$"/>
                                    </div>
                                </c:if>
                                
                                <a href="${pageContext.request.contextPath}/products/detail?id=${product.id}" 
                                   class="btn-view">
                                    View Details
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
