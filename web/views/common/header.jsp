<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <h2>Quản lý trọ</h2>
    <div class="user-info">
        <span>${sessionScope.user.fullName} (${sessionScope.user.role})</span>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </div>
</div>
