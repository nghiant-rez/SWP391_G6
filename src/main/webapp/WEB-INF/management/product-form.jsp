<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Product</title>
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
            max-width: 900px;
            margin: 20px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 30px;
        }
        .page-header h1 {
            font-size: 28px;
            font-weight: 600;
        }
        .form-container {
            padding: 30px;
        }
        .alert {
            padding: 12px 20px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-size: 14px;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
        }
        .required {
            color: #e74c3c;
        }
        input[type="text"],
        input[type="number"],
        select,
        textarea {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            font-family: inherit;
        }
        input:focus,
        select:focus,
        textarea:focus {
            outline: none;
            border-color: #667eea;
        }
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }
        .btn-primary {
            background: #27ae60;
            color: white;
        }
        .btn-primary:hover {
            background: #229954;
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
            gap: 10px;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
        }
        .help-text {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }
        .error-message {
            color: #e74c3c;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }
        input.invalid,
        textarea.invalid,
        select.invalid {
            border-color: #e74c3c;
        }
        input.valid,
        textarea.valid,
        select.valid {
            border-color: #27ae60;
        }
        .char-counter {
            font-size: 12px;
            color: #6c757d;
            text-align: right;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="page-header">
            <h1>Add New Product</h1>
        </div>

        <div class="form-container">
            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/management/products/create">
                <div class="form-group">
                    <label for="categoryId">
                        Category <span class="required">*</span>
                    </label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="">-- Select Category --</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}"><c:out value="${cat.name}"/></option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="name">
                        Product Name <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           placeholder="Enter product name"
                           required
                           minlength="3"
                           maxlength="200"
                           pattern="^[a-zA-Z0-9\s\-\.\/\(\)]+$"
                           title="Product name must be 3-200 characters (letters, numbers, spaces, and basic punctuation only)">
                    <div class="help-text">3-200 characters, letters, numbers, spaces, and basic punctuation</div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="model">Model</label>
                        <input type="text" 
                               id="model" 
                               name="model" 
                               placeholder="e.g., XYZ-123"
                               maxlength="50"
                               pattern="^[a-zA-Z0-9\s\-]+$"
                               title="Model should contain only letters, numbers, spaces, and hyphens">
                        <div class="help-text">Max 50 characters</div>
                    </div>

                    <div class="form-group">
                        <label for="brand">Brand</label>
                        <input type="text" 
                               id="brand" 
                               name="brand" 
                               placeholder="e.g., Samsung, Dell"
                               maxlength="100"
                               pattern="^[a-zA-Z0-9\s\-&\.]+$"
                               title="Brand should contain only letters, numbers, spaces, hyphens, and &">
                        <div class="help-text">Max 100 characters</div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="basePrice">
                            Base Price ($) <span class="required">*</span>
                        </label>
                        <input type="number" 
                               id="basePrice" 
                               name="basePrice" 
                               step="0.01"
                               min="0"
                               max="999999.99"
                               placeholder="0.00"
                               required
                               title="Price must be between 0 and 999,999.99">
                        <div class="help-text">Enter a valid price (0.00 - 999,999.99)</div>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status">
                            <option value="ACTIVE" selected>Active</option>
                            <option value="DISCONTINUED">Discontinued</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="imageUrl">Image URL</label>
                    <input type="url" 
                           id="imageUrl" 
                           name="imageUrl" 
                           placeholder="https://example.com/image.jpg"
                           maxlength="500"
                           pattern="https?://.+"
                           title="Please enter a valid URL starting with http:// or https://">
                    <div class="help-text">Enter a valid URL to the product image (max 500 characters)</div>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" 
                              name="description" 
                              placeholder="Enter product description"
                              maxlength="2000"></textarea>
                    <div class="help-text">Max 2000 characters</div>
                </div>

                <div class="form-group">
                    <label for="specifications">Specifications</label>
                    <textarea id="specifications" 
                              name="specifications" 
                              placeholder="Enter technical specifications"
                              maxlength="5000"></textarea>
                    <div class="help-text">Enter technical details, features, etc. (max 5000 characters)</div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Create Product
                    </button>
                    <a href="${pageContext.request.contextPath}/management/products" 
                       class="btn btn-secondary">
                        Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Real-time validation
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('form');
            const inputs = form.querySelectorAll('input, select, textarea');

            // Add character counters for textareas
            document.querySelectorAll('textarea[maxlength]').forEach(textarea => {
                const maxLength = textarea.getAttribute('maxlength');
                const counter = document.createElement('div');
                counter.className = 'char-counter';
                counter.textContent = `0 / ${maxLength} characters`;
                textarea.parentNode.appendChild(counter);

                textarea.addEventListener('input', function() {
                    const currentLength = this.value.length;
                    counter.textContent = `${currentLength} / ${maxLength} characters`;
                    if (currentLength > maxLength * 0.9) {
                        counter.style.color = '#e74c3c';
                    } else {
                        counter.style.color = '#6c757d';
                    }
                });
            });

            // Real-time validation for inputs
            inputs.forEach(input => {
                input.addEventListener('blur', function() {
                    validateField(this);
                });

                input.addEventListener('input', function() {
                    if (this.classList.contains('invalid')) {
                        validateField(this);
                    }
                });
            });

            // Validate field
            function validateField(field) {
                if (field.checkValidity()) {
                    field.classList.remove('invalid');
                    field.classList.add('valid');
                } else {
                    field.classList.remove('valid');
                    field.classList.add('invalid');
                }
            }

            // Form submission validation
            form.addEventListener('submit', function(e) {
                let isValid = true;
                inputs.forEach(input => {
                    validateField(input);
                    if (!input.checkValidity()) {
                        isValid = false;
                    }
                });

                if (!isValid) {
                    e.preventDefault();
                    alert('Please fix all validation errors before submitting.');
                    // Focus on first invalid field
                    const firstInvalid = form.querySelector('.invalid');
                    if (firstInvalid) {
                        firstInvalid.focus();
                    }
                }
            });

            // Price validation - ensure only 2 decimal places
            const priceInput = document.getElementById('basePrice');
            if (priceInput) {
                priceInput.addEventListener('change', function() {
                    if (this.value) {
                        this.value = parseFloat(this.value).toFixed(2);
                    }
                });
            }

            // Trim whitespace on text inputs before submit
            form.addEventListener('submit', function() {
                inputs.forEach(input => {
                    if (input.type === 'text' || input.tagName === 'TEXTAREA') {
                        input.value = input.value.trim();
                    }
                });
            });
        });
    </script>
</body>
</html>
