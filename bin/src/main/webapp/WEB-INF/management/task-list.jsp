<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Quan ly cong viec</title>
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
            max-width: 1400px; 
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
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
        }
        .header h1 { 
            font-size: 28px; 
            font-weight: 600; 
        }
        .header-right { 
            display: flex; 
            align-items: center; 
            gap: 15px; 
        }
        .btn-logout { 
            background: rgba(255,255,255,0.15); 
            color: white; 
            padding: 8px 16px; 
            border-radius: 4px; 
            border: 1px solid rgba(255,255,255,0.3); 
            text-decoration: none; 
            font-size: 14px; 
            transition: all 0.2s; 
        }
        .btn-logout:hover { 
            background: rgba(255,255,255,0.25); 
        }
        .toolbar { 
            background: #f8f9fa; 
            padding: 20px 30px; 
            border-bottom: 2px solid #e9ecef; 
        }
        .toolbar-row { 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            flex-wrap: wrap; 
            gap: 15px; 
            margin-bottom: 15px; 
        }
        .toolbar-row:last-child { 
            margin-bottom: 0; 
        }
        .search-form { 
            display: flex; 
            gap: 10px; 
            flex-wrap: wrap; 
            align-items: center; 
        }
        .search-form input[type="text"] { 
            padding: 10px 15px; 
            border: 2px solid #ddd; 
            border-radius: 6px; 
            font-size: 14px; 
            width: 250px; 
        }
        .search-form input:focus { 
            outline: none; 
            border-color: #3498db; 
        }
        .search-form select { 
            padding: 10px 15px; 
            border: 2px solid #ddd; 
            border-radius: 6px; 
            font-size: 14px; 
            background: white; 
        }
        .btn { 
            padding: 10px 20px; 
            border: none; 
            border-radius: 4px; 
            font-size: 14px; 
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
        .btn-success { 
            background: #27ae60; 
            color: white; 
        }
        .btn-success:hover { 
            background: #229954; 
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
        .btn-sm { 
            padding: 6px 12px; 
            font-size: 13px; 
        }
        .table-container { 
            padding: 30px; 
            overflow-x: auto; 
        }
        table { 
            width: 100%; 
            border-collapse: collapse; 
        }
        th { 
            background: #f8f9fa; 
            color: #333; 
            font-weight: 600; 
            text-align: left; 
            padding: 12px 15px; 
            border-bottom: 2px solid #dee2e6; 
            white-space: nowrap; 
        }
        td { 
            padding: 12px 15px; 
            border-bottom: 1px solid #dee2e6; 
            vertical-align: middle; 
        }
        tr:hover { 
            background: #f8f9fa; 
        }
        tr.overdue { 
            background: #fff5f5; 
        }
        tr.overdue:hover { 
            background: #ffe8e8; 
        }
        .badge { 
            padding: 4px 10px; 
            border-radius: 12px; 
            font-size: 12px; 
            font-weight: 500; 
            display: inline-block; 
            white-space: nowrap; 
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
        .action-buttons { 
            display: flex; 
            gap: 6px; 
            flex-wrap: nowrap; 
        }
        .action-buttons form { 
            margin: 0; 
        }
        .pagination { 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            padding: 20px; 
            gap: 8px; 
        }
        .pagination a, 
        .pagination span { 
            padding: 8px 14px; 
            border: 1px solid #dee2e6; 
            border-radius: 4px; 
            text-decoration: none; 
            color: #3498db; 
        }
        .pagination a:hover { 
            background: #3498db; 
            color: white; 
            border-color: #3498db; 
        }
        .pagination .active { 
            background: #3498db; 
            color: white; 
            border-color: #3498db; 
        }
        .alert { 
            padding: 15px 20px; 
            margin: 20px 30px; 
            border-radius: 8px; 
        }
        .alert-success { 
            background: #d1e7dd; 
            color: #0f5132; 
            border: 1px solid #badbcc; 
        }
        .alert-danger { 
            background: #f8d7da; 
            color: #842029; 
            border: 1px solid #f5c2c7; 
        }
        .task-title { 
            max-width: 200px; 
            overflow: hidden; 
            text-overflow: ellipsis; 
            white-space: nowrap; 
        }
        .due-date { 
            white-space: nowrap; 
        }
        .stats { 
            color: #6c757d; 
            font-size: 14px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Quan ly cong viec</h1>
            <div class="header-right">
                <span>Xin chao, 
                    <strong>${sessionScope.user.fullName}</strong>
                    (${sessionScope.user.roleName})
                </span>
                <a href="${pageContext.request.contextPath}/logout" 
                   class="btn-logout">Dang xuat</a>
            </div>
        </div>

        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <c:out value="${param.message}"/>
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">
                <c:out value="${param.error}"/>
            </div>
        </c:if>

        <div class="toolbar">
            <form method="get" 
                  action="${pageContext.request.contextPath}/management/tasks" 
                  class="search-form">
                <div class="toolbar-row">
                    <div style="display: flex; gap: 10px; flex-wrap: wrap;">
                        <input type="text" 
                               name="search" 
                               placeholder="Tim theo tieu de, nguoi thuc hien..."
                               value="${searchValue}">
                        
                        <select name="status">
                            <option value="">-- Trang thai --</option>
                            <option value="TODO" 
                                ${statusValue == 'TODO' ? 'selected' : ''}>
                                Chua lam
                            </option>
                            <option value="IN_PROGRESS" 
                                ${statusValue == 'IN_PROGRESS' ? 'selected' : ''}>
                                Dang thuc hien
                            </option>
                            <option value="DONE" 
                                ${statusValue == 'DONE' ? 'selected' : ''}>
                                Hoan thanh
                            </option>
                            <option value="CANCELLED" 
                                ${statusValue == 'CANCELLED' ? 'selected' : ''}>
                                Da huy
                            </option>
                        </select>

                        <select name="priority">
                            <option value="">-- Do uu tien --</option>
                            <option value="LOW" 
                                ${priorityValue == 'LOW' ? 'selected' : ''}>
                                Thap
                            </option>
                            <option value="MEDIUM" 
                                ${priorityValue == 'MEDIUM' ? 'selected' : ''}>
                                Trung binh
                            </option>
                            <option value="HIGH" 
                                ${priorityValue == 'HIGH' ? 'selected' : ''}>
                                Cao
                            </option>
                            <option value="URGENT" 
                                ${priorityValue == 'URGENT' ? 'selected' : ''}>
                                Khan cap
                            </option>
                        </select>

                        <select name="taskType">
                            <option value="">-- Loai cong viec --</option>
                            <option value="FOLLOW_UP" 
                                ${taskTypeValue == 'FOLLOW_UP' ? 'selected' : ''}>
                                Theo doi
                            </option>
                            <option value="SITE_VISIT" 
                                ${taskTypeValue == 'SITE_VISIT' ? 'selected' : ''}>
                                Kham sat
                            </option>
                            <option value="DELIVERY" 
                                ${taskTypeValue == 'DELIVERY' ? 'selected' : ''}>
                                Giao hang
                            </option>
                            <option value="INSTALLATION" 
                                ${taskTypeValue == 'INSTALLATION' ? 'selected' : ''}>
                                Lap dat
                            </option>
                            <option value="MAINTENANCE" 
                                ${taskTypeValue == 'MAINTENANCE' ? 'selected' : ''}>
                                Bao tri
                            </option>
                            <option value="OTHER" 
                                ${taskTypeValue == 'OTHER' ? 'selected' : ''}>
                                Khac
                            </option>
                        </select>

                        <button type="submit" class="btn btn-primary">
                            Tim kiem
                        </button>
                        <a href="${pageContext.request.contextPath}/management/tasks" 
                           class="btn btn-secondary">Xoa loc</a>
                    </div>

                    <div style="display: flex; gap: 10px; align-items: center;">
                        <span class="stats">
                            Tong: <strong>${totalTasks}</strong> cong viec
                        </span>
                        <c:if test="${canCreate}">
                            <a href="${pageContext.request.contextPath}/management/tasks/create" 
                               class="btn btn-success">
                                + Tao cong viec moi
                            </a>
                        </c:if>
                    </div>
                </div>
            </form>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty tasks}">
                    <div style="text-align: center; padding: 50px; color: #6c757d;">
                        <h3>Khong co cong viec nao</h3>
                        <p>Thu thay doi bo loc hoac tao cong viec moi</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tieu de</th>
                                <th>Nguoi thuc hien</th>
                                <th>Loai</th>
                                <th>Do uu tien</th>
                                <th>Trang thai</th>
                                <th>Han hoan thanh</th>
                                <th>Thao tac</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="task" items="${tasks}">
                                <tr class="${task.overdue ? 'overdue' : ''}">
                                    <td>${task.id}</td>
                                    <td class="task-title" 
                                        title="${task.title}">
                                        <c:out value="${task.title}"/>
                                    </td>
                                    <td><c:out value="${task.assigneeName}"/></td>
                                    <td>
                                        <span class="badge badge-type">
                                            ${task.taskTypeDisplay}
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${task.priority == 'LOW'}">
                                                <span class="badge badge-low">
                                                    Thap
                                                </span>
                                            </c:when>
                                            <c:when test="${task.priority == 'MEDIUM'}">
                                                <span class="badge badge-medium">
                                                    Trung binh
                                                </span>
                                            </c:when>
                                            <c:when test="${task.priority == 'HIGH'}">
                                                <span class="badge badge-high">
                                                    Cao
                                                </span>
                                            </c:when>
                                            <c:when test="${task.priority == 'URGENT'}">
                                                <span class="badge badge-urgent">
                                                    Khan cap
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${task.status == 'TODO'}">
                                                <span class="badge badge-todo">
                                                    Chua lam
                                                </span>
                                            </c:when>
                                            <c:when test="${task.status == 'IN_PROGRESS'}">
                                                <span class="badge badge-in-progress">
                                                    Dang thuc hien
                                                </span>
                                            </c:when>
                                            <c:when test="${task.status == 'DONE'}">
                                                <span class="badge badge-done">
                                                    Hoan thanh
                                                </span>
                                            </c:when>
                                            <c:when test="${task.status == 'CANCELLED'}">
                                                <span class="badge badge-cancelled">
                                                    Da huy
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td class="due-date">
                                        <c:choose>
                                            <c:when test="${task.dueDate != null}">
                                                <span class="${task.overdue ? 'text-danger' : ''}">
                                                    <fmt:parseDate value="${task.dueDate}" 
                                                        pattern="yyyy-MM-dd'T'HH:mm" 
                                                        var="parsedDate" type="both"/>
                                                    <fmt:formatDate value="${parsedDate}" 
                                                        pattern="dd/MM/yyyy HH:mm"/>
                                                    <c:if test="${task.overdue}">
                                                        (Qua han)
                                                    </c:if>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #6c757d;">
                                                    Chua dat
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/management/tasks/view?id=${task.id}" 
                                               class="btn btn-primary btn-sm">
                                                Xem
                                            </a>
                                            <c:if test="${canUpdate}">
                                                <a href="${pageContext.request.contextPath}/management/tasks/edit?id=${task.id}" 
                                                   class="btn btn-warning btn-sm">
                                                    Sua
                                                </a>
                                            </c:if>
                                            <c:if test="${canDelete}">
                                                <form method="post" 
                                                      action="${pageContext.request.contextPath}/management/tasks/delete"
                                                      style="display: inline;"
                                                      onsubmit="return confirm('Ban co chac muon xoa cong viec nay?');">
                                                    <input type="hidden" 
                                                           name="taskId" 
                                                           value="${task.id}">
                                                    <button type="submit" 
                                                            class="btn btn-danger btn-sm">
                                                        Xoa
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&search=${searchValue}&status=${statusValue}&priority=${priorityValue}&taskType=${taskTypeValue}">
                                    Truoc
                                </a>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="active">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?page=${i}&search=${searchValue}&status=${statusValue}&priority=${priorityValue}&taskType=${taskTypeValue}">
                                            ${i}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&search=${searchValue}&status=${statusValue}&priority=${priorityValue}&taskType=${taskTypeValue}">
                                    Sau
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
