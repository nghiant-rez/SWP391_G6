<%@ page contentType="text/html;charset=UTF-8"
         language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,
                   initial-scale=1.0">
    <title>Contract list</title>
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
            padding: 20px;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .header {
            background: #2c3e50;
            color: white;
            padding: 20px 30px;
        }

        .header h1 {
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
            gap: 12px;
            margin-bottom: 12px;
        }

        .toolbar-row:last-child {
            margin-bottom: 0;
        }

        .search-group {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: wrap;
        }

        .search-group input[type="text"] {
            padding: 9px 14px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            width: 280px;
        }

        .search-group input:focus {
            outline: none;
            border-color: #3498db;
        }

        .filter-group {
            display: flex;
            gap: 12px;
            align-items: center;
            flex-wrap: wrap;
        }

        .filter-group label {
            font-size: 14px;
            color: #555;
        }

        .filter-group select {
            padding: 8px 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            background: white;
        }

        .radio-group {
            display: flex;
            gap: 10px;
            align-items: center;
            font-size: 14px;
        }

        .radio-group label {
            color: #555;
            cursor: pointer;
        }

        .btn {
            padding: 9px 18px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background 0.2s;
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
            padding: 5px 11px;
            font-size: 13px;
        }

        .table-container {
            padding: 24px 30px;
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
            padding: 11px 14px;
            border-bottom: 2px solid #dee2e6;
            white-space: nowrap;
        }

        td {
            padding: 11px 14px;
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
        }

        .badge-draft {
            background: #e9ecef;
            color: #495057;
        }

        .badge-pending {
            background: #fff3cd;
            color: #664d03;
        }

        .badge-approved {
            background: #d1e7dd;
            color: #0f5132;
        }

        .badge-rejected {
            background: #f8d7da;
            color: #842029;
        }

        .badge-completed {
            background: #cfe2ff;
            color: #084298;
        }

        .action-buttons {
            display: flex;
            gap: 6px;
            flex-wrap: nowrap;
            align-items: center;
        }

        .action-buttons form {
            margin: 0;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            gap: 6px;
        }

        .pagination a, .pagination span {
            padding: 7px 13px;
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
            padding: 14px 20px;
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

        .footer-bar {
            padding: 16px 30px;
            border-top: 1px solid #dee2e6;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Contract list</h1>
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
        <%-- FIX BUG 7: URL dung /management/contracts --%>
        <form method="get"
              action="${pageContext.request.contextPath}/management/contracts">
            <div class="toolbar-row">
                <div class="search-group">
                    <input type="text"
                           name="search"
                           placeholder="Search by customer name, creators"
                           value="<c:out value='${searchValue}'/>">
                    <button type="submit"
                            class="btn btn-primary">
                        Sreach
                    </button>
                </div>
                <c:if test="${canCreate}">
                    <a href="${pageContext.request.contextPath}/management/contracts/create"
                       class="btn btn-success">
                        + Them hop dong moi
                    </a>
                </c:if>
            </div>

            <div class="toolbar-row">
                <div class="filter-group">
                    <label><strong>Sort by:</strong>
                    </label>
                    <div class="radio-group">
                        <label>
                            <input type="radio"
                                   name="sortBy"
                                   value="id"
                            <c:if test="${sortByValue == 'id'
                                       || empty sortByValue}">
                                   checked
                            </c:if>>
                            ID
                        </label>
                        <label>
                            <input type="radio"
                                   name="sortBy"
                                   value="customer"
                            <c:if test="${sortByValue
                                       == 'customer'}">
                                   checked
                            </c:if>>
                            Khach hang
                        </label>
                        <label>
                            <input type="radio"
                                   name="sortBy"
                                   value="createdAt"
                            <c:if test="${sortByValue
                                       == 'createdAt'}">
                                   checked
                            </c:if>>
                            Ngay tao
                        </label>
                    </div>

                    <%-- Chi MANAGER moi thay dropdown Creator --%>
                    <c:if test="${isManager}">
                        <label style="margin-left:20px;">
                            <strong>All Creators</strong>
                        </label>
                        <select name="creatorId">
                            <option value="">All Creators</option>
                            <c:forEach var="staff" items="${staffList}">
                                <option value="${staff.id}"
                                        <c:if test="${not empty creatorIdValue
                                             and creatorIdValue == staff.id}">
                                            selected
                                        </c:if>>
                                    <c:out value="${staff.fullName}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </c:if>
                </div>

                <div class="filter-group">
                    <label><strong>Order:</strong>
                    </label>
                    <div class="radio-group">
                        <label>
                            <input type="radio"
                                   name="order"
                                   value="ASC"

                            <c:if test="${orderValue == 'ASC' || empty orderValue}">
                                   checked
                            </c:if>>
                            Tang dan
                        </label>
                        <label>
                            <input type="radio"
                                   name="order"
                                   value="DESC"

                            <c:if test="${orderValue == 'DESC'}">
                                   checked
                            </c:if>>
                            Giam dan
                        </label>
                    </div>
                    <a href="${pageContext.request.contextPath}/management/contracts"
                       class="btn btn-secondary btn-sm">
                        Reset filter
                    </a>
                </div>
            </div>
        </form>
    </div>

    <div class="table-container">
        <c:choose>
            <c:when test="${empty contracts}">
                <div style="text-align:center;
                            padding:50px;
                            color:#6c757d;">
                    <h3>Khong co hop dong nao</h3>
                    <p>Thu thay doi bo loc</p>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Khach hang</th>
                        <th>Ngay tao</th>
                        <th>Nguoi tao</th>
                        <th>Trang thai</th>
                        <th>Url Contract</th>
                        <th>Hanh dong</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="contract"
                               items="${contracts}">
                        <tr>
                            <td>${contract.id}</td>
                            <td>
                                <c:out value=
                                               "${contract.customerName}"/>
                            </td>
                                <%-- FIX BUG 6: toString()
                                     thay vi fmt:parseDate --%>
                            <td>
                                <c:if test="${contract.createdAt
                                    != null}">
                                    ${contract.createdAt}
                                </c:if>
                            </td>
                            <td>
                                <c:out value=
                                               "${contract.staffName}"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${contract.status
                                        == 'DRAFT'}">
                                        <span class="badge badge-draft">
                                            DRAFT
                                        </span>
                                    </c:when>
                                    <c:when test="${contract.status == 'PENDING'}">
                                        <span class="badge badge-pending">
                                            PENDING
                                        </span>
                                    </c:when>
                                    <c:when test="${contract.status == 'APPROVED'}">
                                        <span class="badge badge-approved">
                                            APPROVED
                                        </span>
                                    </c:when>
                                    <c:when test="${contract.status
                                        == 'REJECTED'}">
                                        <span class="badge badge-rejected">
                                            REJECTED
                                        </span>
                                    </c:when>
                                    <c:when test="${contract.status
                                        == 'COMPLETED'}">
                                        <span class="badge badge-completed">
                                            COMPLETED
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-draft">
                                            <c:out value="${contract.status}"/>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                    <%-- URL Contract
                                         (chua implement) --%>
                                -
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/management/contracts/view?id=${contract.id}"
                                       class="btn btn-primary btn-sm">
                                        View
                                    </a>
                                        <%-- Edit: MANAGER xem tat ca, STAFF chi edit hop dong minh tao --%>
                                    <c:if test="${canUpdate}">
                                        <c:if test="${isManager or contract.staffId == currentUserId}">
                                            <a href="${pageContext.request.contextPath}
                                                       /management/contracts/edit?id=${contract.id}"
                                               class="btn btn-warning btn-sm">
                                                Edit
                                            </a>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${canApprove}">
                                        <%-- FIX BUG 7:
                                         URL deactivate --%>
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/management/contracts/deactivate"
                                              style="display:inline;"
                                              onsubmit="return confirm(
                                                       'Deactivate?');">
                                            <input type="hidden"
                                                   name="contractId"
                                                   value="${contract.id}">

                                            <button
                                                    type="submit"
                                                    class="btn btn-danger btn-sm">
                                                Deactivate
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <%-- Pagination: luon hien khi co du lieu,
                                  du chi co 1 trang --%>
                <c:if test="${not empty contracts}">
                    <div class="pagination">
                            <%-- Nut trang truoc --%>
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&search=<c:out value='${searchValue}'/>&sortBy=${sortByValue}&order=${orderValue}&creatorId=${creatorIdValue}">
                                    &lt;
                                </a>
                            </c:when>
                            <c:otherwise>
                                <%-- Disabled khi o trang dau --%>
                                <span style="color:#ccc;
                                      cursor:not-allowed;">
                                 &lt;
                                </span>
                            </c:otherwise>
                        </c:choose>

                            <%-- So trang --%>
                        <c:forEach begin="1"
                                   end="${totalPages}"
                                   var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=${i}&search=<c:out value='${searchValue}'/>&sortBy=${sortByValue}&order=${orderValue}&creatorId=${creatorIdValue}">
                                            ${i}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                            <%-- Nut trang sau --%>
                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&search=<c:out value='${searchValue}'/>&sortBy=${sortByValue}&order=${orderValue}&creatorId=${creatorIdValue}">
                                    &gt;
                                </a>
                            </c:when>
                            <c:otherwise>
                                <%-- Disabled khi o trang cuoi --%>
                                <span style="color:#ccc;
                                      cursor:not-allowed;">
                                &gt;
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="footer-bar">
        <a href="${pageContext.request.contextPath}/home">
            Tro ve trang chu
        </a>
    </div>
</div>
</body>
</html>