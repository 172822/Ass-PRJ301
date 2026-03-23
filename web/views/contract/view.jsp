<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết hợp đồng</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; border-radius: 4px; transition: all 0.2s; }
        .sidebar a:hover { color: #fff; }
        .sidebar a.active { background: #2563eb; color: #fff; padding-left: 8px; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; margin-top: 60px; }
        .header { position: fixed; top: 0; right: 0; left: 200px; background: #fff; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); min-height: 60px; z-index: 99; display: flex; justify-content: space-between; align-items: center; gap: 16px; }
        .header h2 { margin: 0; font-size: 1.25rem; flex-shrink: 0; }
        .user-info { display: flex; align-items: center; gap: 12px; }
        .user-info span { color: #334155; }
        .logout-btn { background: #dc2626; color: #fff; border: none; padding: 8px 16px; border-radius: 4px; font-size: 0.875rem; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; }
        .logout-btn:hover { background: #b91c1c; }
        .user-info a { margin-left: 12px; color: #2563eb; text-decoration: none; }
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
                    <c:if test="${sessionScope.user.role != 'STUDENT'}">
                        <a href="${pageContext.request.contextPath}/contract?action=edit&id=${contract.id}" class="btn btn-primary">Sửa hợp đồng</a>
                    </c:if>
                </div>
                <div class="card">
                    <h3>Người thuê</h3>
                    <table>
                        <tr><th>Họ tên</th><th>Email</th><th>SĐT</th><c:if test="${sessionScope.user.role != 'STUDENT'}"><th>Thao tác</th></c:if></tr>
                        <c:forEach items="${tenantUsers}" var="u">
                            <tr>
                                <td>${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <c:if test="${sessionScope.user.role != 'STUDENT'}">
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
                    <c:if test="${sessionScope.user.role != 'STUDENT'}">
                        <form method="get" action="${pageContext.request.contextPath}/contract" style="margin-top:16px;">
                            <input type="hidden" name="action" value="view">
                            <input type="hidden" name="id" value="${contract.id}">
                            <p style="margin:0 0 8px 0;"><strong>Tìm người thuê</strong> (email hoặc SĐT, gần đúng)</p>
                            <div class="form-inline">
                                <input type="text" name="tenantSearch" value="<c:out value='${tenantSearch}'/>" placeholder="Ví dụ: 09, gmail..." style="padding:6px; min-width:240px;">
                                <button type="submit" class="btn btn-primary btn-small">Tìm</button>
                                <c:if test="${not empty tenantSearch}">
                                    <a href="${pageContext.request.contextPath}/contract?action=view&amp;id=${contract.id}" class="btn btn-small" style="background:#e2e8f0;color:#334155;">Xem tất cả</a>
                                </c:if>
                            </div>
                        </form>
                        <c:if test="${not empty tenantSearchHint}">
                            <p style="color:#b45309;font-size:0.9rem;margin-top:8px;"><c:out value="${tenantSearchHint}"/></p>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty availableTenants}">
                                <form action="${pageContext.request.contextPath}/contract" method="post" class="form-inline" style="margin-top:12px;">
                                    <input type="hidden" name="action" value="addTenant">
                                    <input type="hidden" name="contractId" value="${contract.id}">
                                    <input type="hidden" name="tenantSearch" value="<c:out value='${tenantSearch}'/>">
                                    <select name="userId" required>
                                        <option value="">-- Chọn người thuê --</option>
                                        <c:forEach items="${availableTenants}" var="t">
                                            <option value="${t.id}">${t.fullName} — ${t.email} — ${t.phone}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-small">Thêm người thuê</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <p style="margin-top:12px;color:#64748b;font-size:0.9rem;">
                                    <c:choose>
                                        <c:when test="${tenantSearchMode}">
                                            Không tìm thấy sinh viên phù hợp. Thử từ khóa khác hoặc
                                            <a href="${pageContext.request.contextPath}/contract?action=view&amp;id=${contract.id}">xem danh sách đầy đủ</a>.
                                        </c:when>
                                        <c:otherwise>
                                            Không còn sinh viên nào để thêm (đã thêm vào hợp đồng hoặc chưa có tài khoản sinh viên).
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
                <a href="${pageContext.request.contextPath}/contract" class="btn">Quay lại</a>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
