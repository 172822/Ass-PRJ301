<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidebar">
    <c:set var="currentPath" value="${pageContext.request.requestURI}"/>
    <c:choose>
        <c:when test="${sessionScope.user != null}">
            <a href="${pageContext.request.contextPath}/dashboard" class="${currentPath.contains('dashboard') ? 'active' : ''}">Dashboard</a>
            <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LANDLORD' || sessionScope.user.role == 'STUDENT'}">
                <a href="${pageContext.request.contextPath}/findroom" class="${currentPath.contains('findroom') ? 'active' : ''}">Tìm trọ</a>
            </c:if>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/area" class="${currentPath.contains('/area') && !currentPath.contains('subarea') ? 'active' : ''}">Khu vực</a>
                <a href="${pageContext.request.contextPath}/subarea" class="${currentPath.contains('subarea') ? 'active' : ''}">Khu vực con</a>
                <a href="${pageContext.request.contextPath}/user" class="${currentPath.contains('/user') ? 'active' : ''}">Quản lý User</a>
            </c:if>
            <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'LANDLORD'}">
                <a href="${pageContext.request.contextPath}/boardinghouse" class="${currentPath.contains('boardinghouse') ? 'active' : ''}">Nhà trọ</a>
                <a href="${pageContext.request.contextPath}/room" class="${currentPath.contains('/room') ? 'active' : ''}">Phòng</a>
                <a href="${pageContext.request.contextPath}/contract" class="${currentPath.contains('contract') ? 'active' : ''}">Hợp đồng</a>
                <a href="${pageContext.request.contextPath}/meterreading" class="${currentPath.contains('meterreading') ? 'active' : ''}">Chỉ số điện nước</a>
                <a href="${pageContext.request.contextPath}/invoice" class="${currentPath.contains('invoice') ? 'active' : ''}">Hóa đơn</a>
            </c:if>
            <c:if test="${sessionScope.user.role == 'STUDENT'}">
                <a href="${pageContext.request.contextPath}/contract" class="${currentPath.contains('contract') ? 'active' : ''}">Hợp đồng của tôi</a>
                <a href="${pageContext.request.contextPath}/invoice" class="${currentPath.contains('invoice') ? 'active' : ''}">Hóa đơn của tôi</a>
            </c:if>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/findroom" class="${currentPath.contains('findroom') ? 'active' : ''}">Tìm trọ</a>
            <a href="${pageContext.request.contextPath}/login" class="${currentPath.contains('login') ? 'active' : ''}">Đăng nhập</a>
            <a href="${pageContext.request.contextPath}/register" class="${currentPath.contains('register') ? 'active' : ''}">Đăng ký</a>
        </c:otherwise>
    </c:choose>
</div>
