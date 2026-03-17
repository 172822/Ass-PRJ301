<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết hợp đồng</title>
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
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        .btn { display: inline-block; padding: 8px 16px; border-radius: 4px; text-decoration: none; font-size: 0.875rem; border: none; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .btn-small { padding: 4px 10px; font-size: 0.8rem; }
        .form-inline { display: inline-flex; gap: 8px; align-items: center; margin-top: 12px; }
        .form-inline select { padding: 6px; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Hợp đồng #${contract.id}</h1>
                <div class="card">
                    <p><strong>Phòng:</strong> ${room != null ? room.roomCode : contract.roomId}</p>
                    <p><strong>Bắt đầu:</strong> ${contract.startDate}</p>
                    <p><strong>Kết thúc:</strong> ${contract.endDate}</p>
                    <p><strong>Tiền cọc:</strong> <fmt:formatNumber value="${contract.deposit}" type="number"/> VNĐ</p>
                    <p><strong>Giá thuê:</strong> <fmt:formatNumber value="${contract.rentPrice}" type="number"/> VNĐ/tháng</p>
                    <p><strong>Trạng thái:</strong> ${contract.status}</p>
                    <c:if test="${sessionScope.user.role != 'tenant'}">
                        <a href="${pageContext.request.contextPath}/contract?action=edit&id=${contract.id}" class="btn btn-primary">Sửa hợp đồng</a>
                    </c:if>
                </div>
                <div class="card">
                    <h3>Người thuê</h3>
                    <table>
                        <tr><th>Họ tên</th><th>Email</th><th>SĐT</th><c:if test="${sessionScope.user.role != 'tenant'}"><th>Thao tác</th></c:if></tr>
                        <c:forEach items="${tenantUsers}" var="u">
                            <tr>
                                <td>${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <c:if test="${sessionScope.user.role != 'tenant'}">
                                    <td>
                                        <form action="${pageContext.request.contextPath}/contract" method="post" style="display:inline;" onsubmit="return confirm('Gỡ người thuê?');">
                                            <input type="hidden" name="action" value="removeTenant">
                                            <input type="hidden" name="contractId" value="${contract.id}">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <button type="submit" class="btn btn-danger btn-small">Gỡ</button>
                                        </form>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </table>
                    <c:if test="${sessionScope.user.role != 'tenant' && not empty availableTenants}">
                        <form action="${pageContext.request.contextPath}/contract" method="post" class="form-inline">
                            <input type="hidden" name="action" value="addTenant">
                            <input type="hidden" name="contractId" value="${contract.id}">
                            <select name="userId" required>
                                <option value="">-- Chọn người thuê --</option>
                                <c:forEach items="${availableTenants}" var="t">
                                    <option value="${t.id}">${t.fullName} - ${t.email}</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-primary btn-small">Thêm người thuê</button>
                        </form>
                    </c:if>
                </div>
                <a href="${pageContext.request.contextPath}/contract" class="btn">Quay lại</a>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
