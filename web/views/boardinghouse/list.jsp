<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nhà trọ - Quản lý trọ</title>
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
                <h1>Quản lý nhà trọ</h1>
                <p><a href="${pageContext.request.contextPath}/boardinghouse?action=add" class="btn btn-primary">Thêm nhà trọ</a></p>
                <div class="card">
                    <table>
                        <tr><th>ID</th><th>Tên</th><th>Địa chỉ</th><th>Khu vực con</th><c:if test="${sessionScope.user.role == 'admin'}"><th>Chủ trọ</th></c:if><th>Thao tác</th></tr>
                        <c:forEach items="${boardinghouses}" var="b">
                            <tr>
                                <td>${b.id}</td>
                                <td>${b.name}</td>
                                <td>${b.address}</td>
                                <td><c:forEach items="${subareas}" var="s"><c:if test="${s.id == b.subAreaId}">${s.name}</c:if></c:forEach></td>
                                <c:if test="${sessionScope.user.role == 'admin'}">
                                    <td>${b.landlordId}</td>
                                </c:if>
                                <td>
                                    <a href="${pageContext.request.contextPath}/boardinghouse?action=edit&id=${b.id}" class="btn btn-primary btn-small">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/room?boardingHouseId=${b.id}" class="btn btn-primary btn-small">Phòng</a>
                                    <form action="${pageContext.request.contextPath}/boardinghouse" method="post" style="display:inline;" onsubmit="return confirm('Xóa?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${b.id}">
                                        <button type="submit" class="btn btn-danger btn-small">Xóa</button>
                                    </form>
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
