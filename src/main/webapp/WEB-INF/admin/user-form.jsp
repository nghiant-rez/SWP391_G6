<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} người dùng</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }
        .header h1 { font-size: 28px; font-weight: 600; }
        .content { padding: 40px; }
        .form-group { margin-bottom: 25px; }
        label { display: block; font-weight: 600; color: #333; margin-bottom: 8px; font-size: 15px; }
        label .required { color: #dc3545; }
        input[type="text"], input[type="email"], input[type="password"], input[type="tel"], select, textarea { width: 100%; padding: 12px 15px; border: 2px solid #ddd; border-radius: 8px; font-size: 15px; transition: border-color 0.3s; font-family: inherit; }
        input:focus, select:focus, textarea:focus { outline: none; border-color: #667eea; }
        textarea { resize: vertical; min-height: 100px; }
        .radio-group { display: flex; gap: 20px; }
        .radio-option { display: flex; align-items: center; gap: 8px; }
        .radio-option input[type="radio"] { width: auto; }
        .btn { padding: 12px 30px; border: none; border-radius: 8px; font-size: 15px; font-weight: 500; cursor: pointer; transition: all 0.3s; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-secondary { background: #6c757d; color: white; }
        .btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
        .form-actions { display: flex; gap: 10px; margin-top: 30px; padding-top: 20px; border-top: 2px solid #dee2e6; }
        .alert { padding: 15px 20px; margin-bottom: 20px; border-radius: 8px; font-size: 15px; }
        .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .checkbox-group { display: flex; align-items: center; gap: 10px; }
        .checkbox-group input[type="checkbox"] { width: auto; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>${isEdit ? '✏️ Chỉnh sửa' : '➕ Thêm mới'} người dùng</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">✗ ${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/users/${isEdit ? 'edit' : 'create'}" onsubmit="return validateForm()">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${user.id}">
                </c:if>

                <div class="form-group">
                    <label for="email">📧 Email <span class="required">*</span></label>
                    <input type="email" id="email" name="email" value="${user.email}" required ${isEdit ? 'readonly' : ''}>
                    <small style="color: #6c757d;">${isEdit ? 'Email không thể thay đổi' : 'Email sẽ dùng để đăng nhập'}</small>
                </div>

                <c:if test="${!isEdit}">
                    <div class="form-group">
                        <label for="password">🔒 Mật khẩu <span class="required">*</span></label>
                        <input type="password" id="password" name="password" required minlength="6">
                        <small style="color: #6c757d;">Tối thiểu 6 ký tự</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">🔒 Xác nhận mật khẩu <span class="required">*</span></label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                    </div>
                </c:if>

                <div class="form-group">
                    <label for="fullName">👤 Họ và tên <span class="required">*</span></label>
                    <input type="text" id="fullName" name="fullName" value="${user.fullName}" required>
                </div>

                <div class="form-group">
                    <label>⚧ Giới tính <span class="required">*</span></label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio" id="male" name="gender" value="Male" ${user.gender == 'Male' || empty user ? 'checked' : ''} required>
                            <label for="male" style="margin: 0;">👨 Nam</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="female" name="gender" value="Female" ${user.gender == 'Female' ? 'checked' : ''}>
                            <label for="female" style="margin: 0;">👩 Nữ</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="other" name="gender" value="Other" ${user.gender == 'Other' ? 'checked' : ''}>
                            <label for="other" style="margin: 0;">⚧ Khác</label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="phone">📱 Số điện thoại</label>
                    <input type="tel" id="phone" name="phone" value="${user.phone}" pattern="[0-9]{10,11}" placeholder="0123456789">
                    <small style="color: #6c757d;">10-11 chữ số</small>
                </div>

                <div class="form-group">
                    <label for="address">🏠 Địa chỉ</label>
                    <textarea id="address" name="address" rows="3">${user.address}</textarea>
                </div>

                <div class="form-group">
                    <label for="avatarUrl">🖼️ Avatar URL</label>
                    <input type="text" id="avatarUrl" name="avatarUrl" value="${user.avatarUrl}" placeholder="https://example.com/avatar.jpg">
                </div>

                <div class="form-group">
                    <label for="roleId">👑 Role <span class="required">*</span></label>
                    <select id="roleId" name="roleId" required>
                        <option value="">-- Chọn role --</option>
                        <c:forEach var="role" items="${roles}">
                            <c:if test="${!role.deleted}">
                                <option value="${role.id}" 
                                    ${user.roleId == role.id ? 'selected' : ''}>
                                    <c:out value="${role.name}"/>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <div class="checkbox-group">
                        <input type="checkbox" id="status" name="status" value="true" ${user.status || empty user ? 'checked' : ''}>
                        <label for="status" style="margin: 0;">✓ Tài khoản đang hoạt động</label>
                    </div>
                    <small style="color: #6c757d;">Bỏ check để khóa tài khoản</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">${isEdit ? '💾 Cập nhật' : '➕ Thêm mới'}</button>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">✗ Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        <c:set var="isEditMode" value="${isEdit}" />
        var isEdit = <c:out value="${isEditMode}" default="false" />;
        
        function validateForm() {
            if (!isEdit) {
                // Validate password match for new users
                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                
                if (password !== confirmPassword) {
                    alert('Mật khẩu xác nhận không khớp!');
                    return false;
                }
            }

            var phone = document.getElementById('phone').value;
            if (phone && !/^[0-9]{10,11}$/.test(phone)) {
                alert('Số điện thoại không hợp lệ! Phải là 10-11 chữ số.');
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
