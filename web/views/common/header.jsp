<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="currentPath" value="${pageContext.request.requestURI}"/>
<style>
    .header-actions { display: flex; align-items: center; gap: 20px; flex-wrap: wrap; flex: 1; justify-content: flex-end; min-width: 0; }
    .header-nav { display: flex; align-items: center; gap: 4px; flex-wrap: wrap; }
    .header-nav-link { color: #2563eb; text-decoration: none; font-size: 0.875rem; padding: 6px 10px; border-radius: 4px; white-space: nowrap; border: 1px solid transparent; }
    .header-nav-link:hover { background: #eff6ff; color: #1d4ed8; }
    .header-nav-link.active { background: #dbeafe; color: #1e40af; font-weight: 600; border-color: #93c5fd; }
</style>
<div class="header">
    <h2>Quản lý trọ</h2>
    <div class="header-actions">
        <nav class="header-nav" aria-label="Điều hướng nhanh">
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/dashboard" class="header-nav-link${currentPath.contains('dashboard') ? ' active' : ''}">Dashboard</a>
                    <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LANDLORD' || sessionScope.user.role == 'STUDENT'}">
                        <a href="${pageContext.request.contextPath}/findroom" class="header-nav-link${currentPath.contains('findroom') ? ' active' : ''}">Tìm trọ</a>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/findroom" class="header-nav-link${currentPath.contains('findroom') ? ' active' : ''}">Tìm trọ</a>
                    <a href="${pageContext.request.contextPath}/login" class="header-nav-link${currentPath.contains('login') ? ' active' : ''}">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/register" class="header-nav-link${currentPath.contains('register') ? ' active' : ''}">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </nav>
        <c:if test="${sessionScope.user != null}">
            <div class="user-info">
                <span>${sessionScope.user.fullName} (${sessionScope.user.role})</span>
                <form action="${pageContext.request.contextPath}/logout" method="post" style="margin: 0; display: inline;">
                    <button type="submit" class="logout-btn">Đăng xuất</button>
                </form>
            </div>
        </c:if>
    </div>
</div>
