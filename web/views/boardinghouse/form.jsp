<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${boardinghouse != null ? 'Sửa' : 'Thêm'} nhà trọ</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 480px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], select { width: 100%; padding: 8px; }
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
                <h1>${boardinghouse != null ? 'Sửa' : 'Thêm'} nhà trọ</h1>
                <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/boardinghouse" method="post">
                        <c:if test="${boardinghouse != null}"><input type="hidden" name="id" value="${boardinghouse.id}"></c:if>
                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
                            <div class="form-group">
                                <label>Chủ trọ</label>
                                <select name="landlordId">
                                    <c:forEach items="${users}" var="u">
                                        <c:if test="${u.role == 'LANDLORD' || u.role == 'ADMIN'}">
                                            <option value="${u.id}" ${boardinghouse != null && boardinghouse.landlordId == u.id ? 'selected' : ''}>${u.fullName} (${u.email})</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <label>Khu vực con</label>
                            <select name="subAreaId" required>
                                <c:forEach items="${subareas}" var="s">
                                    <option value="${s.id}" ${boardinghouse != null && boardinghouse.subAreaId == s.id ? 'selected' : ''}>${s.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Tên nhà trọ</label>
                            <input type="text" name="name" value="${boardinghouse != null ? boardinghouse.name : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Địa chỉ</label>
                            <input type="text" name="address" value="${boardinghouse != null ? boardinghouse.address : ''}">
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/boardinghouse" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
