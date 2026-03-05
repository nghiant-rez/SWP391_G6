<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Device List - CMMS</title>
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

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .page-header h1 {
            font-size: 28px;
            color: #2c3e50;
            font-weight: 600;
        }

        .btn-add {
            background-color: #e9ecef;
            color: #495057;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s ease;
            display: inline-block;
            border: 1px solid #ddd;
        }

        .btn-add:hover {
            background-color: #dee2e6;
            transform: translateY(-1px);
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
        }

        .table-title {
            font-size: 20px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0 0 20px 0;
            text-align: center;
        }

        /* Filters Row */
        .filters-row {
            display: flex;
            gap: 15px;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .filter-group select {
            padding: 8px 35px 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 14px;
            background-color: white;
            cursor: pointer;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23333' d='M6 9L1 4h10z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 10px center;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            min-width: 180px;
        }

        .filter-group select:focus {
            outline: none;
            border-color: #80bdff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background-color: #f8f9fa;
            border-bottom: 2px solid #dee2e6;
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

        tbody tr:last-child {
            border-bottom: none;
        }

        tbody td {
            padding: 18px 20px;
            font-size: 14px;
            color: #2c3e50;
            vertical-align: middle;
        }

        tbody td:first-child {
            font-weight: 500;
        }

        /* Status Badge */
        .status-badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }

        .status-badge.available {
            background-color: #d1f4e0;
            color: #0a7d3e;
        }

        .status-badge.sold {
            background-color: #d1ecf1;
            color: #0c5460;
        }

        .status-badge.maintenance {
            background-color: #fff3cd;
            color: #856404;
        }

        .status-badge.decommissioned {
            background-color: #f8d7da;
            color: #721c24;
        }

        /* Action Buttons */
        .action-buttons {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .btn-action {
            padding: 6px 16px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 13px;
            font-weight: 500;
            text-align: center;
            transition: all 0.2s ease;
            border: 1px solid;
            display: inline-block;
        }

        .btn-edit {
            background-color: #fff;
            color: #495057;
            border-color: #ced4da;
        }

        .btn-edit:hover {
            background-color: #e9ecef;
            border-color: #adb5bd;
        }

        .btn-delete {
            background-color: #fff;
            color: #dc3545;
            border-color: #dc3545;
        }

        .btn-delete:hover {
            background-color: #dc3545;
            color: white;
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

        /* Empty State */
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

        .empty-state-text {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .empty-state-subtext {
            font-size: 14px;
            color: #999;
        }

        @media (max-width: 768px) {
            .filters-row {
                flex-direction: column;
                align-items: stretch;
            }

            .filter-group {
                flex-direction: column;
                align-items: stretch;
            }

            .filter-group select {
                width: 100%;
            }

            .action-buttons {
                flex-direction: row;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <div class="main-container">
        <c:if test="${param.success == 'true'}">
            <div class="alert alert-success">
                Device saved successfully!
            </div>
        </c:if>

        <c:if test="${param.deleted == 'true'}">
            <div class="alert alert-success">
                Device deleted successfully!
            </div>
        </c:if>

        <!-- Devices Table -->
        <div class="table-container">
            <div class="table-header">
                <h2 class="table-title">List Device</h2>
                
                <div style="text-align: right; margin-bottom: 20px; display: flex; gap: 10px; justify-content: flex-end;">
                    <a href="${pageContext.request.contextPath}/management/devices/deleted" 
                       style="padding: 10px 20px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 6px; font-size: 14px; font-weight: 500; border: 1px solid #6c757d;">
                        🗑️ Thiết bị đã xóa
                    </a>
                    <a href="${pageContext.request.contextPath}/management/devices/form" class="btn-add">
                        Add Device
                    </a>
                </div>

                <!-- Filters -->
                <form method="get" action="${pageContext.request.contextPath}/management/devices" style="margin: 0;">
                    <div class="filters-row">
                        <div class="filter-group">
                            <select id="filterName" name="keyword" onchange="this.form.submit()">
                                <option value="">Name</option>
                                <c:forEach var="device" items="${devices}">
                                    <option value="${device.serialNumber}" ${keyword == device.serialNumber ? 'selected' : ''}>
                                        ${device.serialNumber}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="filter-group">
                            <select id="filterMaintenance" name="status" onchange="this.form.submit()">
                                <option value="">Maintaince_time</option>
                                <option value="MAINTENANCE" ${selectedStatus == 'MAINTENANCE' ? 'selected' : ''}>Under Maintenance</option>
                                <option value="AVAILABLE" ${selectedStatus == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                                <option value="SOLD" ${selectedStatus == 'SOLD' ? 'selected' : ''}>Sold</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <select id="filterCategory" name="productId" onchange="this.form.submit()">
                                <option value="">Category</option>
                                <c:forEach var="product" items="${products}">
                                    <option value="${product.id}" 
                                            ${selectedProductId == product.id.toString() ? 'selected' : ''}>
                                        ${product.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty devices}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📦</div>
                        <div class="empty-state-text">No devices found</div>
                        <div class="empty-state-subtext">
                            <c:choose>
                                <c:when test="${not empty keyword || not empty selectedProductId || not empty selectedStatus}">
                                    Try adjusting your filters
                                </c:when>
                                <c:otherwise>
                                    No devices have been added yet
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Image</th>
                                <th>Maintaince_time</th>
                                <th>Created At</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="device" items="${devices}">
                                <tr>
                                    <td>#${device.id}</td>
                                    <td><strong>${device.serialNumber}</strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty device.notes}">
                                                ${device.notes.length() > 40 ? device.notes.substring(0, 40).concat('...') : device.notes}
                                            </c:when>
                                            <c:otherwise>
                                                ${device.productName}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>-</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${device.status == 'AVAILABLE'}">
                                                <span class="status-badge available">Available</span>
                                            </c:when>
                                            <c:when test="${device.status == 'SOLD'}">
                                                <span class="status-badge sold">Sold</span>
                                            </c:when>
                                            <c:when test="${device.status == 'MAINTENANCE'}">
                                                <span class="status-badge maintenance">Maintenance</span>
                                            </c:when>
                                            <c:when test="${device.status == 'DECOMMISSIONED'}">
                                                <span class="status-badge decommissioned">Decommissioned</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${device.createdAt != null}">
                                            <%= ((com.swp391.group6.model.Device)pageContext.findAttribute("device")).getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) %>
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/management/devices/form?id=${device.id}" 
                                               class="btn-action btn-edit">Edit Device</a>
                                            <a href="${pageContext.request.contextPath}/management/devices/detail?id=${device.id}" 
                                               class="btn-action btn-delete">Delete Device</a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <div class="pagination">
                        <a href="#" class="page-link active">1</a>
                        <a href="#" class="page-link">2</a>
                        <a href="#" class="page-link">3</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
