<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Edit' : 'Add New'} User</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; min-height: 100vh; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden; }
        .header { background: #2c3e50; color: white; padding: 20px; text-align: center; }
        .header h1 { font-size: 28px; font-weight: 600; }
        .content { padding: 40px; }
        .form-group { margin-bottom: 25px; }
        label { display: block; font-weight: 600; color: #333; margin-bottom: 8px; font-size: 15px; }
        label .required { color: #dc3545; }
        input[type="text"], input[type="email"], input[type="password"], input[type="tel"], select, textarea { width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; transition: border-color 0.2s; font-family: inherit; }
        input:focus, select:focus, textarea:focus { outline: none; border-color: #3498db; }
        textarea { resize: vertical; min-height: 100px; }
        .radio-group { display: flex; gap: 20px; }
        .radio-option { display: flex; align-items: center; gap: 8px; }
        .radio-option input[type="radio"] { width: auto; }
        .btn { padding: 10px 24px; border: none; border-radius: 4px; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.2s; text-decoration: none; display: inline-block; margin-right: 10px; }
        .btn-primary { background: #3498db; color: white; }
        .btn-primary:hover { background: #2980b9; }
        .btn-secondary { background: #95a5a6; color: white; }
        .btn-secondary:hover { background: #7f8c8d; }
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
            <h1>${isEdit ? 'Edit' : 'Add New'} User</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/admin/users/${isEdit ? 'edit' : 'create'}" accept-charset="UTF-8" onsubmit="return validateForm()">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${user.id}">
                </c:if>

                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <input type="email" id="email" name="email" value="${user.email}" required ${isEdit ? 'readonly' : ''}>
                    <small style="color: #6c757d;">${isEdit ? 'Email cannot be changed' : 'Email will be used for login'}</small>
                </div>

                <c:if test="${!isEdit}">
                    <div class="form-group">
                        <label for="password">Password <span class="required">*</span></label>
                        <input type="password" id="password" name="password" required minlength="6">
                        <small style="color: #6c757d;">Minimum 6 characters</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password <span class="required">*</span></label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                    </div>
                </c:if>

                <div class="form-group">
                    <label for="fullName">Full Name <span class="required">*</span></label>
                    <input type="text" id="fullName" name="fullName" value="${user.fullName}" required pattern="^[a-zA-Z\s]+$">
                    <small style="color: #6c757d;">Only letters and spaces allowed (2-100 characters)</small>
                </div>

                <div class="form-group">
                    <label>Gender <span class="required">*</span></label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio" id="male" name="gender" value="MALE" ${user.gender == 'MALE' || empty user ? 'checked' : ''} required>
                            <label for="male" style="margin: 0;">Male</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="female" name="gender" value="FEMALE" ${user.gender == 'FEMALE' ? 'checked' : ''}>
                            <label for="female" style="margin: 0;">Female</label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone" value="${user.phone}" pattern="[0-9]{10,11}" placeholder="0123456789">
                    <small style="color: #6c757d;">10-11 digits</small>
                </div>

                <div class="form-group">
                    <label for="address">Address</label>
                    <textarea id="address" name="address" rows="3">${user.address}</textarea>
                </div>

                <div class="form-group">
                    <label for="roleId">Role <span class="required">*</span></label>
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
                        <label for="status" style="margin: 0;">Account is active</label>
                    </div>
                    <small style="color: #6c757d;">Uncheck to lock account</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">${isEdit ? 'Update' : 'Create'}</button>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        <c:set var="isEditMode" value="${isEdit}" />
        var isEdit = <c:out value="${isEditMode}" default="false" />;
        
        function validateForm() {
            // Validate Full Name
            var fullName = document.getElementById('fullName').value.trim();
            if (fullName.length < 2 || fullName.length > 100) {
                alert('Full name must be between 2 and 100 characters!');
                return false;
            }
            
            // Check only letters and spaces
            var namePattern = /^[a-zA-Z\s]+$/;
            if (!namePattern.test(fullName)) {
                alert('Full name can only contain letters and spaces!');
                return false;
            }

            // Validate Email format
            var email = document.getElementById('email').value.trim();
            var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                alert('Invalid email format!');
                return false;
            }

            if (!isEdit) {
                // Validate password for new users
                var password = document.getElementById('password').value;
                var confirmPassword = document.getElementById('confirmPassword').value;
                
                if (password.length < 6) {
                    alert('Password must be at least 6 characters!');
                    return false;
                }

                if (password !== confirmPassword) {
                    alert('Password confirmation does not match!');
                    return false;
                }
            }

            // Validate Gender
            var genderSelected = document.querySelector('input[name="gender"]:checked');
            if (!genderSelected) {
                alert('Please select gender!');
                return false;
            }

            // Validate Phone Number (if provided)
            var phone = document.getElementById('phone').value.trim();
            if (phone && !/^[0-9]{10,11}$/.test(phone)) {
                alert('Invalid phone number! Must be 10-11 digits.');
                return false;
            }

            // Validate Role
            var roleId = document.getElementById('roleId').value;
            if (!roleId) {
                alert('Please select a role!');
                return false;
            }

            return true;
        }

        // Real-time validation
        document.addEventListener('DOMContentLoaded', function() {
            // Email duplicate check
            var emailInput = document.getElementById('email');
            var emailError = document.createElement('small');
            emailError.style.color = 'red';
            emailError.style.display = 'none';
            emailInput.parentNode.appendChild(emailError);

            if (emailInput && !isEdit) {
                emailInput.addEventListener('blur', function() {
                    var email = this.value.trim();
                    if (email) {
                        // Check email exists via AJAX
                        fetch('${pageContext.request.contextPath}/admin/users/check-email?email=' + encodeURIComponent(email))
                            .then(response => response.json())
                            .then(data => {
                                if (data.exists) {
                                    emailError.textContent = 'Email already exists!';
                                    emailError.style.display = 'block';
                                    emailInput.setCustomValidity('Email already exists');
                                } else {
                                    emailError.style.display = 'none';
                                    emailInput.setCustomValidity('');
                                }
                            })
                            .catch(error => {
                                console.error('Error checking email:', error);
                            });
                    }
                });
            }

            // Phone number validation on input
            var phoneInput = document.getElementById('phone');
            if (phoneInput) {
                phoneInput.addEventListener('input', function(e) {
                    this.value = this.value.replace(/[^0-9]/g, '');
                });
            }

            // Full name validation
            var fullNameInput = document.getElementById('fullName');
            if (fullNameInput) {
                // Remove non-letter characters on input
                fullNameInput.addEventListener('input', function(e) {
                    this.value = this.value.replace(/[^a-zA-Z\s]/g, '');
                });
                
                fullNameInput.addEventListener('blur', function() {
                    var value = this.value.trim();
                    if (value && (value.length < 2 || value.length > 100)) {
                        this.setCustomValidity('Full name must be between 2 and 100 characters');
                    } else if (value && !/^[a-zA-Z\s]+$/.test(value)) {
                        this.setCustomValidity('Full name can only contain letters and spaces');
                    } else {
                        this.setCustomValidity('');
                    }
                });
            }

            // Password match validation for create form
            if (!isEdit) {
                var confirmPasswordInput = document.getElementById('confirmPassword');
                if (confirmPasswordInput) {
                    confirmPasswordInput.addEventListener('input', function() {
                        var password = document.getElementById('password').value;
                        if (this.value !== password) {
                            this.setCustomValidity('Passwords do not match');
                        } else {
                            this.setCustomValidity('');
                        }
                    });
                }
            }
        });
    </script>
</body>
</html>
