<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết hóa đơn</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 16px; }
        .btn { padding: 8px 16px; border-radius: 4px; text-decoration: none; display: inline-block; background: #2563eb; color: #fff; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Hóa đơn #${invoice.id}</h1>
                <div class="card">
                    <p><strong>Phòng:</strong> ${room != null ? room.roomCode : invoice.roomId}</p>
                    <p><strong>Kỳ:</strong> ${invoice.month}/${invoice.year}</p>
                    <p><strong>Tiền phòng:</strong> <fmt:formatNumber value="${invoice.roomPrice}" type="number"/> VNĐ</p>
                    <p><strong>Tiền điện:</strong> <fmt:formatNumber value="${invoice.electricPrice}" type="number"/> VNĐ</p>
                    <p><strong>Tiền nước:</strong> <fmt:formatNumber value="${invoice.waterPrice}" type="number"/> VNĐ</p>
                    <p><strong>Tổng cộng:</strong> <fmt:formatNumber value="${invoice.totalPrice}" type="number"/> VNĐ</p>
                    <p><strong>Trạng thái:</strong> ${invoice.status}</p>
                </div>
                <a href="${pageContext.request.contextPath}/invoice" class="btn">Quay lại</a>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
