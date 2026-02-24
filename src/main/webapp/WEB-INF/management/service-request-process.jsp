<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Xu ly yeu cau dich vu</title>
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
            max-width: 800px; 
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
        .form-group { 
            margin-bottom: 25px; 
        }
        label { 
            display: block; 
            font-weight: 600; 
            color: #333; 
            margin-bottom: 8px; 
            font-size: 15px; 
        }
        label .required { 
            color: #dc3545; 
        }
        input[type="text"], 
        textarea, 
        select { 
            width: 100%; 
            padding: 12px 15px; 
            border: 2px solid #ddd; 
            border-radius: 8px; 
            font-size: 15px; 
            transition: border-color 0.3s; 
            font-family: inherit; 
        }
        input:focus, 
        textarea:focus, 
        select:focus { 
            outline: none; 
            border-color: #3498db; 
        }
        textarea { 
            resize: vertical; 
            min-height: 120px; 
        }
        .form-row { 
            display: flex; 
            gap: 20px; 
        }
        .form-row .form-group { 
            flex: 1; 
        }
        .readonly-field { 
            background: #f8f9fa; 
            padding: 12px 15px; 
            border: 2px solid #e9ecef; 
            border-radius: 8px; 
            font-size: 15px; 
            color: #495057; 
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
        .alert { 
            padding: 15px 20px; 
            margin-bottom: 20px; 
            border-radius: 8px; 
            font-size: 15px; 
        }
        .alert-danger { 
            background: #f8d7da; 
            color: #842029; 
            border: 1px solid #f5c2c7; 
        }
        .hint { 
            font-size: 13px; 
            color: #6c757d; 
            margin-top: 5px; 
        }
        .badge-type { 
            background: #e2e3e5; 
            color: #41464b; 
            padding: 4px 10px; 
            border-radius: 12px; 
            font-size: 12px; 
            font-weight: 500; 
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="header">
            <h1>Xu ly yeu cau dich vu</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <div class="info-box">
                <p><strong>Ma yeu cau:</strong> 
                    ${serviceRequest.requestCode}
                </p>
                <p><strong>Khach hang:</strong> 
                    <c:out value="${serviceRequest.customerName}"/>
                </p>
                <p>
                    <strong>Ngay tao:</strong>
                    ${serviceRequest.createdAtFormatted}
                </p>
            </div>

            <%-- Read-only display of request info --%>
            <div class="form-group">
                <label>Chu de</label>
                <div class="readonly-field">
                    <c:out value="${serviceRequest.subject}"/>
                </div>
            </div>

            <div class="form-group">
                <label>Mo ta</label>
                <div class="readonly-field" 
                     style="min-height: 80px; white-space: pre-wrap;">
                    <c:out value="${serviceRequest.description}"/>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Loai yeu cau</label>
                    <div class="readonly-field">
                        <span class="badge-type">
                            ${serviceRequest.requestTypeDisplay}
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label>Do uu tien</label>
                    <div class="readonly-field">
                        ${serviceRequest.priorityDisplay}
                    </div>
                </div>
            </div>

            <c:if test="${not empty serviceRequest.deviceSerialNumber}">
                <div class="form-group">
                    <label>Thiet bi</label>
                    <div class="readonly-field">
                        <c:out value="${serviceRequest.deviceSerialNumber}"/>
                    </div>
                </div>
            </c:if>

            <%-- Editable fields --%>
            <form method="post" 
                  action="${pageContext.request.contextPath}/management/service-requests/process"
                  accept-charset="UTF-8">
                <input type="hidden" name="id" 
                       value="${serviceRequest.id}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="status">
                            Trang thai <span class="required">*</span>
                        </label>
                        <select id="status" name="status" required>
                            <c:forEach var="allowedStatus" 
                                       items="${allowedStatuses}">
                                <option value="${allowedStatus}" 
                                    ${serviceRequest.status == allowedStatus ? 'selected' : ''}>
                                    <c:choose>
                                        <c:when test="${allowedStatus == 'OPEN'}">
                                            Mo
                                        </c:when>
                                        <c:when test="${allowedStatus == 'IN_PROGRESS'}">
                                            Dang xu ly
                                        </c:when>
                                        <c:when test="${allowedStatus == 'RESOLVED'}">
                                            Da giai quyet
                                        </c:when>
                                        <c:when test="${allowedStatus == 'CLOSED'}">
                                            Da dong
                                        </c:when>
                                    </c:choose>
                                </option>
                            </c:forEach>
                        </select>
                        <div class="hint">
                            Mo -> Dang xu ly -> Da giai quyet -> Da dong
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="assignedTo">
                            Nguoi xu ly
                        </label>
                        <select id="assignedTo" name="assignedTo">
                            <option value="">-- Chon nhan vien --</option>
                            <c:forEach var="staff" items="${staffList}">
                                <option value="${staff.id}" 
                                    ${serviceRequest.assignedTo != null && serviceRequest.assignedTo == staff.id ? 'selected' : ''}>
                                    <c:out value="${staff.fullName}"/> 
                                    (<c:out value="${staff.email}"/>)
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="resolution">
                        Ket qua xu ly
                        <span class="hint" style="font-weight: normal;">
                            (Bat buoc khi danh dau Da giai quyet)
                        </span>
                    </label>
                    <textarea id="resolution" 
                              name="resolution" 
                              maxlength="2000"
                              placeholder="Nhap ket qua xu ly yeu cau..."><c:out value="${serviceRequest.resolution}"/></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Cap nhat
                    </button>
                    <a href="${pageContext.request.contextPath}/management/service-requests/view?id=${serviceRequest.id}" 
                       class="btn btn-secondary">
                        Huy bo
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Show/require resolution when status is RESOLVED
        document.getElementById('status')
            .addEventListener('change', function() {
            var resField = 
                document.getElementById('resolution');
            if (this.value === 'RESOLVED') {
                resField.required = true;
                resField.placeholder = 
                    'Bat buoc nhap ket qua xu ly...';
            } else {
                resField.required = false;
                resField.placeholder = 
                    'Nhap ket qua xu ly yeu cau...';
            }
        });
        
        // Trigger on page load
        document.getElementById('status')
            .dispatchEvent(new Event('change'));
    </script>
</body>
</html>
