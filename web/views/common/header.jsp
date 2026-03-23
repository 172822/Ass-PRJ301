<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <h2>Quản lý trọ</h2>
    <div class="user-info">
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <span>${sessionScope.user.fullName} (${sessionScope.user.role})</span>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                <span style="margin:0 8px;color:#94a3b8;">|</span>
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
