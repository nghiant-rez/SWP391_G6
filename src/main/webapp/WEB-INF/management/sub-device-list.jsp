<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Sub Device - CMMS</title>
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

        /* Header Card */
        .header-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            padding: 25px 30px;
            margin-bottom: 25px;
            border: 1px solid #e1e8ed;
        }

        .header-title {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
        }

        .header-icon {
            font-size: 24px;
        }

        .header-subtitle {
            font-size: 14px;
            color: #6c757d;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .device-info {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 4px 12px;
            background-color: #e3f2fd;
            border-radius: 16px;
            font-weight: 500;
            color: #1976d2;
        }

        /* Filter Section */
        .filter-section {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            padding: 25px 30px;
            margin-bottom: 25px;
            border: 1px solid #e1e8ed;
        }

        .filter-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .filter-title {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
        }

        .btn-add {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .btn-add:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }

        .btn-back {
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background-color: #5a6268;
        }

        .filter-row {
            display: flex;
            gap: 15px;
            align-items: center;
            justify-content: space-between;
        }

        .filter-inputs {
            display: flex;
            gap: 12px;
            flex: 1;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .filter-label {
            font-size: 14px;
            font-weight: 500;
            color: #495057;
            white-space: nowrap;
        }

        .filter-input {
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            min-width: 200px;
        }

        .filter-date {
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            width: 180px;
        }

        .filter-buttons {
            display: flex;
            gap: 10px;
        }

        .btn-filter {
            padding: 8px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-filter:hover {
            background-color: #0056b3;
        }

        .btn-reset {
            padding: 8px 20px;
            background-color: #fff;
            color: #6c757d;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-reset:hover {
            background-color: #e9ecef;
        }

        /* Table Container */
        .table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            overflow: hidden;
            border: 1px solid #e1e8ed;
        }

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
            padding: 6px 12px;
            border-radius: 16px;
            font-size: 12px;
            font-weight: 600;
        }

        .status-active {
            background-color: #d1f4e0;
            color: #0a7d3e;
        }

        /* Action Button */
        .btn-delete {
            padding: 6px 16px;
            background-color: #dc3545;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s;
        }

        .btn-delete:hover {
            background-color: #c82333;
        }

        .empty-state {
            padding: 60px 20px;
            text-align: center;
            color: #6c757d;
        }

        @media (max-width: 768px) {
            .filter-row {
                flex-direction: column;
                align-items: stretch;
            }

            .filter-inputs {
                flex-direction: column;
            }

            .filter-buttons {
                width: 100%;
            }

            .btn-filter, .btn-reset {
                flex: 1;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <div class="main-container">
        <!-- Header Card -->
        <div class="header-card">
            <div class="header-title">
                <span class="header-icon">📦</span>
                <span>Danh sách Sub Device còn lại</span>
            </div>
            <div class="header-subtitle">
                Thiết bị: <span class="device-info">
                    <strong>Cummins G20</strong>
                </span>
                | Trạng thái: <span class="device-info">Có 10 sản phẩm</span>
            </div>
        </div>

        <!-- Filter Section -->
        <div class="filter-section">
            <div class="filter-header">
                <h3 class="filter-title">Tìm kiếm theo thời gian</h3>
                <div style="display: flex; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/management/devices/sub-devices/form?deviceId=${param.deviceId}" class="btn-add">
                        ➕ Thêm Sub Device
                    </a>
                    <a href="${pageContext.request.contextPath}/management/devices/detail?id=${param.deviceId}" class="btn-back">
                        ← Quay lại chi tiết
                    </a>
                </div>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/management/devices/sub-devices">
                <input type="hidden" name="deviceId" value="${param.deviceId}">
                <div class="filter-row">
                    <div class="filter-inputs">
                        <div class="filter-group">
                            <label class="filter-label">Tìm kiếm theo Seri:</label>
                            <input type="text" name="serialFilter" class="filter-input" placeholder="Nhập số seri...">
                        </div>
                        <div class="filter-group">
                            <label class="filter-label">Từ ngày:</label>
                            <input type="date" name="fromDate" class="filter-date">
                        </div>
                        <div class="filter-group">
                            <label class="filter-label">Đến ngày:</label>
                            <input type="date" name="toDate" class="filter-date">
                        </div>
                    </div>
                    <div class="filter-buttons">
                        <button type="submit" class="btn-filter">🔍 Lọc</button>
                        <button type="button" class="btn-reset" onclick="window.location.href='?deviceId=${param.deviceId}'">🔄 Reset</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Table -->
        <div class="table-container">
            <c:choose>
                <c:when test="${empty subDevices}">
                    <div class="empty-state">
                        <div style="font-size: 64px; margin-bottom: 20px; opacity: 0.3;">📦</div>
                        <div style="font-size: 18px; margin-bottom: 10px;">Không có sub device nào</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>SỐ SERI</th>
                                <th>TÊN THIẾT BỊ</th>
                                <th>DANH MỤC</th>
                                <th>TRẠNG THÁI</th>
                                <th>NGÀY TẠO</th>
                                <th>HÀNH ĐỘNG</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Sample Data -->
                            <tr>
                                <td><strong>PH001</strong></td>
                                <td>Cummins G20</td>
                                <td>Cummins</td>
                                <td><span class="status-badge status-active">Hoạt động</span></td>
                                <td>2025-12-11T10:44:49.87Z</td>
                                <td>
                                    <button class="btn-delete" onclick="return confirm('Xóa sub device này?');">🗑️ Xóa</button>
                                </td>
                            </tr>
                            <tr>
                                <td><strong>PH002</strong></td>
                                <td>Cummins G20</td>
                                <td>Cummins</td>
                                <td><span class="status-badge status-active">Hoạt động</span></td>
                                <td>2025-12-11T10:44:49.87Z</td>
                                <td>
                                    <button class="btn-delete" onclick="return confirm('Xóa sub device này?');">🗑️ Xóa</button>
                                </td>
                            </tr>
                            <c:forEach var="subDevice" items="${subDevices}">
                                <tr>
                                    <td><strong>${subDevice.serialNumber}</strong></td>
                                    <td>${subDevice.deviceName}</td>
                                    <td>${subDevice.category}</td>
                                    <td>
                                        <span class="status-badge status-active">Hoạt động</span>
                                    </td>
                                    <td><fmt:formatDate value="${subDevice.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" /></td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/management/devices/sub-devices/delete" style="display: inline;">
                                            <input type="hidden" name="id" value="${subDevice.id}">
                                            <input type="hidden" name="deviceId" value="${param.deviceId}">
                                            <button type="submit" class="btn-delete" onclick="return confirm('Xóa sub device này?');">🗑️ Xóa</button>
                                        </form>
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
