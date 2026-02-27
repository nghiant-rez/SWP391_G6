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

        /* Statistics Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 25px;
            border-radius: 12px;
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.2);
        }

        .stat-card.available {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }

        .stat-card.sold {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        .stat-card.maintenance {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }

        .stat-label {
            font-size: 14px;
            opacity: 0.95;
            margin-bottom: 8px;
        }

        .stat-value {
            font-size: 36px;
            font-weight: 700;
            margin: 0;
        }

        /* Filters */
        .filter-section {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            margin-bottom: 25px;
        }

        .filter-row {
            display: grid;
            grid-template-columns: 2fr 1.5fr 1.5fr auto;
            gap: 15px;
            align-items: end;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            font-size: 13px;
            font-weight: 500;
            color: #555;
            margin-bottom: 6px;
        }

        .form-group input,
        .form-group select {
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .btn-filter {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 10px 24px;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: transform 0.2s ease;
        }

        .btn-filter:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
        }

        .btn-reset {
            background: #6c757d;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            margin-left: 10px;
        }

        /* Table */
        .table-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        thead th {
            padding: 16px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        tbody tr {
            border-bottom: 1px solid #f0f0f0;
            transition: background-color 0.2s ease;
        }

        tbody tr:hover {
            background-color: #f8f9fa;
        }

        tbody td {
            padding: 16px;
            font-size: 14px;
            color: #2c3e50;
        }

        /* Status Badges */
        .status-badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .status-badge.available {
            background-color: #d4edda;
            color: #155724;
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

        /* Action Links */
        .action-link {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s ease;
        }

        .action-link:hover {
            color: #764ba2;
            text-decoration: underline;
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
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />

    <div class="main-container">
        <div class="page-header">
            <h1>Device Inventory</h1>
        </div>

        <!-- Statistics Cards -->
        <div class="stats-grid">
            <div class="stat-card available">
                <div class="stat-label">Available</div>
                <div class="stat-value">${availableCount}</div>
            </div>
            <div class="stat-card sold">
                <div class="stat-label">Sold</div>
                <div class="stat-value">${soldCount}</div>
            </div>
            <div class="stat-card maintenance">
                <div class="stat-label">Maintenance</div>
                <div class="stat-value">${maintenanceCount}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Total Devices</div>
                <div class="stat-value">${totalCount}</div>
            </div>
        </div>

        <!-- Filter Section -->
        <div class="filter-section">
            <form method="get" action="${pageContext.request.contextPath}/management/devices">
                <div class="filter-row">
                    <div class="form-group">
                        <label for="keyword">Search</label>
                        <input type="text" 
                               id="keyword" 
                               name="keyword" 
                               placeholder="Serial number or product name..."
                               value="${keyword}">
                    </div>

                    <div class="form-group">
                        <label for="productId">Product</label>
                        <select id="productId" name="productId">
                            <option value="ALL">All Products</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}" 
                                        ${selectedProductId == product.id.toString() ? 'selected' : ''}>
                                    ${product.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status">
                            <option value="ALL" ${selectedStatus == 'ALL' || selectedStatus == null ? 'selected' : ''}>All Status</option>
                            <option value="AVAILABLE" ${selectedStatus == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                            <option value="SOLD" ${selectedStatus == 'SOLD' ? 'selected' : ''}>Sold</option>
                            <option value="MAINTENANCE" ${selectedStatus == 'MAINTENANCE' ? 'selected' : ''}>Maintenance</option>
                            <option value="DECOMMISSIONED" ${selectedStatus == 'DECOMMISSIONED' ? 'selected' : ''}>Decommissioned</option>
                        </select>
                    </div>

                    <div>
                        <button type="submit" class="btn-filter">Filter</button>
                        <a href="${pageContext.request.contextPath}/management/devices" class="btn-reset">Reset</a>
                    </div>
                </div>
            </form>
        </div>

        <!-- Devices Table -->
        <div class="table-container">
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
                                <th>Serial Number</th>
                                <th>Product</th>
                                <th>Status</th>
                                <th>Notes</th>
                                <th>Created At</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="device" items="${devices}">
                                <tr>
                                    <td><strong>${device.serialNumber}</strong></td>
                                    <td>${device.productName}</td>
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
                                        <c:choose>
                                            <c:when test="${not empty device.notes}">
                                                ${device.notes.length() > 50 ? device.notes.substring(0, 50).concat('...') : device.notes}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">No notes</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${device.createdAt != null}">
                                            <%= ((com.swp391.group6.model.Device)pageContext.findAttribute("device")).getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) %>
                                        </c:if>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/management/devices/detail?id=${device.id}" 
                                           class="action-link">View Details</a>
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
