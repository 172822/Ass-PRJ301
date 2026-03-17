<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm hợp đồng</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 480px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], input[type="number"], input[type="date"], select { width: 100%; padding: 8px; }
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
                <h1>Thêm hợp đồng</h1>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/contract" method="post">
                        <input type="hidden" name="action" value="save">
                        <div class="form-group">
                            <label>Phòng</label>
                            <select name="roomId" required>
                                <c:forEach items="${rooms}" var="r">
                                    <option value="${r.id}">${r.roomCode} - ${r.price} VNĐ/tháng</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Ngày bắt đầu</label>
                            <input type="date" name="startDate" required>
                        </div>
                        <div class="form-group">
                            <label>Ngày kết thúc</label>
                            <input type="date" name="endDate" required>
                        </div>
                        <div class="form-group">
                            <label>Tiền cọc</label>
                            <input type="number" name="deposit" step="1000" value="0">
                        </div>
                        <div class="form-group">
                            <label>Giá thuê/tháng</label>
                            <input type="number" name="rentPrice" step="1000" required>
                        </div>
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="status">
                                <option value="active">Đang hoạt động</option>
                                <option value="expired">Hết hạn</option>
                                <option value="cancelled">Đã hủy</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Tạo hợp đồng</button>
                        <a href="${pageContext.request.contextPath}/contract" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
