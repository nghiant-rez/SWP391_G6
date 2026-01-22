<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết người dùng</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }
        .header h1 { font-size: 28px; font-weight: 600; }
        .content { padding: 40px; }
        .user-card { display: flex; gap: 30px; margin-bottom: 30px; padding: 20px; background: #f8f9fa; border-radius: 10px; }
        .avatar-section { flex-shrink: 0; }
        .avatar { width: 150px; height: 150px; border-radius: 50%; object-fit: cover; border: 4px solid white; box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
        .default-avatar { width: 150px; height: 150px; border-radius: 50%; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; font-size: 60px; color: white; font-weight: bold; border: 4px solid white; box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
        .info-section { flex: 1; }
        .info-row { display: flex; padding: 15px 0; border-bottom: 1px solid #dee2e6; }
        .info-row:last-child { border-bottom: none; }
        .info-label { font-weight: 600; color: #667eea; width: 180px; flex-shrink: 0; }
        .info-value { flex: 1; color: #333; }
        .badge { padding: 6px 12px; border-radius: 20px; font-size: 13px; font-weight: 500; display: inline-block; }
        .badge-success { background: #d4edda; color: #155724; }
        .badge-danger { background: #f8d7da; color: #721c24; }
        .badge-primary { background: #cfe2ff; color: #084298; }
        .btn { padding: 12px 24px; border: none; border-radius: 8px; font-size: 15px; font-weight: 500; cursor: pointer; transition: all 0.3s; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-warning { background: #ffc107; color: #333; }
        .btn-secondary { background: #6c757d; color: white; }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
        .action-buttons { display: flex; gap: 10px; margin-top: 30px; padding-top: 20px; border-top: 2px solid #dee2e6; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>👤 Chi tiết người dùng</h1>
        </div>

        <div class="content">
            <c:choose>
                <c:when test="${not empty user}">
                    <div class="user-card">
                        <div class="avatar-section">
                            <c:choose>
                                <c:when test="${not empty user.avatarUrl}">
                                    <img src="${user.avatarUrl}" alt="Avatar" class="avatar">
                                </c:when>
                                <c:otherwise>
                                    <div class="default-avatar">
                                        ${user.fullName.substring(0, 1).toUpperCase()}
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="info-section">
                            <h2 style="margin-bottom: 10px; color: #333;">${user.fullName}</h2>
                            <p style="color: #6c757d; margin-bottom: 20px;">${user.email}</p>
                            
                            <div style="display: flex; gap: 10px;">
                                <c:choose>
                                    <c:when test="${user.status}">
                                        <span class="badge badge-success">✓ Đang hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">✗ Bị khóa</span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="badge badge-primary">Role ${user.roleId}</span>
                            </div>
                        </div>
                    </div>

                    <div style="background: white; padding: 20px; border-radius: 10px; border: 1px solid #dee2e6;">
                        <h3 style="margin-bottom: 20px; color: #667eea;">📋 Thông tin chi tiết</h3>
                        
                        <div class="info-row">
                            <div class="info-label">🆔 ID:</div>
                            <div class="info-value">${user.id}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">📧 Email:</div>
                            <div class="info-value">${user.email}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">👤 Họ và tên:</div>
                            <div class="info-value">${user.fullName}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">⚧ Giới tính:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${user.gender == 'Male'}">👨 Nam</c:when>
                                    <c:when test="${user.gender == 'Female'}">👩 Nữ</c:when>
                                    <c:otherwise>⚧ Khác</c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">📱 Số điện thoại:</div>
                            <div class="info-value">${not empty user.phone ? user.phone : '<em>Chưa cập nhật</em>'}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">🏠 Địa chỉ:</div>
                            <div class="info-value">${not empty user.address ? user.address : '<em>Chưa cập nhật</em>'}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">👑 Role ID:</div>
                            <div class="info-value"><span class="badge badge-primary">${user.roleId}</span></div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">📊 Trạng thái:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${user.status}">
                                        <span class="badge badge-success">✓ Đang hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">✗ Bị khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">📅 Ngày tạo:</div>
                            <div class="info-value">${user.createdAt}</div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">🔄 Cập nhật lần cuối:</div>
                            <div class="info-value">${not empty user.updatedAt ? user.updatedAt : '<em>Chưa có</em>'}</div>
                        </div>
                    </div>

                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/users/edit?id=${user.id}" class="btn btn-warning">✏️ Chỉnh sửa</a>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">← Quay lại danh sách</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 50px;">
                        <h3 style="color: #dc3545;">❌ Không tìm thấy người dùng</h3>
                        <p style="margin: 20px 0;">User ID không tồn tại hoặc đã bị xóa</p>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">← Quay lại danh sách</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
