<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉ số điện nước</title>
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
                <h1>Chỉ số điện nước</h1>
                <div class="filter" style="margin-bottom: 16px;">
                    <form method="get" action="${pageContext.request.contextPath}/meterreading">
                        <label>Lọc theo nhà trọ: </label>
                        <select name="boardingHouseId" onchange="this.form.submit()">
                            <option value="">-- Tất cả --</option>
                            <c:forEach items="${boardinghouses}" var="bh">
                                <option value="${bh.id}" ${filterBoardingHouseId == bh.id ? 'selected' : ''}>${bh.name}</option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
                <p><a href="${pageContext.request.contextPath}/meterreading?action=add<c:if test="${filterBoardingHouseId != null}">&boardingHouseId=${filterBoardingHouseId}</c:if>" class="btn btn-primary">Thêm chỉ số</a></p>
                <div class="card">
                    <table>
                        <tr><th>ID</th><th>Phòng</th><th>Tháng/Năm</th><th>Điện đầu</th><th>Điện cuối</th><th>Nước đầu</th><th>Nước cuối</th><th>Thao tác</th></tr>
                        <c:forEach items="${meterreadings}" var="m">
                            <tr>
                                <td>${m.id}</td>
                                <td><c:forEach items="${rooms}" var="r"><c:if test="${r.id == m.roomId}">${r.roomCode}</c:if></c:forEach></td>
                                <td>${m.month}/${m.year}</td>
                                <td>${m.electricStart}</td>
                                <td>${m.electricEnd}</td>
                                <td>${m.waterStart}</td>
                                <td>${m.waterEnd}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/meterreading?action=edit&id=${m.id}" class="btn btn-primary btn-small">Sửa</a>
                                    <form action="${pageContext.request.contextPath}/meterreading" method="post" style="display:inline;" onsubmit="return confirm('Xóa?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${m.id}">
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
