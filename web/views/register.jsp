<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Quản lý trọ</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
        .box { background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        h1 { margin: 0 0 8px 0; font-size: 1.5rem; color: #333; }
        .sub { color: #64748b; font-size: 0.875rem; margin-bottom: 20px; line-height: 1.4; }
        .form-group { margin-bottom: 14px; }
        label { display: block; margin-bottom: 6px; color: #555; font-size: 0.9rem; }
        input[type="text"], input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }
        .btn { width: 100%; padding: 10px; background: #2563eb; color: #fff; border: none; border-radius: 4px; cursor: pointer; font-size: 1rem; margin-top: 8px; }
        .btn:hover { background: #1d4ed8; }
        .error { color: #dc2626; margin-bottom: 12px; font-size: 0.9rem; }
        .footer-link { text-align: center; margin-top: 20px; font-size: 0.9rem; color: #64748b; }
        .footer-link a { color: #2563eb; }
    </style>
</head>
<body>
    <div class="box">
        <h1>Đăng ký</h1>
        <p class="sub">Tạo tài khoản <strong>Khách thuê</strong> để xem phòng, hợp đồng của bạn. Tài khoản chủ trọ do quản trị viên cấp.</p>
        <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}"/></p>
        </c:if>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="fullName">Họ và tên</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${registerFullName}'/>" required autocomplete="name">
            </div>
            <div class="form-group">
                <label for="email">Email (đăng nhập)</label>
                <input type="text" id="email" name="email" value="<c:out value='${registerEmail}'/>" required autocomplete="email">
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required minlength="6" autocomplete="new-password">
            </div>
            <div class="form-group">
                <label for="passwordConfirm">Xác nhận mật khẩu</label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" required minlength="6" autocomplete="new-password">
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại</label>
                <input type="text" id="phone" name="phone" value="<c:out value='${registerPhone}'/>" autocomplete="tel">
            </div>
            <div class="form-group">
                <label for="cccd">CCCD (tuỳ chọn)</label>
                <input type="text" id="cccd" name="cccd" value="<c:out value='${registerCccd}'/>">
            </div>
            <button type="submit" class="btn">Đăng ký</button>
        </form>
        <p class="footer-link">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
    </div>
</body>
</html>
