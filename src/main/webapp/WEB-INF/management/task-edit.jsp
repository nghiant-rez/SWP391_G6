<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Chinh sua cong viec</title>
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
            padding: 20px; 
        }
        .container { 
            max-width: 800px; 
            margin: 0 auto; 
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
        input[type="datetime-local"], 
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
        input:disabled, 
        textarea:disabled, 
        select:disabled { 
            background: #f8f9fa; 
            color: #6c757d; 
            cursor: not-allowed; 
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
        .alert-info { 
            background: #cff4fc; 
            color: #055160; 
            border: 1px solid #b6effb; 
        }
        .hint { 
            font-size: 13px; 
            color: #6c757d; 
            margin-top: 5px; 
        }
        .staff-notice { 
            background: #fff3cd; 
            color: #664d03; 
            padding: 15px 20px; 
            border-radius: 8px; 
            margin-bottom: 25px; 
            border: 1px solid #ffecb5; 
        }
        .badge { 
            padding: 4px 10px; 
            border-radius: 12px; 
            font-size: 12px; 
            font-weight: 500; 
        }
        .badge-type { 
            background: #e2e3e5; 
            color: #41464b; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>
                <c:choose>
                    <c:when test="${isManager}">
                        Chinh sua cong viec
                    </c:when>
                    <c:otherwise>
                        Cap nhat trang thai cong viec
                    </c:otherwise>
                </c:choose>
            </h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <div class="info-box">
                <p><strong>ID:</strong> ${task.id}</p>
                <p><strong>Nguoi giao:</strong> 
                    <c:out value="${task.assignerName}"/>
                </p>
                <p>
                    <strong>Ngay tao:</strong>
                    <c:if test="${task.createdAt != null}">
                        <fmt:parseDate value="${task.createdAt}" 
                            pattern="yyyy-MM-dd'T'HH:mm" 
                            var="createdDate" type="both"/>
                        <fmt:formatDate value="${createdDate}" 
                            pattern="dd/MM/yyyy HH:mm"/>
                    </c:if>
                </p>
            </div>

            <c:if test="${not isManager}">
                <div class="staff-notice">
                    <strong>Luu y:</strong> Ban chi co the cap nhat trang thai 
                    va ghi chu hoan thanh. Cac thong tin khac chi co the duoc 
                    chinh sua boi Quan ly.
                </div>
            </c:if>

            <form method="post" 
                  action="${pageContext.request.contextPath}/management/tasks/edit"
                  accept-charset="UTF-8">
                <input type="hidden" name="id" value="${task.id}">

                <c:choose>
                    <c:when test="${isManager}">
                        <%-- Manager: Full edit form --%>
                        <div class="form-group">
                            <label for="title">
                                Tieu de <span class="required">*</span>
                            </label>
                            <input type="text" 
                                   id="title" 
                                   name="title" 
                                   value="<c:out value='${task.title}'/>"
                                   required 
                                   maxlength="200">
                        </div>

                        <div class="form-group">
                            <label for="description">Mo ta</label>
                            <textarea id="description" 
                                      name="description" 
                                      maxlength="1000"><c:out value="${task.description}"/></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="assigneeId">
                                    Nguoi thuc hien <span class="required">*</span>
                                </label>
                                <select id="assigneeId" name="assigneeId" required>
                                    <c:forEach var="staff" items="${staffList}">
                                        <option value="${staff.id}" 
                                            ${task.assigneeId == staff.id ? 'selected' : ''}>
                                            <c:out value="${staff.fullName}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="taskType">
                                    Loai cong viec <span class="required">*</span>
                                </label>
                                <select id="taskType" name="taskType" required>
                                    <option value="FOLLOW_UP" 
                                        ${task.taskType == 'FOLLOW_UP' ? 'selected' : ''}>
                                        Theo doi
                                    </option>
                                    <option value="SITE_VISIT" 
                                        ${task.taskType == 'SITE_VISIT' ? 'selected' : ''}>
                                        Kham sat
                                    </option>
                                    <option value="DELIVERY" 
                                        ${task.taskType == 'DELIVERY' ? 'selected' : ''}>
                                        Giao hang
                                    </option>
                                    <option value="INSTALLATION" 
                                        ${task.taskType == 'INSTALLATION' ? 'selected' : ''}>
                                        Lap dat
                                    </option>
                                    <option value="MAINTENANCE" 
                                        ${task.taskType == 'MAINTENANCE' ? 'selected' : ''}>
                                        Bao tri
                                    </option>
                                    <option value="OTHER" 
                                        ${task.taskType == 'OTHER' ? 'selected' : ''}>
                                        Khac
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="priority">
                                    Do uu tien <span class="required">*</span>
                                </label>
                                <select id="priority" name="priority" required>
                                    <option value="LOW" 
                                        ${task.priority == 'LOW' ? 'selected' : ''}>
                                        Thap
                                    </option>
                                    <option value="MEDIUM" 
                                        ${task.priority == 'MEDIUM' ? 'selected' : ''}>
                                        Trung binh
                                    </option>
                                    <option value="HIGH" 
                                        ${task.priority == 'HIGH' ? 'selected' : ''}>
                                        Cao
                                    </option>
                                    <option value="URGENT" 
                                        ${task.priority == 'URGENT' ? 'selected' : ''}>
                                        Khan cap
                                    </option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="status">
                                    Trang thai <span class="required">*</span>
                                </label>
                                <select id="status" name="status" required>
                                    <option value="TODO" 
                                        ${task.status == 'TODO' ? 'selected' : ''}>
                                        Chua lam
                                    </option>
                                    <option value="IN_PROGRESS" 
                                        ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>
                                        Dang thuc hien
                                    </option>
                                    <option value="DONE" 
                                        ${task.status == 'DONE' ? 'selected' : ''}>
                                        Hoan thanh
                                    </option>
                                    <option value="CANCELLED" 
                                        ${task.status == 'CANCELLED' ? 'selected' : ''}>
                                        Da huy
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="dueDate">Han hoan thanh</label>
                            <input type="datetime-local" 
                                   id="dueDate" 
                                   name="dueDate"
                                   value="${task.dueDate != null ? task.dueDate.toString().substring(0, 16) : ''}">
                        </div>
                    </c:when>

                    <c:otherwise>
                        <%-- Staff: Read-only display + status/notes only --%>
                        <div class="form-group">
                            <label>Tieu de</label>
                            <div class="readonly-field">
                                <c:out value="${task.title}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Mo ta</label>
                            <div class="readonly-field" 
                                 style="min-height: 80px; white-space: pre-wrap;">
                                <c:choose>
                                    <c:when test="${not empty task.description}">
                                        <c:out value="${task.description}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #999; font-style: italic;">
                                            Khong co mo ta
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label>Nguoi thuc hien</label>
                                <div class="readonly-field">
                                    <c:out value="${task.assigneeName}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Loai cong viec</label>
                                <div class="readonly-field">
                                    <span class="badge badge-type">
                                        ${task.taskTypeDisplay}
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label>Do uu tien</label>
                                <div class="readonly-field">
                                    ${task.priorityDisplay}
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Han hoan thanh</label>
                                <div class="readonly-field">
                                    <c:choose>
                                        <c:when test="${task.dueDate != null}">
                                            <fmt:parseDate value="${task.dueDate}" 
                                                pattern="yyyy-MM-dd'T'HH:mm" 
                                                var="dueDate" type="both"/>
                                            <fmt:formatDate value="${dueDate}" 
                                                pattern="dd/MM/yyyy HH:mm"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #999;">
                                                Chua dat han
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="status">
                                Trang thai <span class="required">*</span>
                            </label>
                            <select id="status" name="status" required>
                                <c:forEach var="allowedStatus" items="${allowedStatuses}">
                                    <option value="${allowedStatus}" 
                                        ${task.status == allowedStatus ? 'selected' : ''}>
                                        <c:choose>
                                            <c:when test="${allowedStatus == 'TODO'}">
                                                Chua lam
                                            </c:when>
                                            <c:when test="${allowedStatus == 'IN_PROGRESS'}">
                                                Dang thuc hien
                                            </c:when>
                                            <c:when test="${allowedStatus == 'DONE'}">
                                                Hoan thanh
                                            </c:when>
                                            <c:when test="${allowedStatus == 'CANCELLED'}">
                                                Da huy
                                            </c:when>
                                        </c:choose>
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="hint">
                                Ban chi co the chuyen trang thai theo trinh tu: 
                                Chua lam -> Dang thuc hien -> Hoan thanh
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

                <%-- Completion notes - shown for both roles --%>
                <div class="form-group">
                    <label for="completionNotes">
                        Ghi chu hoan thanh
                        <span class="hint" style="font-weight: normal;">
                            (Bat buoc khi danh dau Hoan thanh)
                        </span>
                    </label>
                    <textarea id="completionNotes" 
                              name="completionNotes" 
                              maxlength="1000"
                              placeholder="Nhap ghi chu khi hoan thanh cong viec..."><c:out value="${task.completionNotes}"/></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Cap nhat
                    </button>
                    <a href="${pageContext.request.contextPath}/management/tasks/view?id=${task.id}" 
                       class="btn btn-secondary">
                        Huy bo
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Show/require completion notes when status is DONE
        document.getElementById('status').addEventListener('change', function() {
            var notesField = document.getElementById('completionNotes');
            if (this.value === 'DONE') {
                notesField.required = true;
                notesField.placeholder = 'Bat buoc nhap ghi chu khi hoan thanh...';
            } else {
                notesField.required = false;
                notesField.placeholder = 'Nhap ghi chu khi hoan thanh cong viec...';
            }
        });
        
        // Trigger on page load
        document.getElementById('status').dispatchEvent(new Event('change'));
    </script>
</body>
</html>
