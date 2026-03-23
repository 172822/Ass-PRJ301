<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tìm trọ - Quản lý trọ</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 16px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        .btn { display: inline-block; padding: 8px 16px; border-radius: 4px; text-decoration: none; font-size: 0.875rem; border: none; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-small { padding: 4px 10px; font-size: 0.8rem; }
        .filter-row { display: flex; flex-wrap: wrap; gap: 12px; align-items: flex-end; margin-bottom: 12px; }
        .filter-row label { display: block; font-size: 0.875rem; margin-bottom: 4px; color: #475569; }
        .filter-row select, .filter-row input[type="text"] { padding: 8px; min-width: 180px; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="common/header.jsp"/>
            <div class="content">
                <h1>Tìm trọ</h1>
                <p>Lọc theo khu vực và tên nhà trọ (tìm gần đúng). Bạn có thể xem danh sách nhà trọ mà không cần đăng nhập.
                    <c:if test="${sessionScope.user == null}"> Đăng nhập để mở danh sách phòng của từng nhà trọ.</c:if>
                </p>
                <div class="card">
                    <form method="get" action="${pageContext.request.contextPath}/findroom">
                        <div class="filter-row">
                            <div>
                                <label for="areaId">Khu vực lớn</label>
                                <select name="areaId" id="areaId">
                                    <option value="">— Tất cả —</option>
                                    <c:forEach items="${areas}" var="a">
                                        <option value="${a.id}" ${selectedAreaId != null && selectedAreaId == a.id ? 'selected' : ''}>${a.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label for="subAreaId">Khu vực con</label>
                                <select name="subAreaId" id="subAreaId">
                                    <option value="">— Tất cả —</option>
                                    <c:forEach items="${subAreas}" var="s">
                                        <option value="${s.id}" ${selectedSubAreaId != null && selectedSubAreaId == s.id ? 'selected' : ''}>${s.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label for="q">Tên nhà trọ</label>
                                <input type="text" name="q" id="q" value="${q}" placeholder="Ví dụ: minh, an bình…">
                            </div>
                            <div>
                                <button type="submit" class="btn btn-primary">Tìm</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="card">
                    <table>
                        <tr>
                            <th>Tên nhà trọ</th>
                            <th>Địa chỉ</th>
                            <th>Khu vực con</th>
                            <th>Khu vực lớn</th>
                            <th>Liên hệ chủ trọ</th>
                            <th>Xem phòng</th>
                        </tr>
                        <c:choose>
                            <c:when test="${empty results}">
                                <tr><td colspan="6">Không có nhà trọ phù hợp.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${results}" var="row">
                                    <tr>
                                        <td>${row.name}</td>
                                        <td>${row.address}</td>
                                        <td>${row.subAreaName}</td>
                                        <td>${row.areaName}</td>
                                        <td class="contact-cell">
                                            <c:choose>
                                                <c:when test="${not empty row.landlordFullName || not empty row.landlordPhone || not empty row.landlordEmail}">
                                                    <c:if test="${not empty row.landlordFullName}"><strong><c:out value="${row.landlordFullName}"/></strong><br></c:if>
                                                    <c:if test="${not empty row.landlordPhone}"><span style="color:#64748b;font-size:0.85rem;">ĐT:</span> <c:out value="${row.landlordPhone}"/><br></c:if>
                                                    <c:if test="${not empty row.landlordEmail}"><span style="color:#64748b;font-size:0.85rem;">Email:</span> <c:out value="${row.landlordEmail}"/></c:if>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${sessionScope.user != null}">
                                                    <a href="${pageContext.request.contextPath}/room?boardingHouseId=${row.id}" class="btn btn-primary btn-small">Phòng</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:url var="loginForRoomUrl" value="/login">
                                                        <c:param name="next" value="/room?boardingHouseId=${row.id}"/>
                                                    </c:url>
                                                    <a href="${loginForRoomUrl}" class="btn btn-primary btn-small">Xem phòng</a>
                                                    <span style="display:block;font-size:0.75rem;color:#64748b;margin-top:4px;">để xem danh sách phòng</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
            </div>
            <jsp:include page="common/footer.jsp"/>
        </div>
    </div>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        const areaSelect = document.getElementById('areaId');
        const subAreaSelect = document.getElementById('subAreaId');
        
        // Handle area change
        areaSelect.addEventListener('change', function() {
            const areaId = this.value;
            
            // Clear sub-area dropdown
            subAreaSelect.innerHTML = '<option value="">— Tất cả —</option>';
            
            if (!areaId) {
                // No area selected, clear everything
                document.querySelector('form').submit();
                return;
            }
            
            // Fetch sub-areas via AJAX
            fetch('${pageContext.request.contextPath}/findroom?action=getSubAreas&areaId=' + areaId)
                .then(response => response.json())
                .then(subAreas => {
                    // Populate sub-area dropdown
                    subAreas.forEach(subArea => {
                        const option = document.createElement('option');
                        option.value = subArea.id;
                        option.textContent = subArea.name;
                        subAreaSelect.appendChild(option);
                    });
                    
                    // Auto-submit the form to show results
                    document.querySelector('form').submit();
                })
                .catch(error => console.error('Error fetching sub-areas:', error));
        });
    });
    </script>
</body>
</html>
