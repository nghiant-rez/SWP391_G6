<%@ page contentType="text/html;charset=UTF-8"
    language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">
    <title>Quan ly yeu cau dich vu</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma,
                Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            min-height: 100vh;
        }
        .container {
            max-width: 1400px;
            margin: 20px auto;
            background: white;
            border-radius: 8px;
            box-shadow:
                0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .page-header {
            background: #2c3e50;
            color: white;
            padding: 20px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .page-header h1 {
            font-size: 28px;
            font-weight: 600;
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
        .badge {
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
            white-space: nowrap;
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
        .subject-col {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .stats {
            color: #6c757d;
            font-size: 14px;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/includes/navbar.jsp" %>

<div class="container">
    <div class="page-header">
        <h1>Quan ly yeu cau dich vu</h1>
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
              action="${pageContext.request.contextPath}/management/service-requests"
              class="search-form">
            <div class="toolbar-row">
                <div style="display:flex;gap:10px;
                            flex-wrap:wrap;">
                    <input type="text"
                           name="search"
                           placeholder="Tim theo ma, chu de, khach hang..."
                           value="<c:out value='${searchValue}'/>">

                    <select name="status">
                        <option value="">
                            -- Trang thai --
                        </option>
                        <option value="OPEN"
                            ${statusValue == 'OPEN'
                                ? 'selected' : ''}>
                            Mo
                        </option>
                        <option value="IN_PROGRESS"
                            ${statusValue == 'IN_PROGRESS'
                                ? 'selected' : ''}>
                            Dang xu ly
                        </option>
                        <option value="RESOLVED"
                            ${statusValue == 'RESOLVED'
                                ? 'selected' : ''}>
                            Da giai quyet
                        </option>
                        <option value="CLOSED"
                            ${statusValue == 'CLOSED'
                                ? 'selected' : ''}>
                            Da dong
                        </option>
                    </select>

                    <select name="requestType">
                        <option value="">
                            -- Loai yeu cau --
                        </option>
                        <option value="REPAIR"
                            ${requestTypeValue == 'REPAIR'
                                ? 'selected' : ''}>
                            Sua chua
                        </option>
                        <option value="MAINTENANCE"
                            ${requestTypeValue == 'MAINTENANCE'
                                ? 'selected' : ''}>
                            Bao tri
                        </option>
                        <option value="COMPLAINT"
                            ${requestTypeValue == 'COMPLAINT'
                                ? 'selected' : ''}>
                            Khieu nai
                        </option>
                        <option value="INQUIRY"
                            ${requestTypeValue == 'INQUIRY'
                                ? 'selected' : ''}>
                            Yeu cau thong tin
                        </option>
                        <option value="WARRANTY"
                            ${requestTypeValue == 'WARRANTY'
                                ? 'selected' : ''}>
                            Bao hanh
                        </option>
                        <option value="OTHER"
                            ${requestTypeValue == 'OTHER'
                                ? 'selected' : ''}>
                            Khac
                        </option>
                    </select>

                    <select name="priority">
                        <option value="">
                            -- Do uu tien --
                        </option>
                        <option value="LOW"
                            ${priorityValue == 'LOW'
                                ? 'selected' : ''}>
                            Thap
                        </option>
                        <option value="MEDIUM"
                            ${priorityValue == 'MEDIUM'
                                ? 'selected' : ''}>
                            Trung binh
                        </option>
                        <option value="HIGH"
                            ${priorityValue == 'HIGH'
                                ? 'selected' : ''}>
                            Cao
                        </option>
                        <option value="URGENT"
                            ${priorityValue == 'URGENT'
                                ? 'selected' : ''}>
                            Khan cap
                        </option>
                    </select>

                    <button type="submit"
                            class="btn btn-primary">
                        Tim kiem
                    </button>
                    <a href="${pageContext.request.contextPath}/management/service-requests"
                       class="btn btn-secondary">
                        Xoa loc
                    </a>
                </div>

                <div style="display:flex;gap:10px;
                            align-items:center;">
                    <span class="stats">
                        Tong:
                        <strong>${totalRequests}</strong>
                        yeu cau
                    </span>
                </div>
            </div>
        </form>
    </div>

    <div class="table-container">
        <c:choose>
            <c:when test="${empty requests}">
                <div style="text-align:center;
                            padding:50px;
                            color:#6c757d;">
                    <h3>Khong co yeu cau nao</h3>
                    <p>Thu thay doi bo loc</p>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Ma yeu cau</th>
                            <th>Chu de</th>
                            <th>Khach hang</th>
                            <th>Loai</th>
                            <th>Do uu tien</th>
                            <th>Trang thai</th>
                            <th>Ngay tao</th>
                            <th>Thao tac</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sr"
                                   items="${requests}">
                            <tr>
                                <td>
                                    <strong>
                                        ${sr.requestCode}
                                    </strong>
                                </td>
                                <td class="subject-col"
                                    title="<c:out value='${sr.subject}'/>">
                                    <c:out
                                        value="${sr.subject}"/>
                                </td>
                                <td>
                                    <c:out
                                        value="${sr.customerName}"/>
                                </td>
                                <td>
                                    <span class="badge badge-type">
                                        ${sr.requestTypeDisplay}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sr.priority == 'LOW'}">
                                            <span class="badge badge-low">Thap</span>
                                        </c:when>
                                        <c:when test="${sr.priority == 'MEDIUM'}">
                                            <span class="badge badge-medium">Trung binh</span>
                                        </c:when>
                                        <c:when test="${sr.priority == 'HIGH'}">
                                            <span class="badge badge-high">Cao</span>
                                        </c:when>
                                        <c:when test="${sr.priority == 'URGENT'}">
                                            <span class="badge badge-urgent">Khan cap</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sr.status == 'OPEN'}">
                                            <span class="badge badge-open">Mo</span>
                                        </c:when>
                                        <c:when test="${sr.status == 'IN_PROGRESS'}">
                                            <span class="badge badge-in-progress">Dang xu ly</span>
                                        </c:when>
                                        <c:when test="${sr.status == 'RESOLVED'}">
                                            <span class="badge badge-resolved">Da giai quyet</span>
                                        </c:when>
                                        <c:when test="${sr.status == 'CLOSED'}">
                                            <span class="badge badge-closed">Da dong</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td style="white-space:nowrap;">
                                    ${sr.createdAtFormatted}
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/management/service-requests/view?id=${sr.id}"
                                           class="btn btn-primary btn-sm">
                                            Xem
                                        </a>
                                        <c:if test="${canProcess}">
                                            <a href="${pageContext.request.contextPath}/management/service-requests/process?id=${sr.id}"
                                               class="btn btn-warning btn-sm">
                                                Xu ly
                                            </a>
                                        </c:if>
                                        <c:if test="${canDelete}">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/management/service-requests/delete"
                                                  style="display:inline;"
                                                  onsubmit="return confirm('Ban co chac muon xoa yeu cau nay?');">
                                                <input type="hidden"
                                                       name="requestId"
                                                       value="${sr.id}">
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
                            <c:url value="" var="prevUrl">
                                <c:param name="page" value="${currentPage - 1}"/>
                                <c:param name="search" value="${searchValue}"/>
                                <c:param name="status" value="${statusValue}"/>
                                <c:param name="requestType" value="${requestTypeValue}"/>
                                <c:param name="priority" value="${priorityValue}"/>
                            </c:url>
                            <a href="${prevUrl}">
                                Truoc
                            </a>
                        </c:if>

                        <c:forEach begin="1"
                                   end="${totalPages}"
                                   var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="active">
                                        ${i}
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <c:url value="" var="pageUrl">
                                        <c:param name="page" value="${i}"/>
                                        <c:param name="search" value="${searchValue}"/>
                                        <c:param name="status" value="${statusValue}"/>
                                        <c:param name="requestType" value="${requestTypeValue}"/>
                                        <c:param name="priority" value="${priorityValue}"/>
                                    </c:url>
                                    <a href="${pageUrl}">
                                        ${i}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <c:url value="" var="nextUrl">
                                <c:param name="page" value="${currentPage + 1}"/>
                                <c:param name="search" value="${searchValue}"/>
                                <c:param name="status" value="${statusValue}"/>
                                <c:param name="requestType" value="${requestTypeValue}"/>
                                <c:param name="priority" value="${priorityValue}"/>
                            </c:url>
                            <a href="${nextUrl}">
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
