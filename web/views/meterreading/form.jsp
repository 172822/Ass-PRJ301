<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${meterreading != null ? 'Sửa' : 'Thêm'} chỉ số</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { width: 200px; background: #1e293b; padding: 16px; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; }
        .main { flex: 1; display: flex; flex-direction: column; }
        .header { background: #fff; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 400px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], input[type="number"], select { width: 100%; padding: 8px; }
        .btn { padding: 8px 16px; border-radius: 4px; border: none; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn-primary { background: #2563eb; color: #fff; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>${meterreading != null ? 'Sửa' : 'Thêm'} chỉ số điện nước</h1>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/meterreading" method="post">
                        <c:if test="${meterreading != null}"><input type="hidden" name="id" value="${meterreading.id}"></c:if>
                        <div class="form-group">
                            <label>Phòng</label>
                            <select name="roomId" required>
                                <c:forEach items="${rooms}" var="r">
                                    <option value="${r.id}" ${meterreading != null && meterreading.roomId == r.id ? 'selected' : ''}>${r.roomCode}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Tháng</label>
                            <input type="number" name="month" min="1" max="12" value="${meterreading != null ? meterreading.month : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Năm</label>
                            <input type="number" name="year" min="2020" value="${meterreading != null ? meterreading.year : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Điện chỉ số đầu</label>
                            <input type="number" name="electricStart" min="0" value="${meterreading != null ? meterreading.electricStart : 0}">
                        </div>
                        <div class="form-group">
                            <label>Điện chỉ số cuối</label>
                            <input type="number" name="electricEnd" min="0" value="${meterreading != null ? meterreading.electricEnd : 0}">
                        </div>
                        <div class="form-group">
                            <label>Nước chỉ số đầu</label>
                            <input type="number" name="waterStart" min="0" value="${meterreading != null ? meterreading.waterStart : 0}">
                        </div>
                        <div class="form-group">
                            <label>Nước chỉ số cuối</label>
                            <input type="number" name="waterEnd" min="0" value="${meterreading != null ? meterreading.waterEnd : 0}">
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/meterreading" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
