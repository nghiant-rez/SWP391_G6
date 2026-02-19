<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Chi tiet cong viec</title>
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
            max-width: 900px; 
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
        .badge-todo { 
            background: #e9ecef; 
            color: #495057; 
        }
        .badge-in-progress { 
            background: #cfe2ff; 
            color: #084298; 
        }
        .badge-done { 
            background: #d1e7dd; 
            color: #0f5132; 
        }
        .badge-cancelled { 
            background: #f8d7da; 
            color: #842029; 
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
        .text-danger { 
            color: #dc3545; 
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
        .completion-section { 
            background: #f8f9fa; 
            padding: 20px; 
            border-radius: 8px; 
            margin-top: 20px; 
        }
        .completion-section h3 { 
            font-size: 16px; 
            color: #333; 
            margin-bottom: 15px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Chi tiet cong viec</h1>
        </div>

        <div class="content">
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    <c:out value="${param.message}"/>
                </div>
            </c:if>

            <div class="info-box">
                <p><strong>ID:</strong> ${task.id}</p>
                <p>
                    <strong>Ngay tao:</strong> 
                    <fmt:parseDate value="${task.createdAt}" 
                        pattern="yyyy-MM-dd'T'HH:mm" 
                        var="createdDate" type="both"/>
                    <fmt:formatDate value="${createdDate}" 
                        pattern="dd/MM/yyyy HH:mm"/>
                </p>
                <p><strong>Nguoi giao:</strong> 
                    <c:out value="${task.assignerName}"/>
                </p>
            </div>

            <div class="detail-section">
                <div class="detail-label">Tieu de</div>
                <div class="detail-value">
                    <c:out value="${task.title}"/>
                </div>
            </div>

            <div class="detail-section">
                <div class="detail-label">Mo ta</div>
                <div class="detail-value ${empty task.description ? 'empty' : ''}">
                    <c:choose>
                        <c:when test="${not empty task.description}">
                            <c:out value="${task.description}"/>
                        </c:when>
                        <c:otherwise>
                            Khong co mo ta
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Nguoi thuc hien</div>
                    <div class="detail-value">
                        <c:out value="${task.assigneeName}"/>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Loai cong viec</div>
                    <div class="detail-value">
                        <span class="badge badge-type">
                            ${task.taskTypeDisplay}
                        </span>
                    </div>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Do uu tien</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${task.priority == 'LOW'}">
                                <span class="badge badge-low">Thap</span>
                            </c:when>
                            <c:when test="${task.priority == 'MEDIUM'}">
                                <span class="badge badge-medium">
                                    Trung binh
                                </span>
                            </c:when>
                            <c:when test="${task.priority == 'HIGH'}">
                                <span class="badge badge-high">Cao</span>
                            </c:when>
                            <c:when test="${task.priority == 'URGENT'}">
                                <span class="badge badge-urgent">
                                    Khan cap
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Trang thai</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${task.status == 'TODO'}">
                                <span class="badge badge-todo">Chua lam</span>
                            </c:when>
                            <c:when test="${task.status == 'IN_PROGRESS'}">
                                <span class="badge badge-in-progress">
                                    Dang thuc hien
                                </span>
                            </c:when>
                            <c:when test="${task.status == 'DONE'}">
                                <span class="badge badge-done">Hoan thanh</span>
                            </c:when>
                            <c:when test="${task.status == 'CANCELLED'}">
                                <span class="badge badge-cancelled">
                                    Da huy
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="detail-row">
                <div class="detail-section">
                    <div class="detail-label">Han hoan thanh</div>
                    <div class="detail-value ${empty task.dueDate ? 'empty' : ''}">
                        <c:choose>
                            <c:when test="${task.dueDate != null}">
                                <span class="${task.overdue ? 'text-danger' : ''}">
                                    <fmt:parseDate value="${task.dueDate}" 
                                        pattern="yyyy-MM-dd'T'HH:mm" 
                                        var="dueDate" type="both"/>
                                    <fmt:formatDate value="${dueDate}" 
                                        pattern="dd/MM/yyyy HH:mm"/>
                                    <c:if test="${task.overdue}">
                                        (Qua han)
                                    </c:if>
                                </span>
                            </c:when>
                            <c:otherwise>
                                Chua dat han
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-section">
                    <div class="detail-label">Hoan thanh luc</div>
                    <div class="detail-value ${empty task.completedAt ? 'empty' : ''}">
                        <c:choose>
                            <c:when test="${task.completedAt != null}">
                                <fmt:parseDate value="${task.completedAt}" 
                                    pattern="yyyy-MM-dd'T'HH:mm" 
                                    var="completedDate" type="both"/>
                                <fmt:formatDate value="${completedDate}" 
                                    pattern="dd/MM/yyyy HH:mm"/>
                            </c:when>
                            <c:otherwise>
                                Chua hoan thanh
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <c:if test="${task.status == 'DONE' && not empty task.completionNotes}">
                <div class="completion-section">
                    <h3>Ghi chu hoan thanh</h3>
                    <div class="detail-value">
                        <c:out value="${task.completionNotes}"/>
                    </div>
                </div>
            </c:if>

            <div class="detail-row" style="margin-top: 20px;">
                <div class="detail-section">
                    <div class="detail-label">Cap nhat luc</div>
                    <div class="detail-value">
                        <c:if test="${task.updatedAt != null}">
                            <fmt:parseDate value="${task.updatedAt}" 
                                pattern="yyyy-MM-dd'T'HH:mm" 
                                var="updatedDate" type="both"/>
                            <fmt:formatDate value="${updatedDate}" 
                                pattern="dd/MM/yyyy HH:mm"/>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <c:if test="${canUpdate}">
                    <a href="${pageContext.request.contextPath}/management/tasks/edit?id=${task.id}" 
                       class="btn btn-warning">
                        Chinh sua
                    </a>
                </c:if>
                <c:if test="${canDelete}">
                    <form method="post" 
                          action="${pageContext.request.contextPath}/management/tasks/delete"
                          onsubmit="return confirm('Ban co chac muon xoa cong viec nay?');">
                        <input type="hidden" name="taskId" value="${task.id}">
                        <button type="submit" class="btn btn-danger">
                            Xoa
                        </button>
                    </form>
                </c:if>
                <a href="${pageContext.request.contextPath}/management/tasks" 
                   class="btn btn-secondary">
                    Quay lai danh sach
                </a>
            </div>
        </div>
    </div>
</body>
</html>
