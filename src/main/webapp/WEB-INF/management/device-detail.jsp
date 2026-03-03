<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Device Details - CMMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: #f5f7fa;
            color: #2c3e50;
        }

        .main-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background-color: #ffe6e6;
            color: #d63031;
            border: 1px solid #ff7675;
        }

        /* Detail Card with Blue Header */
        .detail-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            border: 1px solid #e1e8ed;
        }

        .card-header {
            background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
            color: white;
            padding: 20px 30px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .card-header-icon {
            font-size: 24px;
        }

        .card-header-title {
            font-size: 18px;
            font-weight: 600;
        }

        .card-body {
            padding: 30px;
        }

        /* Detail Grid Layout */
        .detail-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 25px;
            margin-bottom: 25px;
        }

        .detail-item {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .detail-label {
            font-size: 12px;
            font-weight: 600;
            color: #6c757d;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .detail-value {
            font-size: 15px;
            color: #2c3e50;
            font-weight: 500;
        }

        .detail-value-large {
            font-size: 20px;
            color: #2c3e50;
            font-weight: 600;
        }

        /* Status Badges */
        .status-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            min-width: 100px;
        }

        .status-active {
            background-color: #4caf50;
            color: white;
        }

        .status-available {
            background-color: #4caf50;
            color: white;
        }

        .status-sold {
            background-color: #2196f3;
            color: white;
        }

        .status-maintenance {
            background-color: #ff9800;
            color: white;
        }

        .status-decommissioned {
            background-color: #f44336;
            color: white;
        }

        /* Category Badge */
        .category-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 6px 14px;
            background-color: #17a2b8;
            color: white;
            border-radius: 16px;
            font-size: 13px;
            font-weight: 500;
        }

        .category-icon {
            font-size: 14px;
        }

        /* Buttons */
        .action-buttons {
            display: flex;
            gap: 12px;
            margin-top: 30px;
            padding-top: 25px;
            border-top: 1px solid #e9ecef;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
        }

        .btn-primary {
            background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(33, 150, 243, 0.4);
        }

        .btn-warning {
            background-color: #ffc107;
            color: #000;
        }

        .btn-warning:hover {
            background-color: #ffb300;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
        }

        /* Sub Device Counter */
        .sub-device-counter {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 10px 20px;
            background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
            color: white;
            border-radius: 25px;
            font-size: 14px;
            font-weight: 600;
            min-width: 120px;
        }

        .sub-device-icon {
            font-size: 18px;
        }

        .empty-value {
            color: #95a5a6;
            font-style: italic;
        }

        @media (max-width: 768px) {
            .detail-grid {
                grid-template-columns: 1fr;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
            font-weight: 600;
            text-transform: uppercase;
        }

        .status-available {
            background-color: #d4edda;
            color: #155724;
        }

        .status-sold {
            background-color: #cce5ff;
            color: #004085;
        }

        .status-maintenance {
            background-color: #fff3cd;
            color: #856404;
        }

        .status-decommissioned {
            background-color: #f8d7da;
            color: #721c24;
        }

        .condition-excellent {
            background-color: #d4edda;
            color: #155724;
        }

        .condition-good {
            background-color: #cce5ff;
            color: #004085;
        }

        .condition-fair {
            background-color: #fff3cd;
            color: #856404;
        }

        .quick-actions {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
        }

        .quick-actions h3 {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 15px;
            color: #2c3e50;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .status-form {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-top: 15px;
        }

        .form-control {
            padding: 10px;
            border: 1px solid #dfe6e9;
            border-radius: 8px;
            font-size: 14px;
        }

        select.form-control {
            cursor: pointer;
        }

        .delete-form {
            display: inline;
        }

        .empty-value {
            color: #95a5a6;
            font-style: italic;
        }

        @media (max-width: 768px) {
            .detail-grid {
                grid-template-columns: 1fr;
            }

            .header-actions {
                flex-direction: column;
                width: 100%;
            }

            .btn {
                width: 100%;
            }

            .action-buttons {
                flex-direction: column;
            }

            .status-form {
                flex-direction: column;
                align-items: stretch;
            }
        }
    </style>
</head>
<body>
    <!-- Include Navbar -->
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>

    <div class="main-container">
        <c:if test="${param.updated == 'true'}">
            <div class="alert alert-success">
                Device status updated successfully!
            </div>
        </c:if>

        <c:if test="${param.error == 'update_failed'}">
            <div class="alert alert-error">
                Failed to update device status. Please try again.
            </div>
        </c:if>

        <c:if test="${param.error == 'delete_failed'}">
            <div class="alert alert-error">
                Failed to delete device. Please try again.
            </div>
        </c:if>

        <div class="detail-card">
            <div class="card-header">
                <span class="card-header-icon">ℹ️</span>
                <span class="card-header-title">Chi tiết Thiết bị</span>
            </div>
            
            <div class="card-body">
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">ID THIẾT BỊ</div>
                        <div class="detail-value-large">#${device.id}</div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label">TRẠNG THÁI</div>
                        <div class="detail-value">
                            <c:choose>
                                <c:when test="${device.status == 'AVAILABLE'}">
                                    <span class="status-badge status-active">Hoạt động</span>
                                </c:when>
                                <c:when test="${device.status == 'SOLD'}">
                                    <span class="status-badge status-sold">Sold</span>
                                </c:when>
                                <c:when test="${device.status == 'MAINTENANCE'}">
                                    <span class="status-badge status-maintenance">Maintenance</span>
                                </c:when>
                                <c:when test="${device.status == 'DECOMMISSIONED'}">
                                    <span class="status-badge status-decommissioned">Decommissioned</span>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">TÊN THIẾT BỊ</div>
                        <div class="detail-value">${device.serialNumber}</div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label">THỜI GIAN BẢO TRÌ (THÁNG)</div>
                        <div class="detail-value">
                            <span style="background-color: #ff9800; color: white; padding: 6px 12px; border-radius: 16px; display: inline-block;">
                                ⏰ ${device.currentLocation != null && !device.currentLocation.isEmpty() ? device.currentLocation : '6'}
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">DANH MỤC</div>
                        <div class="detail-value">
                            <span class="category-badge">
                                <span class="category-icon">🔧</span>
                                <span>${device.productName}</span>
                            </span>
                        </div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label">SỐ LƯỢNG SUB DEVICE CÒN LẠI</div>
                        <div class="detail-value">
                            <span class="sub-device-counter">
                                <span class="sub-device-icon">📦</span>
                                <span>10 sản phẩm</span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">MÔ TẢ</div>
                        <div class="detail-value">
                            <c:choose>
                                <c:when test="${not empty device.notes}">
                                    ${device.notes}
                                </c:when>
                                <c:otherwise>
                                    Máy phát điện Cummins 20kVA
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label">NGÀY TẠO</div>
                        <div class="detail-value">
                            📅 <fmt:formatDate value="${device.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" />
                        </div>
                    </div>
                </div>

                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/management/devices/sub-devices?deviceId=${device.id}" class="btn btn-primary">
                        📋 Xem danh sách Sub Device còn lại
                    </a>
                    <a href="${pageContext.request.contextPath}/management/devices/form?id=${device.id}" 
                       class="btn btn-warning">
                        ✏️ Chỉnh sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/management/devices" 
                       class="btn btn-secondary">
                        ← Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
