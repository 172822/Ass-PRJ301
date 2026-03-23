<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; }
        .sidebar a:hover { color: #fff; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; }
        .header { background: #fff; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .header h2 { margin: 0; font-size: 1.25rem; }
        .user-info a { margin-left: 12px; color: #2563eb; text-decoration: none; }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; color: #64748b; }
        .stats { display: flex; gap: 16px; flex-wrap: wrap; margin-bottom: 24px; }
        .stat { background: #fff; padding: 20px; border-radius: 8px; min-width: 140px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .stat span { font-size: 1.5rem; font-weight: bold; color: #2563eb; }
        .stat-link { text-decoration: none; color: inherit; display: block; transition: box-shadow 0.2s; }
        .stat-link:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
        .quick-links { margin-bottom: 24px; }
        .quick-links a { color: #2563eb; text-decoration: none; margin-right: 8px; }
        .quick-links a:hover { text-decoration: underline; }
        .btn-small { font-size: 0.875rem; color: #2563eb; text-decoration: none; margin-left: 8px; }
        .btn-small:hover { text-decoration: underline; }
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
                <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LANDLORD'}">
                    <div class="stats">
                        <a href="${pageContext.request.contextPath}/boardinghouse" class="stat stat-link">Nhà trọ: <span>${boardingHouseCount}</span></a>
                        <a href="${pageContext.request.contextPath}/room" class="stat stat-link">Phòng: <span>${roomCount}</span></a>
                        <a href="${pageContext.request.contextPath}/contract" class="stat stat-link">Hợp đồng: <span>${contractCount}</span></a>
                        <a href="${pageContext.request.contextPath}/invoice" class="stat stat-link">Hóa đơn: <span>${invoiceCount}</span></a>
                    </div>
                    <p class="quick-links">
                        <a href="${pageContext.request.contextPath}/boardinghouse?action=add">Thêm nhà trọ</a> |
                        <a href="${pageContext.request.contextPath}/room?action=add">Thêm phòng</a> |
                        <a href="${pageContext.request.contextPath}/contract?action=add">Thêm hợp đồng</a> |
                        <a href="${pageContext.request.contextPath}/meterreading?action=add">Thêm số điện nước</a> |
                        <a href="${pageContext.request.contextPath}/invoice?action=add">Tạo hóa đơn</a>
                    </p>
                </c:if>
                <c:if test="${sessionScope.user.role == 'STUDENT'}">
                    <div class="card">
                        <h3>Hợp đồng của tôi <a href="${pageContext.request.contextPath}/contract" class="btn-small">Xem tất cả</a></h3>
                        <c:choose>
                            <c:when test="${empty contracts}"><p>Chưa có hợp đồng.</p></c:when>
                            <c:otherwise>
                                <table>
                                    <tr><th>ID</th><th>Phòng</th><th>Bắt đầu</th><th>Kết thúc</th><th>Trạng thái</th><th></th></tr>
                                    <c:forEach items="${contracts}" var="c">
                                        <tr>
                                            <td>${c.id}</td>
                                            <td>${c.roomId}</td>
                                            <td>${c.startDate}</td>
                                            <td>${c.endDate}</td>
                                            <td>${c.status}</td>
                                            <td><a href="${pageContext.request.contextPath}/contract?action=view&id=${c.id}">Xem</a></td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card">
                        <h3>Hóa đơn chưa thanh toán <a href="${pageContext.request.contextPath}/invoice" class="btn-small">Xem tất cả</a></h3>
                        <c:choose>
                            <c:when test="${empty invoices}"><p>Không có hóa đơn chưa thanh toán.</p></c:when>
                            <c:otherwise>
                                <table>
                                    <tr><th>Tháng/Năm</th><th>Tổng tiền</th><th>Trạng thái</th><th></th></tr>
                                    <c:forEach items="${invoices}" var="inv">
                                        <tr>
                                            <td>${inv.month}/${inv.year}</td>
                                            <td>${inv.totalPrice}</td>
                                            <td>${inv.status}</td>
                                            <td><a href="${pageContext.request.contextPath}/invoice?action=view&id=${inv.id}">Xem</a></td>
                                        </tr>
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
