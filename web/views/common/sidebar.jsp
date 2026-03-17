<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
    <c:if test="${sessionScope.user.role == 'admin'}">
        <a href="${pageContext.request.contextPath}/area">Khu vực</a>
        <a href="${pageContext.request.contextPath}/subarea">Khu vực con</a>
        <a href="${pageContext.request.contextPath}/user">Quản lý User</a>
    </c:if>
    <c:if test="${sessionScope.user.role == 'admin' || sessionScope.user.role == 'landlord'}">
        <a href="${pageContext.request.contextPath}/boardinghouse">Nhà trọ</a>
        <a href="${pageContext.request.contextPath}/room">Phòng</a>
        <a href="${pageContext.request.contextPath}/contract">Hợp đồng</a>
        <a href="${pageContext.request.contextPath}/meterreading">Chỉ số điện nước</a>
        <a href="${pageContext.request.contextPath}/invoice">Hóa đơn</a>
    </c:if>
    <c:if test="${sessionScope.user.role == 'tenant'}">
        <a href="${pageContext.request.contextPath}/contract">Hợp đồng của tôi</a>
        <a href="${pageContext.request.contextPath}/invoice">Hóa đơn của tôi</a>
    </c:if>
</div>
