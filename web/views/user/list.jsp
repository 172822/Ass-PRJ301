<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý User</title>
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
                <h1>Quản lý User</h1>
                <p><a href="${pageContext.request.contextPath}/user?action=add" class="btn btn-primary">Thêm User</a></p>
                <div class="card">
                    <table>
                        <tr><th>ID</th><th>Họ tên</th><th>Email</th><th>SĐT</th><th>CCCD</th><th>Vai trò</th><th>Trạng thái</th><th>Thao tác</th></tr>
                        <c:forEach items="${users}" var="u">
                            <tr>
                                <td>${u.id}</td>
                                <td>${u.fullName}</td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <td>${u.cccd}</td>
                                <td>${u.role}</td>
                                <td>${u.isActive ? 'Hoạt động' : 'Khóa'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/user?action=edit&id=${u.id}" class="btn btn-primary btn-small">Sửa</a>
                                    <c:if test="${u.id != sessionScope.user.id}">
                                        <form action="${pageContext.request.contextPath}/user" method="post" style="display:inline;" onsubmit="return confirm('Xóa user này?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${u.id}">
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
