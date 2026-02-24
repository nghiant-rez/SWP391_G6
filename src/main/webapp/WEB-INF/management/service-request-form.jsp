<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1.0">
    <title>Tao yeu cau dich vu</title>
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
            max-width: 800px; 
            margin: 20px auto; 
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

<%@ include file="/WEB-INF/includes/navbar.jsp" %>

    <div class="container">
        <div class="header">
            <h1>Tao yeu cau dich vu</h1>
        </div>

        <div class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <form method="post" 
                  action="${pageContext.request.contextPath}/management/service-requests/create"
                  accept-charset="UTF-8">

                <div class="form-group">
                    <label for="subject">
                        Chu de <span class="required">*</span>
                    </label>
                    <input type="text" 
                           id="subject" 
                           name="subject" 
                           value="<c:out value='${serviceRequest.subject}'/>"
                           required 
                           maxlength="200"
                           placeholder="Nhap chu de yeu cau...">
                    <div class="hint">Toi da 200 ky tu</div>
                </div>

                <div class="form-group">
                    <label for="description">
                        Mo ta chi tiet <span class="required">*</span>
                    </label>
                    <textarea id="description" 
                              name="description" 
                              required
                              maxlength="2000"
                              placeholder="Mo ta chi tiet van de hoac yeu cau cua ban..."><c:out value="${serviceRequest.description}"/></textarea>
                    <div class="hint">Toi da 2000 ky tu</div>
                </div>

                <div class="form-group">
                    <label for="requestType">
                        Loai yeu cau
                        <span class="required">*</span>
                    </label>
                    <select id="requestType"
                            name="requestType"
                            required>
                        <option value="">
                            -- Chon loai --
                        </option>
                        <option value="REPAIR"
                            ${serviceRequest.requestType == 'REPAIR'
                                ? 'selected' : ''}>
                            Sua chua
                        </option>
                        <option value="MAINTENANCE"
                            ${serviceRequest.requestType == 'MAINTENANCE'
                                ? 'selected' : ''}>
                            Bao tri
                        </option>
                        <option value="COMPLAINT"
                            ${serviceRequest.requestType == 'COMPLAINT'
                                ? 'selected' : ''}>
                            Khieu nai
                        </option>
                        <option value="INQUIRY"
                            ${serviceRequest.requestType == 'INQUIRY'
                                ? 'selected' : ''}>
                            Yeu cau thong tin
                        </option>
                        <option value="WARRANTY"
                            ${serviceRequest.requestType == 'WARRANTY'
                                ? 'selected' : ''}>
                            Bao hanh
                        </option>
                        <option value="OTHER"
                            ${serviceRequest.requestType == 'OTHER'
                                ? 'selected' : ''}>
                            Khac
                        </option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="deviceId">Thiet bi lien quan</label>
                    <select id="deviceId" name="deviceId">
                        <option value="">-- Chon thiet bi (neu co) --</option>
                        <c:forEach var="device" items="${devices}">
                            <option value="${device[0]}" 
                                ${serviceRequest.deviceId != null && serviceRequest.deviceId == device[0] ? 'selected' : ''}>
                                <c:out value="${device[2]}"/> 
                                (<c:out value="${device[1]}"/>)
                            </option>
                        </c:forEach>
                    </select>
                    <div class="hint">
                        De trong neu khong lien quan den thiet bi cu the
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        Gui yeu cau
                    </button>
                    <a href="${pageContext.request.contextPath}/my-service-requests" 
                       class="btn btn-secondary">
                        Huy bo
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
