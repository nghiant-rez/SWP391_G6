<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Tao cong viec moi</title>
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
        .hint { 
            font-size: 13px; 
            color: #6c757d; 
            margin-top: 5px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Tao cong viec moi</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <form method="post" 
                  action="${pageContext.request.contextPath}/management/tasks/create"
                  accept-charset="UTF-8">

                <div class="form-group">
                    <label for="title">
                        Tieu de <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="title" 
                           name="title" 
                           value="<c:out value='${task.title}'/>"
                           required 
                           maxlength="200"
                           placeholder="Nhap tieu de cong viec...">
                    <div class="hint">Toi da 200 ky tu</div>
                </div>

                <div class="form-group">
                    <label for="description">Mo ta</label>
                    <textarea id="description" 
                              name="description" 
                              maxlength="1000"
                              placeholder="Nhap mo ta chi tiet cong viec..."><c:out value="${task.description}"/></textarea>
                    <div class="hint">Toi da 1000 ky tu</div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="assigneeId">
                            Nguoi thuc hien <span class="required">*</span>
                        </label>
                        <select id="assigneeId" name="assigneeId" required>
                            <option value="">-- Chon nhan vien --</option>
                            <c:forEach var="staff" items="${staffList}">
                                <option value="${staff.id}" 
                                    ${task.assigneeId == staff.id ? 'selected' : ''}>
                                    <c:out value="${staff.fullName}"/> 
                                    (<c:out value="${staff.email}"/>)
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="taskType">
                            Loai cong viec <span class="required">*</span>
                        </label>
                        <select id="taskType" name="taskType" required>
                            <option value="">-- Chon loai --</option>
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
                                ${empty task.priority || task.priority == 'MEDIUM' ? 'selected' : ''}>
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
                        <label for="dueDate">Han hoan thanh</label>
                        <input type="datetime-local" 
                               id="dueDate" 
                               name="dueDate"
                               value="${task.dueDate != null ? task.dueDate.toString().substring(0, 16) : ''}">
                        <div class="hint">De trong neu khong co han cu the</div>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Tao cong viec
                    </button>
                    <a href="${pageContext.request.contextPath}/management/tasks" 
                       class="btn btn-secondary">
                        Huy bo
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
