<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Chi tiet yeu cau dich vu</title>
    <style>
        * { 
            margin: 0; 
            padding: 0; 
            box-sizing: border-box; 
        }
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background: #f5f5f5; 
            min-height: 100vh; 
        }
        .container { 
            max-width: 900px; 
            margin: 20px auto; 
            background: white; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
            overflow: hidden; 
        }
        .header { 
            background: #2c3e50; 
            color: white; 
            padding: 20px 30px; 
            text-align: center; 
        }
        .header h1 { 
            font-size: 28px; 
            font-weight: 600; 
        }
        .content { 
            padding: 40px; 
        }
        .info-box { 
            background: #e7f3ff; 
            padding: 15px 20px; 
            border-radius: 8px; 
            margin-bottom: 30px; 
            border-left: 4px solid #3498db; 
            display: flex; 
            justify-content: space-between; 
            flex-wrap: wrap; 
            gap: 15px; 
        }
        .info-box p { 
            margin: 0; 
            color: #333; 
            font-size: 14px; 
        }
        .detail-section { 
            margin-bottom: 30px; 
        }
        .detail-label { 
            font-weight: 600; 
            color: #666; 
            font-size: 13px; 
            text-transform: uppercase; 
            margin-bottom: 8px; 
        }
        .detail-value { 
            font-size: 16px; 
            color: #333; 
            line-height: 1.6; 
        }
        .detail-value.empty { 
            color: #999; 
            font-style: italic; 
        }
        .detail-row { 
            display: flex; 
            gap: 40px; 
            margin-bottom: 25px; 
        }
        .detail-row .detail-section { 
            flex: 1; 
            margin-bottom: 0; 
        }
        .badge { 
            padding: 6px 14px; 
            border-radius: 20px; 
            font-size: 13px; 
            font-weight: 500; 
            display: inline-block; 
        }
        .badge-open { 
            background: #cfe2ff; 
            color: #084298; 
        }
        .badge-in-progress { 
            background: #fff3cd; 
            color: #664d03; 
        }
        .badge-resolved { 
            background: #d1e7dd; 
            color: #0f5132; 
        }
        .badge-closed { 
            background: #e9ecef; 
            color: #495057; 
        }
        .badge-low { 
            background: #e9ecef; 
            color: #495057; 
        }
        .badge-medium { 
            background: #cfe2ff; 
            color: #084298; 
        }
        .badge-high { 
            background: #fff3cd; 
            color: #664d03; 
        }
        .badge-urgent { 
            background: #f8d7da; 
            color: #842029; 
        }
        .badge-type { 
            background: #e2e3e5; 
            color: #41464b; 
        }
        .btn { 
            padding: 12px 24px; 
            border: none; 
            border-radius: 6px; 
            font-size: 15px; 
            font-weight: 500; 
            cursor: pointer; 
            transition: all 0.2s; 
            text-decoration: none; 
            display: inline-block; 
        }
        .btn-primary { 
            background: #3498db; 
            color: white; 
        }
        .btn-primary:hover { 
            background: #2980b9; 
        }
        .btn-warning { 
            background: #f39c12; 
            color: white; 
        }
        .btn-warning:hover { 
            background: #e67e22; 
        }
        .btn-danger { 
            background: #e74c3c; 
            color: white; 
        }
        .btn-danger:hover { 
            background: #c0392b; 
        }
        .btn-secondary { 
            background: #95a5a6; 
            color: white; 
        }
        .btn-secondary:hover { 
            background: #7f8c8d; 
        }
        .form-actions { 
            display: flex; 
            gap: 15px; 
            margin-top: 30px; 
            padding-top: 20px; 
            border-top: 2px solid #dee2e6; 
        }
        .form-actions form { 
            margin: 0; 
        }
        .alert { 
            padding: 15px 20px; 
            margin-bottom: 20px; 
            border-radius: 8px; 
        }
        .alert-success { 
            background: #d1e7dd; 
            color: #0f5132; 
            border: 1px solid #badbcc; 
        }
        .resolution-section { 
            background: #f8f9fa; 
            padding: 20px; 
            border-radius: 8px; 
            margin-top: 20px; 
        }
        .resolution-section h3 { 
            font-size: 16px; 
            color: #333; 
            margin-bottom: 15px; 
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="header">
            <h1>Chi tiet yeu cau dich vu</h1>
        </div>

        <div class="content">
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    <c:out value="${param.message}"/>
                </div>
            </c:if>

            <div class="info-box">
                <p><strong>Ma yeu cau:</strong> 
                    ${serviceRequest.requestCode}
                </p>
                <p>
                    <strong>Ngay tao:</strong> 
                    ${serviceRequest.createdAtFormatted}
                </p>
                <p><strong>Khach hang:</strong> 
                    <c:out value="${serviceRequest.customerName}"/>
                </p>
            </div>

            <div class="detail-section">
                <div class="detail-label">Chu de</div>
                <div class="detail-value">
                    <c:out value="${serviceRequest.subject}"/>
                </div>
            </div>

            <div class="detail-section">
                <div class="detail-label">Mo ta chi tiet</div>
                <div class="detail-value" 
                     style="white-space: pre-wrap;">
                    <c:out value="${serviceRequest.description}"/>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Loai yeu cau</div>
                    <div class="detail-value">
                        <span class="badge badge-type">
                            ${serviceRequest.requestTypeDisplay}
                        </span>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Do uu tien</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${serviceRequest.priority == 'LOW'}">
                                <span class="badge badge-low">Thap</span>
                            </c:when>
                            <c:when test="${serviceRequest.priority == 'MEDIUM'}">
                                <span class="badge badge-medium">
                                    Trung binh
                                </span>
                            </c:when>
                            <c:when test="${serviceRequest.priority == 'HIGH'}">
                                <span class="badge badge-high">Cao</span>
                            </c:when>
                            <c:when test="${serviceRequest.priority == 'URGENT'}">
                                <span class="badge badge-urgent">
                                    Khan cap
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Trang thai</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${serviceRequest.status == 'OPEN'}">
                                <span class="badge badge-open">Mo</span>
                            </c:when>
                            <c:when test="${serviceRequest.status == 'IN_PROGRESS'}">
                                <span class="badge badge-in-progress">
                                    Dang xu ly
                                </span>
                            </c:when>
                            <c:when test="${serviceRequest.status == 'RESOLVED'}">
                                <span class="badge badge-resolved">
                                    Da giai quyet
                                </span>
                            </c:when>
                            <c:when test="${serviceRequest.status == 'CLOSED'}">
                                <span class="badge badge-closed">
                                    Da dong
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Nguoi xu ly</div>
                    <div class="detail-value ${empty serviceRequest.assignedToName ? 'empty' : ''}">
                        <c:choose>
                            <c:when test="${not empty serviceRequest.assignedToName}">
                                <c:out value="${serviceRequest.assignedToName}"/>
                            </c:when>
                            <c:otherwise>
                                Chua phan cong
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Thiet bi</div>
                    <div class="detail-value ${empty serviceRequest.deviceSerialNumber ? 'empty' : ''}">
                        <c:choose>
                            <c:when test="${not empty serviceRequest.deviceSerialNumber}">
                                <c:out value="${serviceRequest.deviceSerialNumber}"/>
                            </c:when>
                            <c:otherwise>
                                Khong co
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Giai quyet luc</div>
                    <div class="detail-value ${empty serviceRequest.resolvedAtFormatted ? 'empty' : ''}">
                        <c:choose>
                            <c:when test="${not empty serviceRequest.resolvedAtFormatted}">
                                ${serviceRequest.resolvedAtFormatted}
                            </c:when>
                            <c:otherwise>
                                Chua giai quyet
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <c:if test="${not empty serviceRequest.resolution}">
                <div class="resolution-section">
                    <h3>Ket qua xu ly</h3>
                    <div class="detail-value" 
                         style="white-space: pre-wrap;">
                        <c:out value="${serviceRequest.resolution}"/>
                    </div>
                </div>
            </c:if>

            <div class="detail-row" style="margin-top: 20px;">
                <div class="detail-section">
                    <div class="detail-label">Cap nhat luc</div>
                    <div class="detail-value">
                        ${serviceRequest.updatedAtFormatted}
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <c:if test="${canProcess}">
                    <a href="${pageContext.request.contextPath}/management/service-requests/process?id=${serviceRequest.id}" 
                       class="btn btn-warning">
                        Xu ly yeu cau
                    </a>
                </c:if>
                <c:if test="${canDelete}">
                    <form method="post" 
                          action="${pageContext.request.contextPath}/management/service-requests/delete"
                          onsubmit="return confirm('Ban co chac muon xoa yeu cau nay?');">
                        <input type="hidden" name="requestId" 
                               value="${serviceRequest.id}">
                        <button type="submit" class="btn btn-danger">
                            Xoa
                        </button>
                    </form>
                </c:if>
                <a href="${pageContext.request.contextPath}/management/service-requests" 
                   class="btn btn-secondary">
                    Quay lai danh sach
                </a>
            </div>
        </div>
    </div>
</body>
</html>
