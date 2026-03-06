<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Product Detail</title>
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
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            padding: 10px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s;
        }
        .back-link:hover {
            background: rgba(255,255,255,0.3);
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }
        .product-detail {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 40px;
            padding: 40px;
        }
        @media (max-width: 768px) {
            .product-detail {
                grid-template-columns: 1fr;
                gap: 30px;
            }
        }
        .product-image-section {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .product-image-large {
            width: 100%;
            max-width: 500px;
            height: auto;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            object-fit: cover;
        }
        .product-image-placeholder {
            width: 100%;
            max-width: 500px;
            height: 500px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 120px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
        .product-info-section {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        .category-badge {
            display: inline-block;
            padding: 6px 16px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            width: fit-content;
        }
        .product-title {
            font-size: 36px;
            font-weight: 700;
            color: #2c3e50;
            margin-top: 10px;
            line-height: 1.2;
        }
        .product-price {
            font-size: 42px;
            font-weight: 700;
            color: #667eea;
            margin: 15px 0;
        }
        .info-section {
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        .info-section h3 {
            font-size: 18px;
            color: #495057;
            margin-bottom: 15px;
            font-weight: 600;
        }
        .info-row {
            display: flex;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 600;
            color: #495057;
            min-width: 120px;
        }
        .info-value {
            color: #6c757d;
            flex: 1;
        }
        .description-section {
            margin-top: 20px;
        }
        .description-section h3 {
            font-size: 20px;
            color: #2c3e50;
            margin-bottom: 15px;
            font-weight: 600;
        }
        .description-content {
            color: #495057;
            line-height: 1.6;
            font-size: 15px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .specifications-section {
            margin-top: 20px;
        }
        .specifications-section h3 {
            font-size: 20px;
            color: #2c3e50;
            margin-bottom: 15px;
            font-weight: 600;
        }
        .specifications-content {
            color: #495057;
            line-height: 1.8;
            font-size: 15px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            white-space: pre-wrap;
        }
        .status-badge {
            display: inline-block;
            padding: 8px 16px;
            background: #27ae60;
            color: white;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
            text-transform: uppercase;
        }
        .divider {
            height: 1px;
            background: #e9ecef;
            margin: 20px 0;
        }
        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        .btn {
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            flex: 1;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background: #5a6268;
        }
        .empty-value {
            color: #adb5bd;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div style="max-width: 1200px; margin: 0 auto;">
        <a href="${pageContext.request.contextPath}/products" class="back-link">
            ← Back to Products
        </a>
    </div>

    <div class="container">
        <div class="product-detail">
            <!-- Product Image Section -->
            <div class="product-image-section">
                <c:choose>
                    <c:when test="${not empty product.imageUrl}">
                        <img src="${product.imageUrl}" 
                             alt="${product.name}" 
                             class="product-image-large"
                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                        <div class="product-image-placeholder" style="display:none;">📦</div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-image-placeholder">📦</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Product Info Section -->
            <div class="product-info-section">
                <div class="category-badge">${product.categoryName}</div>
                
                <h1 class="product-title">${product.name}</h1>
                
                <div class="status-badge">✓ Available</div>

                <c:if test="${not empty product.basePrice}">
                    <div class="product-price">
                        <fmt:formatNumber value="${product.basePrice}" 
                                         type="currency" 
                                         currencySymbol="$"/>
                    </div>
                </c:if>

                <div class="divider"></div>

                <!-- Basic Information -->
                <div class="info-section">
                    <h3>📋 Basic Information</h3>
                    
                    <c:if test="${not empty product.brand}">
                        <div class="info-row">
                            <div class="info-label">Brand:</div>
                            <div class="info-value">${product.brand}</div>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty product.model}">
                        <div class="info-row">
                            <div class="info-label">Model:</div>
                            <div class="info-value">${product.model}</div>
                        </div>
                    </c:if>
                    
                    <div class="info-row">
                        <div class="info-label">Category:</div>
                        <div class="info-value">${product.categoryName}</div>
                    </div>
                    
                    <div class="info-row">
                        <div class="info-label">Product ID:</div>
                        <div class="info-value">#${product.id}</div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                        Back to Catalog
                    </a>
                    <button class="btn btn-primary" onclick="alert('Contact us to purchase this product!')">
                        Contact to Purchase
                    </button>
                </div>
            </div>
        </div>

        <!-- Full Width Sections -->
        <div style="padding: 0 40px 40px 40px;">
            <!-- Description Section -->
            <c:if test="${not empty product.description}">
                <div class="description-section">
                    <h3>📝 Description</h3>
                    <div class="description-content">
                        ${product.description}
                    </div>
                </div>
            </c:if>

            <!-- Specifications Section -->
            <c:if test="${not empty product.specifications}">
                <div class="specifications-section">
                    <h3>🔧 Specifications</h3>
                    <div class="specifications-content">${product.specifications}</div>
                </div>
            </c:if>

            <c:if test="${empty product.description && empty product.specifications}">
                <div class="description-section">
                    <div class="description-content empty-value">
                        No detailed description or specifications available for this product.
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
