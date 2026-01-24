<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Details</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; min-height: 100vh; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden; }
        .header { background: #2c3e50; color: white; padding: 20px; text-align: center; }
        .header h1 { font-size: 28px; font-weight: 600; }
        .content { padding: 40px; }
        .user-card { display: flex; gap: 30px; margin-bottom: 30px; padding: 20px; background: #f8f9fa; border-radius: 10px; }
        .avatar-section { flex-shrink: 0; }
        .avatar { width: 150px; height: 150px; border-radius: 50%; object-fit: cover; border: 4px solid white; box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
        .default-avatar { width: 150px; height: 150px; border-radius: 50%; background: #3498db; display: flex; align-items: center; justify-content: center; font-size: 60px; color: white; font-weight: bold; border: 4px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .info-section { flex: 1; }
        .info-row { display: flex; padding: 15px 0; border-bottom: 1px solid #dee2e6; }
        .info-row:last-child { border-bottom: none; }
        .info-label { font-weight: 600; color: #2c3e50; width: 180px; flex-shrink: 0; }
        .info-value { flex: 1; color: #333; }
        .badge { padding: 6px 12px; border-radius: 20px; font-size: 13px; font-weight: 500; display: inline-block; }
        .badge-success { background: #d4edda; color: #155724; }
        .badge-danger { background: #f8d7da; color: #721c24; }
        .badge-primary { background: #cfe2ff; color: #084298; }
        .btn { padding: 10px 20px; border: none; border-radius: 4px; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.2s; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn-primary { background: #3498db; color: white; }
        .btn-primary:hover { background: #2980b9; }
        .btn-warning { background: #f39c12; color: white; }
        .btn-warning:hover { background: #e67e22; }
        .btn-secondary { background: #95a5a6; color: white; }
        .btn-secondary:hover { background: #7f8c8d; }
        .action-buttons { display: flex; gap: 10px; margin-top: 30px; padding-top: 20px; border-top: 2px solid #dee2e6; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>User Details</h1>
        </div>

        <div class="content">
            <c:choose>
                <c:when test="${not empty user}">
                    <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #dee2e6;">
                        <h3 style="margin-bottom: 20px; color: #2c3e50;">Detailed Information</h3>
                        
                        <div class="info-row">
                            <div class="info-label">ID:</div>
                            <div class="info-value">${user.id}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Email:</div>
                            <div class="info-value">${user.email}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Full Name:</div>
                            <div class="info-value">${user.fullName}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Gender:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${user.gender == 'MALE'}">Male</c:when>
                                    <c:when test="${user.gender == 'FEMALE'}">Female</c:when>
                                    <c:otherwise>Other</c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Phone:</div>
                            <div class="info-value">${not empty user.phone ? user.phone : '<em>Not updated</em>'}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Address:</div>
                            <div class="info-value">${not empty user.address ? user.address : '<em>Not updated</em>'}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Role ID:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${user.roleId == 1}"><span class="badge badge-primary">Administrator</span></c:when>
                                    <c:when test="${user.roleId == 2}"><span class="badge badge-primary">Manager</span></c:when>
                                    <c:when test="${user.roleId == 3}"><span class="badge badge-primary">Sales</span></c:when>
                                    <c:when test="${user.roleId == 4}"><span class="badge badge-primary">Customer</span></c:when>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Status:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${user.status}">
                                        <span class="badge badge-success">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Locked</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Created At:</div>
                            <div class="info-value">${user.createdAt}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Last Updated:</div>
                            <div class="info-value">${not empty user.updatedAt ? user.updatedAt : '<em>None</em>'}</div>
                        </div>
                    </div>

                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/users/edit?id=${user.id}" class="btn btn-warning">Edit</a>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Back to List</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 50px;">
                        <h3 style="color: #dc3545;">User Not Found</h3>
                        <p style="margin: 20px 0;">User ID does not exist or has been deleted</p>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">Back to List</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
