<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thiết Bị Đã Xóa - CMMS</title>
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
            max-width: 1400px;
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

        /* Table Container */
        .table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            overflow: hidden;
            border: 1px solid #e1e8ed;
        }

        .table-header {
            padding: 25px 30px;
            border-bottom: 2px solid #e1e8ed;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .table-title-section {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .table-icon {
            font-size: 24px;
        }

        .table-title {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0;
        }

        .btn-back {
            padding: 8px 16px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background-color: #0056b3;
        }

        /* Search Section */
        .search-section {
            padding: 20px 30px;
            border-bottom: 1px solid #e1e8ed;
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .search-input {
            flex: 1;
            max-width: 600px;
            display: flex;
            gap: 10px;
        }

        .search-select {
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            min-width: 180px;
        }

        .search-text {
            flex: 1;
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
        }

        .btn-search {
            padding: 8px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-search:hover {
            background-color: #0056b3;
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background-color: #f8f9fa;
        }

        thead th {
            padding: 15px 20px;
            text-align: left;
            font-weight: 600;
            font-size: 13px;
            color: #495057;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            border-bottom: 2px solid #dee2e6;
        }

        tbody tr {
            border-bottom: 1px solid #f0f0f0;
            transition: background-color 0.2s ease;
        }

        tbody tr:hover {
            background-color: #f8f9fa;
        }

        tbody td {
            padding: 18px 20px;
            font-size: 14px;
            color: #2c3e50;
        }

        /* Status Badge */
        .status-badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 16px;
            font-size: 12px;
            font-weight: 600;
            background-color: #ffe6e6;
            color: #d63031;
        }

        /* Action Buttons */
        .action-buttons {
            display: flex;
            gap: 8px;
        }

        .btn-action {
            padding: 6px 14px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 13px;
            font-weight: 500;
            text-align: center;
            transition: all 0.2s ease;
            border: 1px solid;
            display: inline-block;
        }

        .btn-view {
            background-color: #fff;
            color: #6c757d;
            border-color: #ced4da;
        }

        .btn-view:hover {
            background-color: #e9ecef;
        }

        .btn-restore {
            background-color: #28a745;
            color: white;
            border-color: #28a745;
        }

        .btn-restore:hover {
            background-color: #218838;
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            gap: 5px;
        }

        .page-link {
            padding: 8px 12px;
            border: 1px solid #dee2e6;
            color: #007bff;
            text-decoration: none;
            border-radius: 4px;
            transition: all 0.2s;
            font-size: 14px;
        }

        .page-link:hover {
            background-color: #e9ecef;
        }

        .page-link.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }

        .empty-state {
            padding: 60px 20px;
            text-align: center;
            color: #6c757d;
        }

        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.3;
        }

        @media (max-width: 768px) {
            .search-section {
                flex-direction: column;
                align-items: stretch;
            }

            .search-input {
                flex-direction: column;
                max-width: 100%;
            }

            .action-buttons {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <div class="main-container">
        <c:if test="${param.restored == 'true'}">
            <div class="alert alert-success">
                Device restored successfully!
            </div>
        </c:if>

        <div class="table-container">
            <div class="table-header">
                <div class="table-title-section">
                    <span class="table-icon">🗑️</span>
                    <h2 class="table-title">Thiết Bị Đã Xóa</h2>
                </div>
                <a href="${pageContext.request.contextPath}/management/devices" class="btn-back">
                    ← Quay lại Danh sách
                </a>
            </div>

            <div class="search-section">
                <form method="get" action="${pageContext.request.contextPath}/management/devices/deleted" style="display: flex; gap: 15px; width: 100%;">
                    <div class="search-input">
                        <select name="category" class="search-select">
                            <option value="">-- Tất cả Danh mục --</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}">${product.name}</option>
                            </c:forEach>
                        </select>
                        <input type="text" name="keyword" class="search-text" placeholder="Tìm kiếm theo tên...">
                    </div>
                    <button type="submit" class="btn-search">Tìm Kiếm</button>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty deletedDevices}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📦</div>
                        <div style="font-size: 18px; margin-bottom: 10px;">Không có thiết bị đã xóa</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>TÊN</th>
                                <th>MÔ TẢ</th>
                                <th>THƯƠNG HIỆU</th>
                                <th>THỜI GIAN BẢO TRÌ</th>
                                <th>NGÀY TẠO</th>
                                <th>TRẠNG THÁI</th>
                                <th>HÀNH ĐỘNG</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Sample data - replace with actual JSP iteration -->
                            <tr>
                                <td>64</td>
                                <td><strong>hgfd</strong></td>
                                <td>-</td>
                                <td>Cummins</td>
                                <td>7 tháng</td>
                                <td>2025-12-11T21:35:11Z</td>
                                <td><span class="status-badge">Đã xóa</span></td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="#" class="btn-action btn-view">Xem Chi tiết</a>
                                        <a href="#" class="btn-action btn-restore" onclick="return confirm('Khôi phục thiết bị này?');">Khôi phục</a>
                                    </div>
                                </td>
                            </tr>
                            <c:forEach var="device" items="${deletedDevices}">
                                <tr>
                                    <td>${device.id}</td>
                                    <td><strong>${device.serialNumber}</strong></td>
                                    <td>${device.notes != null ? device.notes : '-'}</td>
                                    <td>${device.productName}</td>
                                    <td>${device.currentLocation != null ? device.currentLocation : '6'} tháng</td>
                                    <td><fmt:formatDate value="${device.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss'Z'" /></td>
                                    <td><span class="status-badge">Đã xóa</span></td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/management/devices/detail?id=${device.id}" 
                                               class="btn-action btn-view">Xem Chi tiết</a>
                                            <a href="${pageContext.request.contextPath}/management/devices/restore?id=${device.id}" 
                                               class="btn-action btn-restore" 
                                               onclick="return confirm('Khôi phục thiết bị này?');">Khôi phục</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="pagination">
                        <a href="#" class="page-link">1</a>
                        <a href="#" class="page-link">2</a>
                        <a href="#" class="page-link active">3</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
