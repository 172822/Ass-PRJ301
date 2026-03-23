<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sửa hợp đồng</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 520px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        .hint { color: #64748b; font-size: 0.85rem; margin-top: 4px; }
        input[type="text"], input[type="number"], input[type="date"], select { width: 100%; padding: 8px; }
        .btn { padding: 8px 16px; border-radius: 4px; border: none; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn-primary { background: #2563eb; color: #fff; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Sửa hợp đồng #${contract.id}</h1>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/contract" method="post" id="contractEditForm">
                        <input type="hidden" name="action" value="save">
                        <input type="hidden" name="id" value="${contract.id}">
                        <div class="form-group">
                            <label for="boardingHouseSelect">Nhà trọ</label>
                            <select id="boardingHouseSelect" required>
                                <option value="">— Chọn nhà trọ —</option>
                                <c:forEach items="${boardingHousesForContract}" var="bh">
                                    <option value="${bh.id}" ${preselectedBoardingHouseId != null && preselectedBoardingHouseId == bh.id ? 'selected' : ''}><c:out value="${bh.name}"/></option>
                                </c:forEach>
                            </select>
                            <p class="hint">Chọn nhà trọ trước để lọc danh sách phòng.</p>
                        </div>
                        <div class="form-group">
                            <label for="roomSelect">Phòng</label>
                            <select name="roomId" id="roomSelect" required>
                                <option value="">— Chọn phòng —</option>
                                <c:forEach items="${rooms}" var="r">
                                    <option value="${r.id}"
                                            data-bh="${r.boardingHouseId}"
                                            ${r.id == contract.roomId ? 'selected' : ''}>
                                        <c:out value="${r.roomCode}"/> — <fmt:formatNumber value="${r.price}" type="number" groupingUsed="true"/> VNĐ/tháng
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Ngày bắt đầu</label>
                            <input type="date" name="startDate" value="${contract.startDate}" required>
                        </div>
                        <div class="form-group">
                            <label>Ngày kết thúc</label>
                            <input type="date" name="endDate" value="${contract.endDate}" required>
                        </div>
                        <div class="form-group">
                            <label>Tiền cọc</label>
                            <input type="number" name="deposit" step="1000" value="${contract.deposit}">
                        </div>
                        <div class="form-group">
                            <label>Giá thuê/tháng</label>
                            <input type="number" name="rentPrice" step="1000" value="${contract.rentPrice}" required>
                        </div>
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="status">
                                <option value="active" ${contract.status == 'active' ? 'selected' : ''}>Đang hoạt động</option>
                                <option value="ended" ${contract.status == 'ended' ? 'selected' : ''}>Đã kết thúc</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/contract?action=view&id=${contract.id}" class="btn">Hủy</a>
                    </form>
                    <script>
(function () {
    var bh = document.getElementById('boardingHouseSelect');
    var room = document.getElementById('roomSelect');
    if (!bh || !room) return;
    var roomOptions = Array.prototype.slice.call(room.querySelectorAll('option[value]:not([value=""])'));
    function syncRooms() {
        var bid = bh.value;
        var firstMatch = null;
        roomOptions.forEach(function (opt) {
            var obh = opt.getAttribute('data-bh') || '';
            var show = bid !== '' && obh === String(bid);
            opt.hidden = !show;
            opt.disabled = !show;
            if (show && firstMatch === null) firstMatch = opt;
        });
        var sel = room.selectedOptions[0];
        if (bid !== '' && sel && sel.value !== '' && sel.disabled) {
            room.value = '';
        }
        if (bid !== '' && room.value === '' && firstMatch) {
            room.value = firstMatch.value;
        }
    }
    bh.addEventListener('change', syncRooms);
    syncRooms();
    var bhReal = bh.querySelectorAll('option[value]:not([value=""])');
    if (bh.value === '' && bhReal.length === 1) {
        bh.value = bhReal[0].value;
        syncRooms();
    }
})();
                    </script>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
