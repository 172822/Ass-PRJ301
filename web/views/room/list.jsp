<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Phòng - Quản lý trọ</title>
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
        .hint { color: #64748b; font-size: 0.9rem; margin-bottom: 12px; }
        .contact-card { margin-bottom: 16px; }
        .contact-card dl { margin: 0; }
        .contact-card dt { color: #64748b; font-size: 0.8rem; margin-top: 8px; }
        .contact-card dt:first-child { margin-top: 0; }
        .contact-card dd { margin: 2px 0 0 0; font-weight: 500; }
        .bh-photo-block { margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #e2e8f0; }
        .bh-photo-block .bh-photo { max-width: 100%; max-height: 280px; width: auto; height: auto; object-fit: contain; border-radius: 8px; border: 1px solid #e2e8f0; background: #f8fafc; display: block; }
        .bh-photo-block .open-full { display: inline-block; margin-top: 10px; font-size: 0.9rem; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1><c:choose><c:when test="${roomListReadOnly}">Danh sách phòng (xem)</c:when><c:otherwise>Quản lý phòng</c:otherwise></c:choose></h1>
                <c:if test="${roomListReadOnly}">
                    <p class="hint">Chỉ xem thông tin phòng. Liên hệ chủ trọ để hỏi thuê hoặc xem phòng.</p>
                    <c:if test="${landlordContact != null}">
                        <div class="card contact-card">
                            <h2 style="margin-top:0;font-size:1.1rem;">Liên hệ chủ trọ</h2>
                            <dl>
                                <dt>Họ tên</dt>
                                <dd><c:choose><c:when test="${not empty landlordContact.fullName}"><c:out value="${landlordContact.fullName}"/></c:when><c:otherwise>—</c:otherwise></c:choose></dd>
                                <dt>Điện thoại</dt>
                                <dd><c:choose><c:when test="${not empty landlordContact.phone}"><c:out value="${landlordContact.phone}"/></c:when><c:otherwise>—</c:otherwise></c:choose></dd>
                                <dt>Email</dt>
                                <dd><c:choose><c:when test="${not empty landlordContact.email}"><c:out value="${landlordContact.email}"/></c:when><c:otherwise>—</c:otherwise></c:choose></dd>
                            </dl>
                        </div>
                    </c:if>
                    <p><a href="${pageContext.request.contextPath}/findroom" class="btn btn-primary btn-small">← Quay lại Tìm trọ</a></p>
                </c:if>
                <c:if test="${!roomListReadOnly}">
                    <div class="filter">
                        <form method="get" action="${pageContext.request.contextPath}/room">
                            <label>Lọc theo nhà trọ: </label>
                            <select name="boardingHouseId" onchange="this.form.submit()">
                                <option value="">-- Tất cả --</option>
                                <c:forEach items="${boardinghouses}" var="bh">
                                    <option value="${bh.id}" ${filterBoardingHouseId == bh.id ? 'selected' : ''}>${bh.name}</option>
                                </c:forEach>
                            </select>
                        </form>
                    </div>
                    <p><a href="${pageContext.request.contextPath}/room?action=add<c:if test="${filterBoardingHouseId != null}">&boardingHouseId=${filterBoardingHouseId}</c:if>" class="btn btn-primary">Thêm phòng</a></p>
                </c:if>
                <div class="card">
                    <c:if test="${filterBoardingHouseId != null}">
                        <c:forEach items="${boardinghouses}" var="bh">
                            <c:if test="${bh.id == filterBoardingHouseId}">
                                <c:choose>
                                    <c:when test="${not empty bh.imagePath}">
                                        <div class="bh-photo-block">
                                            <p style="margin:0 0 10px 0;color:#334155;"><strong>Ảnh nhà trọ</strong> — <c:out value="${bh.name}"/></p>
                                            <a href="${pageContext.request.contextPath}/${bh.imagePath}" target="_blank" rel="noopener noreferrer" title="Xem ảnh đầy đủ">
                                                <img class="bh-photo" src="${pageContext.request.contextPath}/${bh.imagePath}" alt="Ảnh nhà trọ">
                                            </a>
                                            <a class="open-full" href="${pageContext.request.contextPath}/${bh.imagePath}" target="_blank" rel="noopener noreferrer">Mở ảnh cỡ lớn</a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="hint bh-photo-block" style="border-bottom:1px solid #e2e8f0;">Nhà trọ <strong><c:out value="${bh.name}"/></strong> chưa có ảnh.<c:if test="${!roomListReadOnly}"> Thêm ảnh tại <a href="${pageContext.request.contextPath}/boardinghouse?action=edit&amp;id=${bh.id}">Sửa nhà trọ</a>.</c:if></p>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    <c:if test="${filterBoardingHouseId == null && !roomListReadOnly}">
                        <p class="hint" style="margin-top:0;">Chọn nhà trọ ở bộ lọc phía trên để xem ảnh nhà trọ (một ảnh cho cả danh sách phòng).</p>
                    </c:if>
                    <table>
                        <tr>
                            <th>ID</th><th>Nhà trọ</th><th>Mã phòng</th><th>Giá</th><th>Số người tối đa</th><th>Trạng thái</th>
                            <c:if test="${!roomListReadOnly}"><th>Thao tác</th></c:if>
                        </tr>
                        <c:choose>
                            <c:when test="${empty rooms}">
                                <tr><td colspan="${roomListReadOnly ? 6 : 7}">Không có phòng nào được đăng.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${rooms}" var="r">
                                    <tr>
                                        <td>${r.id}</td>
                                        <td><c:forEach items="${boardinghouses}" var="bh"><c:if test="${bh.id == r.boardingHouseId}">${bh.name}</c:if></c:forEach></td>
                                        <td>${r.roomCode}</td>
                                        <td>${r.price}</td>
                                        <td>${r.maxPerson}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${r.status == 'EMPTY'}">Trống</c:when>
                                                <c:when test="${r.status == 'RENTED'}">Đã thuê</c:when>
                                                <c:otherwise><c:out value="${r.status}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <c:if test="${!roomListReadOnly}">
                                            <td>
                                                <c:set var="viewCid" value="${roomContractViewId[r.id]}"/>
                                                <c:choose>
                                                    <c:when test="${viewCid != null}">
                                                        <a href="${pageContext.request.contextPath}/contract?action=view&amp;id=${viewCid}" class="btn btn-small" style="background:#0d9488;color:#fff;" title="Xem hợp đồng, thêm người thuê hoặc chỉnh sửa">Xem hợp đồng</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/contract?action=add&amp;roomId=${r.id}" class="btn btn-small" style="background:#0d9488;color:#fff;" title="Tạo hợp đồng mới cho phòng trống">Tạo hợp đồng</a>
                                                    </c:otherwise>
                                                </c:choose>
                                                <a href="${pageContext.request.contextPath}/room?action=edit&id=${r.id}" class="btn btn-primary btn-small">Sửa</a>
                                                <form action="${pageContext.request.contextPath}/room" method="post" style="display:inline;" onsubmit="return confirm('Xóa?');">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${r.id}">
                                                    <button type="submit" class="btn btn-danger btn-small">Xóa</button>
                                                </form>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
