<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${invoice != null ? 'Sửa' : 'Thêm'} hóa đơn</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 480px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        input[type="text"], input[type="number"], select { width: 100%; padding: 8px; }
        .btn { padding: 8px 16px; border-radius: 4px; border: none; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn-primary { background: #2563eb; color: #fff; }
        .hint { color: #64748b; font-size: 0.85rem; margin-top: 4px; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>${invoice != null ? 'Sửa' : 'Thêm'} hóa đơn</h1>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/invoice" method="post">
                        <input type="hidden" name="action" value="save">
                        <c:if test="${invoice != null}"><input type="hidden" name="id" value="${invoice.id}"></c:if>
                        <div class="form-group">
                            <label>Nhà trọ</label>
                            <c:choose>
                                <c:when test="${invoice != null}">
                                    <c:set var="invoiceRoom" value="${null}"/>
                                    <c:forEach items="${rooms}" var="r">
                                        <c:if test="${r.id == invoice.roomId}">
                                            <c:set var="invoiceRoom" value="${r}"/>
                                        </c:if>
                                    </c:forEach>
                                    <input type="hidden" name="boardingHouseId" value="${invoiceRoom.boardingHouseId}">
                                    <input type="text" value="<c:forEach items='${boardinghouses}' var='bh'><c:if test='${bh.id == invoiceRoom.boardingHouseId}'>${bh.name}</c:if></c:forEach>" readonly style="background: #f1f5f9;">
                                </c:when>
                                <c:otherwise>
                                    <select id="boardingHouseId" name="boardingHouseId">
                                        <option value="">-- Chọn nhà trọ --</option>
                                        <c:forEach items="${boardinghouses}" var="bh">
                                            <option value="${bh.id}">${bh.name}</option>
                                        </c:forEach>
                                    </select>
                                    <p class="hint">Chọn nhà trọ trước để lọc danh sách phòng</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label>Phòng</label>
                            <c:choose>
                                <c:when test="${invoice != null}">
                                    <input type="hidden" name="roomId" value="${invoice.roomId}">
                                    <input type="text" value="<c:forEach items='${rooms}' var='r'><c:if test='${r.id == invoice.roomId}'>${r.roomCode}</c:if></c:forEach>" readonly style="background: #f1f5f9;">
                                </c:when>
                                <c:otherwise>
                                    <select id="roomId" name="roomId" required>
                                        <option value="">-- Chọn phòng --</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label>Tháng</label>
                            <input type="number" name="month" min="1" max="12" value="${invoice != null ? invoice.month : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Năm</label>
                            <input type="number" name="year" min="2020" value="${invoice != null ? invoice.year : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Tiền phòng</label>
                            <input type="number" name="roomPrice" step="1000" value="${invoice != null ? invoice.roomPrice : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Tiền điện</label>
                            <input type="number" name="electricPrice" step="1000" value="${invoice != null ? invoice.electricPrice : 0}">
                        </div>
                        <div class="form-group">
                            <label>Tiền nước</label>
                            <input type="number" name="waterPrice" step="1000" value="${invoice != null ? invoice.waterPrice : 0}">
                        </div>
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <c:choose>
                                <c:when test="${invoice != null}">
                                    <select name="status">
                                        <option value="unpaid" ${invoice.status == 'unpaid' ? 'selected' : ''}>Chưa thanh toán</option>
                                        <option value="paid" ${invoice.status == 'paid' ? 'selected' : ''}>Đã thanh toán</option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="status" value="unpaid">
                                    <input type="text" value="Chưa thanh toán" readonly style="background: #f1f5f9;">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/invoice" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>

    <script>
        // Map of room data: roomId -> { roomCode, boardingHouseId }
        const roomsData = {
            <c:forEach items="${rooms}" var="r" varStatus="status">
            ${r.id}: { roomCode: '${r.roomCode}', bhId: ${r.boardingHouseId} }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };

        // Only add event listener if boardingHouseId select exists (not in edit mode)
        const bhSelect = document.getElementById('boardingHouseId');
        if (bhSelect && bhSelect.tagName === 'SELECT') {
            bhSelect.addEventListener('change', function() {
                const selectedBhId = parseInt(this.value);
                const roomSelect = document.getElementById('roomId');
                const currentRoomId = document.querySelector('input[name="roomId"]')?.value;
                
                roomSelect.innerHTML = '<option value="">-- Chọn phòng --</option>';
                
                if (selectedBhId) {
                    Object.keys(roomsData).forEach(roomId => {
                        if (roomsData[roomId].bhId === selectedBhId) {
                            const option = document.createElement('option');
                            option.value = roomId;
                            option.textContent = roomsData[roomId].roomCode;
                            if (currentRoomId && parseInt(currentRoomId) === parseInt(roomId)) {
                                option.selected = true;
                            }
                            roomSelect.appendChild(option);
                        }
                    });
                }
            });
        }
    </script>
</body>
</html>
