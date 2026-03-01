<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${device != null ? 'Edit Device' : 'Add New Device'} - CMMS</title>
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
            max-width: 800px;
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

        .back-button {
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            transition: background-color 0.3s;
            font-size: 14px;
        }

        .back-button:hover {
            background-color: #5a6268;
        }

        .form-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            padding: 30px;
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

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #2c3e50;
            font-size: 14px;
        }

        .form-group label .required {
            color: #e74c3c;
            margin-left: 3px;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #dfe6e9;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s, box-shadow 0.3s;
            box-sizing: border-box;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        select.form-control {
            cursor: pointer;
        }

        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
        }

        .btn {
            padding: 12px 30px;
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

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }

            .form-actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <!-- Include Navbar -->
    <jsp:include page="/WEB-INF/includes/navbar.jsp"/>

    <div class="main-container">
        <div class="page-header">
            <h1>${device != null ? 'Edit Device' : 'Add New Device'}</h1>
            <a href="${pageContext.request.contextPath}/management/devices" class="back-button">
                ← Back to List
            </a>
        </div>

        <div class="form-card">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    ${errorMessage}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/management/devices/form">
                <c:if test="${device != null}">
                    <input type="hidden" name="id" value="${device.id}">
                </c:if>

                <div class="form-group">
                    <label for="productId">
                        Product <span class="required">*</span>
                    </label>
                    <select id="productId" name="productId" class="form-control" required>
                        <option value="">-- Select Product --</option>
                        <c:forEach var="product" items="${products}">
                            <option value="${product.id}" 
                                ${device != null && device.productId == product.id ? 'selected' : ''}>
                                ${product.name} (${product.category})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="serialNumber">
                        Serial Number <span class="required">*</span>
                    </label>
                    <input type="text" id="serialNumber" name="serialNumber" 
                           class="form-control" 
                           value="${device != null ? device.serialNumber : ''}" 
                           placeholder="Enter unique serial number"
                           required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="status">
                            Status <span class="required">*</span>
                        </label>
                        <select id="status" name="status" class="form-control" required>
                            <option value="">-- Select Status --</option>
                            <option value="AVAILABLE" ${device != null && device.status == 'AVAILABLE' ? 'selected' : ''}>
                                Available
                            </option>
                            <option value="SOLD" ${device != null && device.status == 'SOLD' ? 'selected' : ''}>
                                Sold
                            </option>
                            <option value="MAINTENANCE" ${device != null && device.status == 'MAINTENANCE' ? 'selected' : ''}>
                                Maintenance
                            </option>
                            <option value="DECOMMISSIONED" ${device != null && device.status == 'DECOMMISSIONED' ? 'selected' : ''}>
                                Decommissioned
                            </option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="condition">
                            Condition <span class="required">*</span>
                        </label>
                        <select id="condition" name="condition" class="form-control" required>
                            <option value="">-- Select Condition --</option>
                            <option value="EXCELLENT" ${device != null && device.condition == 'EXCELLENT' ? 'selected' : ''}>
                                Excellent
                            </option>
                            <option value="GOOD" ${device != null && device.condition == 'GOOD' ? 'selected' : ''}>
                                Good
                            </option>
                            <option value="FAIR" ${device != null && device.condition == 'FAIR' ? 'selected' : ''}>
                                Fair
                            </option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="currentLocation">
                        Current Location
                    </label>
                    <input type="text" id="currentLocation" name="currentLocation" 
                           class="form-control" 
                           value="${device != null ? device.currentLocation : ''}" 
                           placeholder="Enter current location">
                </div>

                <div class="form-group">
                    <label for="notes">
                        Notes
                    </label>
                    <textarea id="notes" name="notes" class="form-control" 
                              placeholder="Enter any additional notes">${device != null ? device.notes : ''}</textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        ${device != null ? 'Update Device' : 'Create Device'}
                    </button>
                    <a href="${pageContext.request.contextPath}/management/devices" class="btn btn-secondary">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
