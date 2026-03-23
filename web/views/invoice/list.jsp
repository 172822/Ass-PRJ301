<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        .btn { display: inline-block; padding: 8px 16px; border-radius: 4px; text-decoration: none; font-size: 0.875rem; border: none; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .btn-small { padding: 4px 10px; font-size: 0.8rem; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Hóa đơn</h1>
                <c:if test="${sessionScope.user.role != 'STUDENT'}">
                    <p><a href="${pageContext.request.contextPath}/invoice?action=add" class="btn btn-primary">Thêm hóa đơn</a></p>
                </c:if>
                <div class="card">
                    <table>
                        <tr><th>ID</th><th>Phòng</th><th>Tháng/Năm</th><th>Tiền phòng</th><th>Điện</th><th>Nước</th><th>Tổng</th><th>Trạng thái</th><th>Thao tác</th></tr>
                        <c:forEach items="${invoices}" var="i">
                            <tr>
                                <td>${i.id}</td>
                                <td><c:forEach items="${rooms}" var="r"><c:if test="${r.id == i.roomId}">${r.roomCode}</c:if></c:forEach></td>
                                <td>${i.month}/${i.year}</td>
                                <td><fmt:formatNumber value="${i.roomPrice}" type="number"/></td>
                                <td><fmt:formatNumber value="${i.electricPrice}" type="number"/></td>
                                <td><fmt:formatNumber value="${i.waterPrice}" type="number"/></td>
                                <td><fmt:formatNumber value="${i.totalPrice}" type="number"/></td>
                                <td>${i.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/invoice?action=view&id=${i.id}" class="btn btn-primary btn-small">Xem</a>
                                    <c:if test="${sessionScope.user.role != 'STUDENT'}">
                                        <a href="${pageContext.request.contextPath}/invoice?action=edit&id=${i.id}" class="btn btn-primary btn-small">Sửa</a>
                                        <form action="${pageContext.request.contextPath}/invoice" method="post" style="display:inline;" onsubmit="return confirm('Xóa?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${i.id}">
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
