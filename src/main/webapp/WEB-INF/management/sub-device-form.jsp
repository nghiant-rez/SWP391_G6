<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Sub Device - CMMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: #f5f7fa;
            color: #2c3e50;
        }

        .main-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        /* Form Card */
        .form-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            border: 1px solid #e1e8ed;
        }

        /* Blue Header */
        .form-header {
            background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
            color: white;
            padding: 20px 30px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .form-header-left {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .form-header-icon {
            font-size: 24px;
        }

        .form-header-title {
            font-size: 18px;
            font-weight: 600;
        }

        .btn-back {
            padding: 8px 16px;
            background-color: rgba(255, 255, 255, 0.2);
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
            border: 1px solid rgba(255, 255, 255, 0.3);
        }

        .btn-back:hover {
            background-color: rgba(255, 255, 255, 0.3);
        }

        /* Info Section */
        .info-section {
            background-color: #e3f2fd;
            padding: 20px 30px;
            border-bottom: 1px solid #e1e8ed;
        }

        .info-title {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
            font-weight: 600;
            color: #1976d2;
            margin-bottom: 12px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .info-label {
            font-size: 12px;
            color: #6c757d;
            font-weight: 500;
        }

        .info-value {
            font-size: 14px;
            color: #2c3e50;
            font-weight: 600;
        }

        .category-badge {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 4px 10px;
            background-color: #17a2b8;
            color: white;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
        }

        /* Form Body */
        .form-body {
            padding: 30px;
        }

        .section-title {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #e9ecef;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #495057;
            font-size: 14px;
        }

        .form-control {
            width: 100%;
            padding: 10px 15px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s, box-shadow 0.3s;
            box-sizing: border-box;
            font-family: inherit;
        }

        .form-control:focus {
            outline: none;
            border-color: #80bdff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }

        .form-help {
            display: flex;
            align-items: center;
            gap: 6px;
            padding: 12px;
            background-color: #f0f8ff;
            border-left: 4px solid #2196f3;
            border-radius: 4px;
            font-size: 13px;
            color: #495057;
            margin-top: 8px;
        }

        .help-icon {
            font-size: 16px;
        }

        /* Note Section */
        .note-section {
            background-color: #e8f5e9;
            padding: 15px;
            border-left: 4px solid #4caf50;
            border-radius: 4px;
            margin-bottom: 25px;
        }

        .note-title {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            font-weight: 600;
            color: #2e7d32;
            margin-bottom: 6px;
        }

        .note-text {
            font-size: 13px;
            color: #1b5e20;
        }

        /* Form Actions */
        .form-actions {
            display: flex;
            justify-content: center;
            gap: 15px;
            padding-top: 25px;
            border-top: 1px solid #e9ecef;
        }

        .btn {
            padding: 12px 40px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(33, 150, 243, 0.4);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
        }

        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-error {
            background-color: #ffe6e6;
            color: #d63031;
            border: 1px solid #ff7675;
        }

        @media (max-width: 768px) {
            .info-grid {
                grid-template-columns: 1fr;
            }

            .form-actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>

    <div class="main-container">
        <div class="form-card">
            <!-- Blue Header -->
            <div class="form-header">
                <div class="form-header-left">
                    <span class="form-header-icon">➕</span>
                    <span class="form-header-title">Thêm Sub Device mới</span>
                </div>
                <a href="${pageContext.request.contextPath}/management/devices/sub-devices?deviceId=${param.deviceId}" class="btn-back">
                    ← Quay lại
                </a>
            </div>

            <!-- Device Info Section -->
            <div class="info-section">
                <div class="info-title">
                    <span>ℹ️</span>
                    <span>Thông tin Thiết bị</span>
                </div>
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">ID Thiết bị</div>
                        <div class="info-value">#${device.id != null ? device.id : param.deviceId}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Tên thiết bị</div>
                        <div class="info-value">${device.serialNumber != null ? device.serialNumber : 'Cummins G20'}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Danh mục</div>
                        <div class="info-value">
                            <span class="category-badge">
                                🔧 ${device.productName != null ? device.productName : 'Cummins'}
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Form Body -->
            <div class="form-body">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">
                        ${errorMessage}
                    </div>
                </c:if>

                <h3 class="section-title">Số Seri</h3>

                <form method="post" action="${pageContext.request.contextPath}/management/devices/sub-devices/form">
                    <input type="hidden" name="deviceId" value="${param.deviceId}">

                    <div class="form-group">
                        <label for="serialNumbers">Nhập số seri (Ví dụ: SERI001, DEV001-001)</label>
                        <input type="text" 
                               id="serialNumbers" 
                               name="serialNumbers" 
                               class="form-control" 
                               placeholder="Nhập số seri..."
                               required>
                        <div class="form-help">
                            <span class="help-icon">💡</span>
                            <span>Số seri phải là duy nhất trong hệ thống. Ví dụ: SERI001, DEV001-001, ABC-12345</span>
                        </div>
                    </div>

                    <div class="note-section">
                        <div class="note-title">
                            <span>💡</span>
                            <span>Lưu ý:</span>
                        </div>
                        <div class="note-text">
                            • Số seri phải là duy nhất, không được trùng với các Sub Device khác<br>
                            • Khuyến nghị sử dụng format: [PREFIX][NUMBER] (ví dụ: SERI001, DEV001-001)<br>
                            • Số seri không thể thay đổi sau khi tạo
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            💾 Lưu Sub Device
                        </button>
                        <a href="${pageContext.request.contextPath}/management/devices/sub-devices?deviceId=${param.deviceId}" 
                           class="btn btn-secondary">
                            🚫 Hủy
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
