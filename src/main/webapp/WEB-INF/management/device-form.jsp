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
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .back-button {
            display: inline-block;
            padding: 8px 16px;
            margin-bottom: 20px;
            background-color: #e9ecef;
            color: #495057;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            border: 1px solid #ddd;
            transition: all 0.3s;
        }

        .back-button:hover {
            background-color: #dee2e6;
        }

        .form-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            padding: 40px;
            border: 1px solid #e1e8ed;
        }

        .form-title {
            font-size: 24px;
            font-weight: 600;
            color: #2c3e50;
            margin: 0 0 30px 0;
            text-align: center;
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

        .form-body {
            max-width: 600px;
            margin: 0 auto;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #495057;
            font-size: 14px;
        }

        .form-group label .required {
            color: #e74c3c;
            margin-left: 3px;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
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

        select.form-control {
            cursor: pointer;
            background-color: white;
        }

        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }

        /* Image Upload Section */
        .image-upload-section {
            display: flex;
            gap: 20px;
            align-items: flex-start;
        }

        .image-upload-label {
            flex: 0 0 120px;
            font-weight: 500;
            color: #495057;
            font-size: 14px;
            padding-top: 8px;
        }

        .image-upload-area {
            flex: 1;
            border: 2px dashed #ced4da;
            border-radius: 8px;
            padding: 40px;
            text-align: center;
            background-color: #f8f9fa;
            cursor: pointer;
            transition: all 0.3s;
        }

        .image-upload-area:hover {
            border-color: #80bdff;
            background-color: #e7f3ff;
        }

        .image-upload-icon {
            font-size: 48px;
            color: #adb5bd;
            margin-bottom: 10px;
        }

        .image-upload-text {
            font-size: 14px;
            color: #6c757d;
            font-weight: 500;
        }

        .image-upload-input {
            display: none;
        }

        .form-actions {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 35px;
            padding-top: 25px;
            border-top: 1px solid #e9ecef;
        }

        .btn {
            padding: 12px 40px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        @media (max-width: 768px) {
            .form-card {
                padding: 25px;
            }

            .image-upload-section {
                flex-direction: column;
            }

            .image-upload-label {
                flex: none;
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
        <a href="${pageContext.request.contextPath}/management/devices" class="back-button">
            ← Back
        </a>

        <div class="form-card">
            <h2 class="form-title">${device != null ? 'Update device' : 'Add device'}</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    ${errorMessage}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/management/devices/form" enctype="multipart/form-data">
                <c:if test="${device != null}">
                    <input type="hidden" name="id" value="${device.id}">
                </c:if>

                <div class="form-body">
                    <div class="form-group">
                        <label for="name">
                            Name
                        </label>
                        <input type="text" id="name" name="serialNumber" 
                               class="form-control" 
                               value="${device != null ? device.serialNumber : ''}" 
                               placeholder="Text box">
                    </div>

                    <div class="form-group">
                        <div class="image-upload-section">
                            <label class="image-upload-label">Image</label>
                            <label for="imageFile" class="image-upload-area">
                                <div class="image-upload-icon">📷</div>
                                <div class="image-upload-text">IMG</div>
                                <input type="file" id="imageFile" name="imageFile" accept="image/*" class="image-upload-input">
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description">
                            Description
                        </label>
                        <input type="text" id="description" name="notes" 
                               class="form-control" 
                               value="${device != null ? device.notes : ''}" 
                               placeholder="Text box">
                    </div>

                    <div class="form-group">
                        <label for="category">
                            Category
                        </label>
                        <select id="category" name="productId" class="form-control">
                            <option value="">All Category</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}" 
                                    ${device != null && device.productId == product.id ? 'selected' : ''}>
                                    ${product.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="maintenanceTime">
                            Maintaince_time
                        </label>
                        <input type="text" id="maintenanceTime" name="currentLocation" 
                               class="form-control" 
                               value="${device != null ? device.currentLocation : ''}" 
                               placeholder="Text box">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            ${device != null ? 'Update' : 'Add'}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
