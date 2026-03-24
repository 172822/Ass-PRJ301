<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm hợp đồng</title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; border-radius: 4px; transition: all 0.2s; }
        .sidebar a:hover { color: #fff; }
        .sidebar a.active { background: #2563eb; color: #fff; padding-left: 8px; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; margin-top: 60px; }
        .header { position: fixed; top: 0; right: 0; left: 200px; background: #fff; padding: 16px 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); min-height: 60px; z-index: 99; display: flex; justify-content: space-between; align-items: center; gap: 16px; }
        .header h2 { margin: 0; font-size: 1.25rem; flex-shrink: 0; }
        .user-info { display: flex; align-items: center; gap: 12px; }
        .user-info span { color: #334155; }
        .logout-btn { background: #dc2626; color: #fff; border: none; padding: 8px 16px; border-radius: 4px; font-size: 0.875rem; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; }
        .logout-btn:hover { background: #b91c1c; }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; }
        .card { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); max-width: 720px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; }
        .hint { color: #64748b; font-size: 0.85rem; margin-top: 4px; }
        input[type="text"], input[type="number"], input[type="date"] { width: 100%; padding: 8px; }
        select.sel-full { width: 100%; padding: 8px; }
        input[readonly] { background: #f1f5f9; color: #334155; cursor: not-allowed; }
        .btn { display: inline-block; padding: 8px 16px; border-radius: 4px; text-decoration: none; font-size: 0.875rem; border: none; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .btn-small { padding: 4px 10px; font-size: 0.8rem; }
        .error-alert { background: #fef2f2; color: #b91c1c; padding: 12px 14px; border-radius: 6px; margin-bottom: 16px; border: 1px solid #fecaca; }
        .form-inline { display: inline-flex; gap: 8px; align-items: center; margin-top: 12px; flex-wrap: wrap; }
        .form-inline select { padding: 6px; }
        table.pick-table { width: 100%; border-collapse: collapse; margin-bottom: 12px; }
        table.pick-table th, table.pick-table td { padding: 10px; text-align: left; border-bottom: 1px solid #e2e8f0; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="../common/sidebar.jsp"/>
        <div class="main">
            <jsp:include page="../common/header.jsp"/>
            <div class="content">
                <h1>Thêm hợp đồng</h1>
                <p style="color:#64748b;font-size:0.9rem;margin-bottom:16px;max-width:720px;">
                    Chọn nhà trọ và phòng, điền thời hạn. Phần người thuê giống màn chi tiết hợp đồng: tìm gần đúng rồi thêm từng người.
                </p>
                <div class="card">
                    <c:choose>
                        <c:when test="${empty rooms}">
                            <p style="color:#64748b;">Bạn chưa có phòng nào để lập hợp đồng. Hãy thêm phòng trong mục Phòng.</p>
                            <a href="${pageContext.request.contextPath}/room" class="btn btn-primary">Đến quản lý phòng</a>
                            <a href="${pageContext.request.contextPath}/contract" class="btn">Quay lại</a>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty error}">
                                <div class="error-alert"><c:out value="${error}"/></div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/contract" method="post" id="contractAddForm">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="tenantSearch" value="<c:out value='${tenantSearch}'/>">
                                <div class="form-group">
                                    <label for="boardingHouseSelect">Nhà trọ</label>
                                    <select id="boardingHouseSelect" class="sel-full" required>
                                        <option value="">— Chọn nhà trọ —</option>
                                        <c:forEach items="${boardingHousesForContract}" var="bh">
                                            <option value="${bh.id}" ${preselectedBoardingHouseId != null && preselectedBoardingHouseId == bh.id ? 'selected' : ''}><c:out value="${bh.name}"/></option>
                                        </c:forEach>
                                    </select>
                                    <p class="hint">Chọn nhà trọ trước để lọc danh sách phòng.</p>
                                </div>
                                <div class="form-group">
                                    <label for="roomSelect">Phòng</label>
                                    <select name="roomId" id="roomSelect" class="sel-full" required>
                                        <option value="">— Chọn phòng —</option>
                                        <c:forEach items="${rooms}" var="r">
                                            <option value="${r.id}"
                                                    data-bh="${r.boardingHouseId}"
                                                    data-price="${r.price}"
                                                    data-max-person="${r.maxPerson}"
                                                    ${preselectedRoomId != null && preselectedRoomId == r.id ? 'selected' : ''}>
                                                <c:out value="${r.roomCode}"/> — <c:out value="${r.price}"/> VNĐ/tháng
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <p class="hint" id="maxPersonHint"></p>
                                </div>
                                <div class="form-group">
                                    <label>Ngày bắt đầu</label>
                                    <input type="date" name="startDate" required value="<c:out value='${prefillStartDate}'/>">
                                </div>
                                <div class="form-group">
                                    <label>Ngày kết thúc</label>
                                    <input type="date" name="endDate" required value="<c:out value='${prefillEndDate}'/>">
                                </div>
                                <div class="form-group">
                                    <label>Tiền cọc</label>
                                    <input type="number" name="deposit" step="1000" value="<c:choose><c:when test="${empty prefillDeposit}">0</c:when><c:otherwise><c:out value="${prefillDeposit}"/></c:otherwise></c:choose>">
                                </div>
                                <div class="form-group">
                                    <label>Giá thuê/tháng</label>
                                    <input type="number" name="rentPrice" id="rentPriceInput" step="1000" required readonly
                                           aria-readonly="true" title="Lấy theo giá phòng đã chọn"
                                           value="<c:out value='${prefillRentPrice}'/>">
                                    <p class="hint">Tự lấy theo phòng đã chọn, không chỉnh tay.</p>
                                </div>
                                <div class="form-group">
                                    <label>Trạng thái</label>
                                    <select name="status" class="sel-full">
                                        <option value="active" ${prefillStatus == 'ended' ? '' : 'selected'}>Đang hoạt động</option>
                                        <option value="ended" ${prefillStatus == 'ended' ? 'selected' : ''}>Đã kết thúc</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <h3 style="margin:0 0 8px 0; font-size:1.1rem;">Người thuê</h3>
                                    <p class="hint" id="pickedCountHint" style="margin-top:0;"></p>
                                    <p style="margin:0 0 8px 0;"><strong>Tìm người thuê</strong> (email hoặc SĐT, gần đúng)</p>
                                    <div class="form-inline" style="margin-top:0;">
                                        <input type="text" id="tenantSearchInput" value="<c:out value='${tenantSearch}'/>"
                                               placeholder="Ví dụ: 09, gmail..." style="padding:6px; min-width:240px;">
                                        <button type="button" id="btnTenantSearch" class="btn btn-primary btn-small">Tìm</button>
                                        <c:if test="${not empty tenantSearch}">
                                            <button type="button" id="btnTenantSearchAll" class="btn btn-small" style="background:#e2e8f0;color:#334155;">Xem tất cả</button>
                                        </c:if>
                                    </div>
                                    <c:if test="${not empty tenantSearchHint}">
                                        <p style="color:#b45309;font-size:0.9rem;margin:8px 0 12px 0;"><c:out value="${tenantSearchHint}"/></p>
                                    </c:if>
                                    <table class="pick-table">
                                        <thead>
                                        <tr><th>Họ tên</th><th>Email</th><th>SĐT</th><th>Thao tác</th></tr>
                                        </thead>
                                        <tbody id="pickedTenantsBody">
                                        <c:forEach items="${selectedTenantUsers}" var="u">
                                            <tr data-user-id="${u.id}">
                                                <td><c:out value="${u.fullName}"/></td>
                                                <td><c:out value="${u.email}"/></td>
                                                <td><c:out value="${u.phone}"/></td>
                                                <td><button type="button" class="btn btn-danger btn-small btn-remove-picked">Gỡ</button></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                    <div id="tenantUserIdsHidden">
                                        <c:forEach items="${selectedTenantIds}" var="sid">
                                            <input type="hidden" name="tenantUserId" value="${sid}">
                                        </c:forEach>
                                    </div>

                                    <c:choose>
                                        <c:when test="${not empty availableTenants}">
                                            <div class="form-inline" style="margin-top:12px;">
                                                <select id="pickTenantSelect">
                                                    <option value="">-- Chọn người thuê --</option>
                                                    <c:forEach items="${availableTenants}" var="t">
                                                        <option value="${t.id}"><c:out value="${t.fullName}"/> — <c:out value="${t.email}"/> — <c:out value="${t.phone}"/></option>
                                                    </c:forEach>
                                                </select>
                                                <button type="button" id="btnAddPickedTenant" class="btn btn-primary btn-small">Thêm người thuê</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p style="margin-top:12px;color:#64748b;font-size:0.9rem;">
                                                <c:choose>
                                                    <c:when test="${tenantSearchMode}">
                                                        Không tìm thấy sinh viên phù hợp. Thử từ khóa khác hoặc
                                                        <a href="${pageContext.request.contextPath}/contract?action=add<c:forEach items="${selectedTenantIds}" var="pid">&amp;pickedTenantId=${pid}</c:forEach>">xem danh sách đầy đủ</a>.
                                                    </c:when>
                                                    <c:otherwise>
                                                        Không còn sinh viên nào để thêm (đã chọn hết trong tìm kiếm hoặc chưa có tài khoản sinh viên).
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <button type="submit" class="btn btn-primary">Tạo hợp đồng</button>
                                <a href="${pageContext.request.contextPath}/contract" class="btn">Hủy</a>
                            </form>
                            <script>
(function () {
    var bh = document.getElementById('boardingHouseSelect');
    var room = document.getElementById('roomSelect');
    var rent = document.getElementById('rentPriceInput');
    var maxHint = document.getElementById('maxPersonHint');
    var pickedHint = document.getElementById('pickedCountHint');
    var searchInput = document.getElementById('tenantSearchInput');
    var btnTenantSearch = document.getElementById('btnTenantSearch');
    var btnTenantSearchAll = document.getElementById('btnTenantSearchAll');
    var tenantUserIdsHidden = document.getElementById('tenantUserIdsHidden');
    var pickedBody = document.getElementById('pickedTenantsBody');
    var pickSelect = document.getElementById('pickTenantSelect');
    var btnAddPicked = document.getElementById('btnAddPickedTenant');
    var autoRent = true;
    if (!bh || !room) return;
    var roomOptions = Array.prototype.slice.call(room.querySelectorAll('option[value]:not([value=""])'));

    function getMaxPerson() {
        var sel = room.selectedOptions[0];
        if (!sel || !sel.value) return 0;
        var m = sel.getAttribute('data-max-person');
        var n = m != null && m !== '' ? parseInt(m, 10) : 0;
        return isNaN(n) ? 0 : n;
    }
    function updateMaxHint() {
        if (!maxHint) return;
        var mp = getMaxPerson();
        maxHint.textContent = mp > 0
            ? 'Phòng này tối đa ' + mp + ' người ở.'
            : 'Phòng chưa cấu hình sức chứa (0) — không thể thêm người thuê.';
    }
    function pickedCount() {
        return tenantUserIdsHidden ? tenantUserIdsHidden.querySelectorAll('input[name="tenantUserId"]').length : 0;
    }
    function updatePickedHint() {
        if (!pickedHint) return;
        var mp = getMaxPerson();
        var n = pickedCount();
        if (mp > 0) {
            pickedHint.textContent = 'Đang chọn ' + n + ' / tối đa ' + mp + ' người.';
        } else {
            pickedHint.textContent = 'Đang chọn ' + n + ' người.';
        }
    }
    function buildTenantSearchUrl(includeQuery) {
        var params = new URLSearchParams();
        params.set('action', 'add');
        if (bh && bh.value) params.set('boardingHouseId', bh.value);
        if (room && room.value) params.set('roomId', room.value);
        if (includeQuery && searchInput && searchInput.value && searchInput.value.trim() !== '') {
            params.set('tenantSearch', searchInput.value.trim());
        }
        if (tenantUserIdsHidden) {
            tenantUserIdsHidden.querySelectorAll('input[name="tenantUserId"]').forEach(function (inp) {
                params.append('pickedTenantId', inp.value);
            });
        }
        return '${pageContext.request.contextPath}/contract?' + params.toString();
    }
    if (btnTenantSearch) {
        btnTenantSearch.addEventListener('click', function () {
            window.location.href = buildTenantSearchUrl(true);
        });
    }
    if (btnTenantSearchAll) {
        btnTenantSearchAll.addEventListener('click', function () {
            window.location.href = buildTenantSearchUrl(false);
        });
    }
    function escapeHtml(s) {
        if (!s) return '';
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }
    function addPickedRow(id, labelParts) {
        if (!tenantUserIdsHidden || !pickedBody) return;
        var mp = getMaxPerson();
        if (mp <= 0) {
            alert('Phòng chưa có sức chứa hợp lệ để thêm người.');
            return;
        }
        if (pickedCount() >= mp) {
            alert('Đã đạt số người tối đa cho phòng này (' + mp + ').');
            return;
        }
        var exists = tenantUserIdsHidden.querySelector('input[name="tenantUserId"][value="' + id + '"]');
        if (exists) return;
        var inp = document.createElement('input');
        inp.type = 'hidden';
        inp.name = 'tenantUserId';
        inp.value = String(id);
        tenantUserIdsHidden.appendChild(inp);
        var tr = document.createElement('tr');
        tr.setAttribute('data-user-id', String(id));
        tr.innerHTML = '<td>' + escapeHtml(labelParts[0] || '') + '</td>' +
            '<td>' + escapeHtml(labelParts[1] || '') + '</td>' +
            '<td>' + escapeHtml(labelParts[2] || '') + '</td>' +
            '<td><button type="button" class="btn btn-danger btn-small btn-remove-picked">Gỡ</button></td>';
        pickedBody.appendChild(tr);
        if (pickSelect) {
            var opt = pickSelect.querySelector('option[value="' + id + '"]');
            if (opt) opt.remove();
            pickSelect.value = '';
        }
        updatePickedHint();
    }
    function removePicked(id) {
        if (!tenantUserIdsHidden || !pickedBody) return;
        var inp = tenantUserIdsHidden.querySelector('input[name="tenantUserId"][value="' + id + '"]');
        if (inp) inp.remove();
        var tr = pickedBody.querySelector('tr[data-user-id="' + id + '"]');
        if (tr && pickSelect && !pickSelect.querySelector('option[value="' + id + '"]')) {
            var tds = tr.querySelectorAll('td');
            if (tds.length >= 3) {
                var opt = document.createElement('option');
                opt.value = String(id);
                opt.textContent = tds[0].textContent.trim() + ' — ' + tds[1].textContent.trim() + ' — ' + tds[2].textContent.trim();
                pickSelect.insertBefore(opt, pickSelect.options[1] || null);
            }
        }
        if (tr) tr.remove();
        updatePickedHint();
    }
    document.addEventListener('click', function (ev) {
        var t = ev.target;
        if (t && t.classList && t.classList.contains('btn-remove-picked')) {
            var tr = t.closest('tr');
            if (tr && tr.getAttribute('data-user-id')) {
                removePicked(tr.getAttribute('data-user-id'));
            }
        }
    });
    if (btnAddPicked && pickSelect) {
        btnAddPicked.addEventListener('click', function () {
            var opt = pickSelect.selectedOptions[0];
            if (!opt || !opt.value) {
                alert('Vui lòng chọn người thuê.');
                return;
            }
            var txt = (opt.textContent || '').split('—').map(function (s) { return s.trim(); });
            addPickedRow(opt.value, txt);
        });
    }
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
        if (bid !== '' && sel && (sel.value === '' || sel.disabled)) {
            room.value = '';
        }
        if (bid !== '' && room.value === '' && firstMatch) {
            room.value = firstMatch.value;
            applyRent();
        }
        updateMaxHint();
        updatePickedHint();
    }
    function applyRent() {
        if (!autoRent || !rent) return;
        var sel = room.selectedOptions[0];
        if (!sel || !sel.value) {
            rent.value = '';
            return;
        }
        var p = sel.getAttribute('data-price');
        if (p !== null && p !== '') rent.value = p;
    }
    bh.addEventListener('change', function () { syncRooms(); });
    room.addEventListener('change', function () { applyRent(); updateMaxHint(); updatePickedHint(); });
    syncRooms();
    if (rent && (!rent.value || rent.value === '0')) applyRent();
    updateMaxHint();
    updatePickedHint();
    var bhReal = bh.querySelectorAll('option[value]:not([value=""])');
    if (bh.value === '' && bhReal.length === 1) {
        bh.value = bhReal[0].value;
        syncRooms();
        applyRent();
    }
})();
</script>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <jsp:include page="../common/footer.jsp"/>
        </div>
    </div>
</body>
</html>
