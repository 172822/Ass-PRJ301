<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hợp đồng - Quản lý trọ</title>
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
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        .btn { display: inline-block; padding: 8px 16px; border-radius: 4px; text-decoration: none; font-size: 0.875rem; border: none; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .btn-small { padding: 4px 10px; font-size: 0.8rem; }
        .filter { margin-bottom: 16px; }
        .filter select { padding: 8px; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Hợp đồng</h1>
                <div class="filter" style="margin-bottom: 16px;">
                    <form method="get" action="${pageContext.request.contextPath}/contract">
                        <label>Lọc theo nhà trọ: </label>
                        <select name="boardingHouseId" onchange="this.form.submit()">
                            <option value="">-- Tất cả --</option>
                            <c:forEach items="${boardinghouses}" var="bh">
                                <option value="${bh.id}" ${filterBoardingHouseId == bh.id ? 'selected' : ''}>${bh.name}</option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
                <c:if test="${sessionScope.user.role != 'STUDENT'}">
                    <p><a href="${pageContext.request.contextPath}/contract?action=add<c:if test="${filterBoardingHouseId != null}">&boardingHouseId=${filterBoardingHouseId}</c:if>" class="btn btn-primary">Thêm hợp đồng</a></p>
                </c:if>
                <div class="card">
                    <table>
                        <tr><th>ID</th><th>Phòng</th><th>Bắt đầu</th><th>Kết thúc</th><th>Tiền cọc</th><th>Giá thuê</th><th>Trạng thái</th><th>Thao tác</th></tr>
                        <c:forEach items="${contracts}" var="c">
                            <tr>
                                <td>${c.id}</td>
                                <td>
                                    <c:forEach items="${rooms}" var="r"><c:if test="${r.id == c.roomId}">${r.roomCode} <c:forEach items="${boardinghouses}" var="bh"><c:if test="${bh.id == r.boardingHouseId}">(${bh.name})</c:if></c:forEach></c:if></c:forEach>
                                </td>
                                <td>${c.startDate}</td>
                                <td>${c.endDate}</td>
                                <td><fmt:formatNumber value="${c.deposit}" type="number"/></td>
                                <td><fmt:formatNumber value="${c.rentPrice}" type="number"/></td>
                                <td>${c.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/contract?action=view&id=${c.id}" class="btn btn-primary btn-small">Xem</a>
                                    <c:if test="${sessionScope.user.role != 'STUDENT'}">
                                        <a href="${pageContext.request.contextPath}/contract?action=edit&id=${c.id}" class="btn btn-primary btn-small">Sửa</a>
                                        <form action="${pageContext.request.contextPath}/contract" method="post" style="display:inline;" onsubmit="return confirm('Xóa hợp đồng?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.id}">
                                            <button type="submit" class="btn btn-danger btn-small">Xóa</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
