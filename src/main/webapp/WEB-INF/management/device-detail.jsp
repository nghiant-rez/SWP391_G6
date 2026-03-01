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
        body {
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: #f5f7fa;
            color: #2c3e50;
        }

        .main-container {
            max-width: 1200px;
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

        .header-actions {
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .btn-danger {
            background-color: #e74c3c;
            color: white;
        }

        .btn-danger:hover {
            background-color: #c0392b;
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

        .detail-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin-bottom: 20px;
        }

        .card-header {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e9ecef;
        }

        .detail-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
        }

        .detail-item {
            display: flex;
            flex-direction: column;
        }

        .detail-label {
            font-size: 12px;
            font-weight: 600;
            color: #7f8c8d;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .detail-value {
            font-size: 15px;
            color: #2c3e50;
            word-wrap: break-word;
        }

        .status-badge, .condition-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 13px;
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
        <div class="page-header">
            <h1>Device Details</h1>
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/management/devices" class="btn btn-secondary">
                    ← Back to List
                </a>
                <a href="${pageContext.request.contextPath}/management/devices/form?id=${device.id}" class="btn btn-primary">
                    ✏ Edit Device
                </a>
            </div>
        </div>

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
            <div class="card-header">Device Information</div>
            
            <div class="detail-grid">
                <div class="detail-item">
                    <div class="detail-label">Device ID</div>
                    <div class="detail-value">#${device.id}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Product Name</div>
                    <div class="detail-value">${device.productName}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Serial Number</div>
                    <div class="detail-value">${device.serialNumber}</div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Status</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${device.status == 'AVAILABLE'}">
                                <span class="status-badge status-available">Available</span>
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

                <div class="detail-item">
                    <div class="detail-label">Condition</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${device.condition == 'EXCELLENT'}">
                                <span class="condition-badge condition-excellent">Excellent</span>
                            </c:when>
                            <c:when test="${device.condition == 'GOOD'}">
                                <span class="condition-badge condition-good">Good</span>
                            </c:when>
                            <c:when test="${device.condition == 'FAIR'}">
                                <span class="condition-badge condition-fair">Fair</span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Current Location</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${not empty device.currentLocation}">
                                ${device.currentLocation}
                            </c:when>
                            <c:otherwise>
                                <span class="empty-value">Not specified</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Created At</div>
                    <div class="detail-value">
                        <fmt:formatDate value="${device.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                    </div>
                </div>

                <div class="detail-item">
                    <div class="detail-label">Last Updated</div>
                    <div class="detail-value">
                        <fmt:formatDate value="${device.updatedAt}" pattern="MMM dd, yyyy HH:mm" />
                    </div>
                </div>
            </div>

            <c:if test="${not empty device.notes}">
                <div class="detail-grid" style="margin-top: 25px;">
                    <div class="detail-item" style="grid-column: 1 / -1;">
                        <div class="detail-label">Notes</div>
                        <div class="detail-value">${device.notes}</div>
                    </div>
                </div>
            </c:if>

            <div class="quick-actions">
                <h3>Quick Actions</h3>
                
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/management/devices/form?id=${device.id}" 
                       class="btn btn-primary">
                        ✏ Edit Full Details
                    </a>

                    <form method="post" action="${pageContext.request.contextPath}/management/devices/detail" 
                          class="delete-form" 
                          onsubmit="return confirm('Are you sure you want to delete this device?');">
                        <input type="hidden" name="id" value="${device.id}">
                        <input type="hidden" name="action" value="delete">
                        <button type="submit" class="btn btn-danger">
                            🗑 Delete Device
                        </button>
                    </form>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/management/devices/detail" 
                      class="status-form">
                    <input type="hidden" name="id" value="${device.id}">
                    <input type="hidden" name="action" value="updateStatus">
                    
                    <label for="status" style="font-weight: 500; min-width: 120px;">Quick Status Update:</label>
                    <select id="status" name="status" class="form-control" style="min-width: 200px;">
                        <option value="AVAILABLE" ${device.status == 'AVAILABLE' ? 'selected' : ''}>Available</option>
                        <option value="SOLD" ${device.status == 'SOLD' ? 'selected' : ''}>Sold</option>
                        <option value="MAINTENANCE" ${device.status == 'MAINTENANCE' ? 'selected' : ''}>Maintenance</option>
                        <option value="DECOMMISSIONED" ${device.status == 'DECOMMISSIONED' ? 'selected' : ''}>Decommissioned</option>
                    </select>
                    
                    <button type="submit" class="btn btn-primary">Update Status</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
