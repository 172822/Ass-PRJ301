<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Quản lý trọ</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { width: 200px; background: #1e293b; padding: 16px; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; }
        .sidebar a:hover { color: #fff; }
        .main { flex: 1; display: flex; flex-direction: column; }
        .header { background: #fff; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .header h2 { margin: 0; font-size: 1.25rem; }
        .user-info a { margin-left: 12px; color: #2563eb; text-decoration: none; }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; color: #64748b; }
        .stats { display: flex; gap: 16px; flex-wrap: wrap; margin-bottom: 24px; }
        .stat { background: #fff; padding: 20px; border-radius: 8px; min-width: 140px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .stat span { font-size: 1.5rem; font-weight: bold; color: #2563eb; }
        .card { background: #fff; border-radius: 8px; padding: 20px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="common/header.jsp"/>
            <div class="content">
                <h1>Dashboard</h1>
                <c:if test="${sessionScope.user.role == 'admin' || sessionScope.user.role == 'landlord'}">
                    <div class="stats">
                        <div class="stat">Nhà trọ: <span>${boardingHouseCount}</span></div>
                        <div class="stat">Phòng: <span>${roomCount}</span></div>
                        <div class="stat">Hợp đồng: <span>${contractCount}</span></div>
                        <div class="stat">Hóa đơn: <span>${invoiceCount}</span></div>
                    </div>
                </c:if>
                <c:if test="${sessionScope.user.role == 'tenant'}">
                    <div class="card">
                        <h3>Hợp đồng của tôi</h3>
                        <c:choose>
                            <c:when test="${empty contracts}"><p>Chưa có hợp đồng.</p></c:when>
                            <c:otherwise>
                                <table>
                                    <tr><th>ID</th><th>Phòng</th><th>Bắt đầu</th><th>Kết thúc</th><th>Trạng thái</th></tr>
                                    <c:forEach items="${contracts}" var="c">
                                        <tr>
                                            <td>${c.id}</td>
                                            <td>${c.roomId}</td>
                                            <td>${c.startDate}</td>
                                            <td>${c.endDate}</td>
                                            <td>${c.status}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card">
                        <h3>Hóa đơn chưa thanh toán</h3>
                        <c:choose>
                            <c:when test="${empty invoices}"><p>Không có hóa đơn chưa thanh toán.</p></c:when>
                            <c:otherwise>
                                <table>
                                    <tr><th>Tháng/Năm</th><th>Tổng tiền</th><th>Trạng thái</th></tr>
                                    <c:forEach items="${invoices}" var="inv">
                                        <tr><td>${inv.month}/${inv.year}</td><td>${inv.totalPrice}</td><td>${inv.status}</td></tr>
                                    </c:forEach>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
            <jsp:include page="common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
