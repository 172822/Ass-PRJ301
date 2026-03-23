<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${room != null ? 'Sửa' : 'Thêm'} phòng</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; }
        .header { background: #fff; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 400px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], input[type="number"], select { width: 100%; padding: 8px; }
        .btn { padding: 8px 16px; border-radius: 4px; border: none; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn-primary { background: #2563eb; color: #fff; }
        .error { color: #dc2626; margin-bottom: 8px; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>${room != null ? 'Sửa' : 'Thêm'} phòng</h1>
                <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
                <div class="card">
                    <c:set var="pickBoardingHouseId" value="${room != null ? room.boardingHouseId : prefillBoardingHouseId}"/>
                    <form action="${pageContext.request.contextPath}/room" method="post">
                        <c:if test="${room != null}"><input type="hidden" name="id" value="${room.id}"></c:if>
                        <div class="form-group">
                            <label>Nhà trọ</label>
                            <select name="boardingHouseId" required>
                                <c:forEach items="${boardinghouses}" var="bh">
                                    <option value="${bh.id}" ${pickBoardingHouseId != null && pickBoardingHouseId == bh.id ? 'selected' : ''}>${bh.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <c:if test="${empty boardinghouses}">
                            <p class="error">Chưa có nhà trọ. Vui lòng tạo nhà trọ trước khi thêm phòng.</p>
                        </c:if>
                        <div class="form-group">
                            <label>Mã phòng</label>
                            <input type="text" name="roomCode" value="${room != null ? room.roomCode : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Giá (VNĐ/tháng)</label>
                            <input type="number" name="price" step="1000" value="${room != null ? room.price : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Số người tối đa</label>
                            <input type="number" name="maxPerson" min="1" value="${room != null ? room.maxPerson : 1}" required>
                        </div>
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="status">
                                <option value="EMPTY" ${room != null && room.status == 'EMPTY' ? 'selected' : ''}>Trống</option>
                                <option value="RENTED" ${room != null && room.status == 'RENTED' ? 'selected' : ''}>Đã thuê</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary" <c:if test="${empty boardinghouses}">disabled</c:if>>Lưu</button>
                        <a href="${pageContext.request.contextPath}/room" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
