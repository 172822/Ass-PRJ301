<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${meterreading != null ? 'Sửa' : 'Thêm'} chỉ số</title>
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
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 400px; }
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
                <h1>${meterreading != null ? 'Sửa' : 'Thêm'} chỉ số điện nước</h1>
                <div class="card">
                    <form action="${pageContext.request.contextPath}/meterreading" method="post">
                        <c:if test="${meterreading != null}"><input type="hidden" name="id" value="${meterreading.id}"></c:if>
                        <div class="form-group">
                            <label>Nhà trọ</label>
                            <select id="boardingHouseId" name="boardingHouseId" required>
                                <option value="">-- Chọn nhà trọ --</option>
                                <c:forEach items="${boardinghouses}" var="bh">
                                    <option value="${bh.id}">${bh.name}</option>
                                </c:forEach>
                            </select>
                            <p class="hint">Chọn nhà trọ trước để lọc danh sách phòng</p>
                        </div>
                        <div class="form-group">
                            <label>Phòng</label>
                            <select id="roomId" name="roomId" required>
                                <c:choose>
                                    <c:when test="${meterreading != null}">
                                        <option value="${meterreading.roomId}" selected>
                                            <c:forEach items="${rooms}" var="r">
                                                <c:if test="${r.id == meterreading.roomId}">${r.roomCode}</c:if>
                                            </c:forEach>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="">-- Chọn phòng --</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Tháng</label>
                            <input type="number" name="month" min="1" max="12" value="${meterreading != null ? meterreading.month : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Năm</label>
                            <input type="number" name="year" min="2020" value="${meterreading != null ? meterreading.year : ''}" required>
                        </div>
                        <div class="form-group">
                            <label>Điện chỉ số đầu</label>
                            <input type="number" id="electricStart" name="electricStart" min="0" value="${meterreading != null ? meterreading.electricStart : 0}" readonly style="background: #f1f5f9;">
                            <p class="hint">Tự động lấy chỉ số cuối tháng trước</p>
                        </div>
                        <div class="form-group">
                            <label>Điện chỉ số cuối</label>
                            <input type="number" id="electricEnd" name="electricEnd" min="0" value="${meterreading != null ? meterreading.electricEnd : 0}" required>
                        </div>
                        <div class="form-group">
                            <label>Nước chỉ số đầu</label>
                            <input type="number" id="waterStart" name="waterStart" min="0" value="${meterreading != null ? meterreading.waterStart : 0}" readonly style="background: #f1f5f9;">
                            <p class="hint">Tự động lấy chỉ số cuối tháng trước</p>
                        </div>
                        <div class="form-group">
                            <label>Nước chỉ số cuối</label>
                            <input type="number" id="waterEnd" name="waterEnd" min="0" value="${meterreading != null ? meterreading.waterEnd : 0}" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                        <a href="${pageContext.request.contextPath}/meterreading" class="btn">Hủy</a>
                    </form>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>

    <script>
        // Map of room data: roomId -> { roomCode, boardingHouseId, previousElectricEnd, previousWaterEnd }
        const roomsData = {
            <c:forEach items="${rooms}" var="r" varStatus="status">
            ${r.id}: { roomCode: '${r.roomCode}', bhId: ${r.boardingHouseId} }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };

        document.getElementById('boardingHouseId').addEventListener('change', function() {
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

        // Auto-populate starting numbers when room is selected
        document.getElementById('roomId').addEventListener('change', function() {
            const roomId = parseInt(this.value);
            const monthInput = document.querySelector('input[name="month"]');
            const yearInput = document.querySelector('input[name="year"]');
            
            if (roomId && monthInput.value && yearInput.value) {
                fetchPreviousMeterReading(roomId, parseInt(monthInput.value), parseInt(yearInput.value));
            }
        });

        document.querySelector('input[name="month"]').addEventListener('change', function() {
            const roomId = parseInt(document.getElementById('roomId').value);
            const yearInput = document.querySelector('input[name="year"]');
            if (roomId && this.value && yearInput.value) {
                fetchPreviousMeterReading(roomId, parseInt(this.value), parseInt(yearInput.value));
            }
        });

        document.querySelector('input[name="year"]').addEventListener('change', function() {
            const roomId = parseInt(document.getElementById('roomId').value);
            const monthInput = document.querySelector('input[name="month"]');
            if (roomId && monthInput.value && this.value) {
                fetchPreviousMeterReading(roomId, parseInt(monthInput.value), parseInt(this.value));
            }
        });

        function fetchPreviousMeterReading(roomId, month, year) {
            const ctx = '${pageContext.request.contextPath}';
            const previousMonth = month > 1 ? month - 1 : 12;
            const previousYear = month > 1 ? year : year - 1;
            
            fetch(ctx + '/meterreading?action=getPrevious&roomId=' + roomId + '&month=' + previousMonth + '&year=' + previousYear)
                .then(response => response.json())
                .then(data => {
                    if (data.found) {
                        document.getElementById('electricStart').value = data.electricEnd || 0;
                        document.getElementById('waterStart').value = data.waterEnd || 0;
                    } else {
                        document.getElementById('electricStart').value = 0;
                        document.getElementById('waterStart').value = 0;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    document.getElementById('electricStart').value = 0;
                    document.getElementById('waterStart').value = 0;
                });
        }

        // Set initial boarding house if editing
        <c:if test="${meterreading != null}">
            window.addEventListener('load', function() {
                const roomId = parseInt(document.getElementById('roomId').value);
                if (roomId && roomsData[roomId]) {
                    document.getElementById('boardingHouseId').value = roomsData[roomId].bhId;
                    document.getElementById('boardingHouseId').dispatchEvent(new Event('change'));
                }
            });
        </c:if>
    </script>
</body>
</html>
