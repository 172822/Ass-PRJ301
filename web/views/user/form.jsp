<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${editUser != null ? 'Sửa' : 'Thêm'} User</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; border-radius: 4px; transition: all 0.2s; }
        .sidebar a:hover { color: #fff; }
        .sidebar a.active { background: #2563eb; color: #fff; padding-left: 8px; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; margin-top: 60px; }
        .header { position: fixed; top: 0; right: 0; left: 200px; background: #fff; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); height: 60px; z-index: 99; display: flex; justify-content: space-between; align-items: center; }
        .header h2 { margin: 0; font-size: 1.25rem; }
        .user-info { display: flex; align-items: center; gap: 12px; }
        .user-info span { color: #334155; }
        .logout-btn { background: #dc2626; color: #fff; border: none; padding: 8px 16px; border-radius: 4px; font-size: 0.875rem; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; }
        .logout-btn:hover { background: #b91c1c; }
        .user-info a { margin-left: 12px; color: #2563eb; text-decoration: none; }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 480px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], input[type="password"], select { width: 100%; padding: 8px; }
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
                <h1>${editUser != null ? 'Sửa' : 'Thêm'} User</h1>
                <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/user" method="post">
                        <c:if test="${editUser != null}"><input type="hidden" name="id" value="${editUser.id}"></c:if>
                        <div class="form-group">
                            <label>Họ tên</label>
                            <input type="text" name="fullName" value="${editUser != null ? editUser.fullName : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="text" name="email" value="${editUser != null ? editUser.email : ''}" required ${editUser != null ? 'readonly' : ''}>
                        </div>
                        <div class="form-group">
                            <label>Mật khẩu ${editUser != null ? '(để trống nếu không đổi)' : ''}</label>
                            <input type="password" name="password" ${editUser == null ? 'required' : ''}>
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="text" name="phone" value="${editUser != null ? editUser.phone : ''}">
                        </div>
                        <div class="form-group">
                            <label>CCCD</label>
                            <input type="text" name="cccd" value="${editUser != null ? editUser.cccd : ''}">
                        </div>
                        <div class="form-group">
                            <label>Vai trò</label>
                            <select name="role">
                                <option value="ADMIN" ${editUser != null && editUser.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                <option value="LANDLORD" ${editUser != null && editUser.role == 'LANDLORD' ? 'selected' : ''}>Chủ trọ</option>
                                <option value="STUDENT" ${editUser != null && editUser.role == 'STUDENT' ? 'selected' : ''}>Khách thuê</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label><input type="checkbox" name="isActive" value="1" ${editUser == null || editUser.isActive ? 'checked' : ''}> Hoạt động</label>
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/user" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
