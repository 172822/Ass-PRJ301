package controller;

import dal.BoardingHouseDAO;
import dal.ContractDAO;
import dal.ContractUserDAO;
import dal.RoomDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouse;
import models.Contract;
import models.ContractUser;
import models.Room;
import models.RoomStatus;
import models.User;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContractServlet extends HttpServlet {

    private final ContractDAO contractDAO = new ContractDAO();
    private final ContractUserDAO contractUserDAO = new ContractUserDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final UserDAO userDAO = new UserDAO();

    private List<Room> getRoomsForUser(User user) {
        if (user == null) return List.of();
        if ("ADMIN".equals(user.getRole())) return roomDAO.getAll();
        List<BoardingHouse> houses = boardingHouseDAO.getByLandlordId(user.getId());
        List<Integer> houseIds = houses.stream().map(BoardingHouse::getId).collect(Collectors.toList());
        return roomDAO.getAll().stream().filter(r -> houseIds.contains(r.getBoardingHouseId())).collect(Collectors.toList());
    }

    private List<BoardingHouse> getBoardingHousesForUser(User user) {
        if (user == null) return List.of();
        if ("ADMIN".equals(user.getRole())) return boardingHouseDAO.getAll();
        if ("STUDENT".equals(user.getRole())) return List.of();
        return boardingHouseDAO.getByLandlordId(user.getId());
    }

    private List<Room> getRoomsByBoardingHouse(Integer boardingHouseId, User user) {
        List<Room> allRooms = getRoomsForUser(user);
        if (boardingHouseId == null) return allRooms;
        return allRooms.stream().filter(r -> r.getBoardingHouseId().equals(boardingHouseId)).collect(Collectors.toList());
    }

    private List<Contract> getContractsForUser(User user) {
        if (user == null) return List.of();
        if ("STUDENT".equals(user.getRole())) return contractDAO.getByUserId(user.getId());
        if ("ADMIN".equals(user.getRole())) return contractDAO.getAll();
        List<Room> myRooms = getRoomsForUser(user);
        List<Integer> roomIds = myRooms.stream().map(Room::getId).collect(Collectors.toList());
        return contractDAO.getAll().stream().filter(c -> roomIds.contains(c.getRoomId())).collect(Collectors.toList());
    }

    /** Phòng + danh sách nhà trọ tương ứng cho form tạo/sửa hợp đồng. */
    private List<Room> attachRoomsAndBoardingHousesForContractForm(HttpServletRequest request, User user) {
        List<Room> rooms = getRoomsForUser(user);
        request.setAttribute("rooms", rooms);
        List<Integer> bhIds = rooms.stream()
                .map(Room::getBoardingHouseId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<BoardingHouse> housesForContract = boardingHouseDAO.getAll().stream()
                .filter(bh -> bh.getId() != null && bhIds.contains(bh.getId()))
                .sorted(Comparator.comparing(BoardingHouse::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .collect(Collectors.toList());
        request.setAttribute("boardingHousesForContract", housesForContract);
        return rooms;
    }

    private boolean hasConflictingActiveContract(int roomId, Integer excludeContractId) {
        for (Contract c : contractDAO.getByRoomId(roomId)) {
            if (c.getStatus() == null || !"active".equalsIgnoreCase(c.getStatus())) {
                continue;
            }
            if (excludeContractId != null && c.getId() != null && c.getId().equals(excludeContractId)) {
                continue;
            }
            return true;
        }
        return false;
    }

    private void syncRoomRentalStatus(int roomId) {
        Room r = roomDAO.getById(roomId);
        if (r == null) {
            return;
        }
        boolean hasActive = contractDAO.getActiveContractIdByRoomIds(Collections.singletonList(roomId)).containsKey(roomId);
        RoomStatus target = hasActive ? RoomStatus.RENTED : RoomStatus.EMPTY;
        RoomStatus current = RoomStatus.parse(r.getStatus());
        if (current != target) {
            r.setStatus(target.dbValue());
            roomDAO.update(r);
        }
    }

    private void attachTenantPickerForForm(HttpServletRequest request, List<Integer> linkedUserIds) {
        String tsParam = request.getParameter("tenantSearch");
        String ts = tsParam == null ? "" : tsParam.trim();
        List<User> pool;
        if (ts.length() >= 2) {
            pool = userDAO.searchActiveStudentsByEmailOrPhone(ts);
            request.setAttribute("tenantSearchMode", Boolean.TRUE);
        } else {
            pool = userDAO.getByRole("STUDENT");
            request.setAttribute("tenantSearchMode", Boolean.FALSE);
            if (ts.length() == 1) {
                request.setAttribute("tenantSearchHint",
                        "Nhập ít nhất 2 ký tự để tìm theo email hoặc số điện thoại.");
            }
        }
        List<User> available = pool.stream()
                .filter(u -> u.getId() != null && !linkedUserIds.contains(u.getId()))
                .collect(Collectors.toList());
        request.setAttribute("availableTenants", available);
        request.setAttribute("tenantSearch", tsParam == null ? "" : tsParam);
    }

    private List<Integer> parseDistinctTenantUserIds(HttpServletRequest request) {
        String[] raw = request.getParameterValues("tenantUserId");
        if (raw == null || raw.length == 0) {
            return new ArrayList<>();
        }
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (String s : raw) {
            if (s == null || s.isBlank()) {
                continue;
            }
            try {
                set.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return new ArrayList<>(set);
    }

    /** Giữ người đã chọn khi GET tìm kiếm lại trên màn thêm hợp đồng. */
    private List<Integer> parsePickedTenantIdsFromRequest(HttpServletRequest request) {
        String[] raw = request.getParameterValues("pickedTenantId");
        if (raw == null || raw.length == 0) {
            return new ArrayList<>();
        }
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (String s : raw) {
            if (s == null || s.isBlank()) {
                continue;
            }
            try {
                set.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return new ArrayList<>(set);
    }

    private void setSelectedTenantsForAddForm(HttpServletRequest request, List<Integer> ids) {
        List<Integer> validIds = new ArrayList<>();
        List<User> pickedUsers = new ArrayList<>();
        for (Integer id : ids) {
            if (id == null) {
                continue;
            }
            User u = userDAO.getUserById(id);
            if (u != null && "STUDENT".equals(u.getRole()) && Boolean.TRUE.equals(u.getIsActive())) {
                validIds.add(id);
                pickedUsers.add(u);
            }
        }
        request.setAttribute("selectedTenantIds", validIds);
        request.setAttribute("selectedTenantUsers", pickedUsers);
    }

    private void forwardAddFormWithError(HttpServletRequest request, HttpServletResponse response, User user, String errorMsg)
            throws ServletException, IOException {
        List<Room> rooms = attachRoomsAndBoardingHousesForContractForm(request, user);
        try {
            String roomIdStr = request.getParameter("roomId");
            if (roomIdStr != null && !roomIdStr.isBlank()) {
                int rid = Integer.parseInt(roomIdStr.trim());
                Room match = rooms.stream().filter(r -> r.getId() != null && r.getId() == rid).findFirst().orElse(null);
                if (match != null) {
                    request.setAttribute("preselectedRoomId", rid);
                    request.setAttribute("preselectedBoardingHouseId", match.getBoardingHouseId());
                }
            }
        } catch (NumberFormatException ignored) {
        }
        request.setAttribute("error", errorMsg);
        String sd = request.getParameter("startDate");
        String ed = request.getParameter("endDate");
        request.setAttribute("prefillStartDate", sd != null ? sd : "");
        request.setAttribute("prefillEndDate", ed != null ? ed : "");
        request.setAttribute("prefillDeposit", request.getParameter("deposit") != null ? request.getParameter("deposit") : "0");
        request.setAttribute("prefillRentPrice", request.getParameter("rentPrice") != null ? request.getParameter("rentPrice") : "");
        String st = request.getParameter("status");
        request.setAttribute("prefillStatus", st != null ? st : "active");
        List<Integer> sel = parseDistinctTenantUserIds(request);
        List<Integer> safeSel = sel != null ? sel : List.of();
        setSelectedTenantsForAddForm(request, safeSel);
        attachTenantPickerForForm(request, safeSel);
        request.getRequestDispatcher("/views/contract/form.jsp").forward(request, response);
    }

    private void forwardEditFormWithError(HttpServletRequest request, HttpServletResponse response, User user,
            int contractId, int roomId, Date startDate, Date endDate, double deposit, double rentPrice, String status, String errorMsg)
            throws ServletException, IOException {
        Contract draft = new Contract(contractId, roomId, startDate, endDate, deposit, rentPrice, status);
        request.setAttribute("contract", draft);
        request.setAttribute("error", errorMsg);
        attachRoomsAndBoardingHousesForContractForm(request, user);
        Room cur = roomDAO.getById(roomId);
        if (cur != null && cur.getBoardingHouseId() != null) {
            request.setAttribute("preselectedBoardingHouseId", cur.getBoardingHouseId());
        }
        request.setAttribute("preselectedRoomId", roomId);
        request.getRequestDispatcher("/views/contract/edit.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        if ("view".equals(action) || "edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Contract c = contractDAO.getById(Integer.parseInt(idStr));
                if (c != null) {
                    List<Contract> allowed = getContractsForUser(user);
                    if (allowed.stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                        request.setAttribute("contract", c);
                        request.setAttribute("room", roomDAO.getById(c.getRoomId()));
                        request.setAttribute("contractUsers", contractUserDAO.getByContractId(c.getId()));
                        if ("view".equals(action)) {
                            List<User> tenants = new ArrayList<>();
                            for (ContractUser cu : contractUserDAO.getByContractId(c.getId())) {
                                User u = userDAO.getUserById(cu.getUserId());
                                if (u != null) tenants.add(u);
                            }
                            request.setAttribute("tenantUsers", tenants);
                        }
                        if ("edit".equals(action) && !"STUDENT".equals(user.getRole())) {
                            attachRoomsAndBoardingHousesForContractForm(request, user);
                            Room cur = roomDAO.getById(c.getRoomId());
                            if (cur != null && cur.getBoardingHouseId() != null) {
                                request.setAttribute("preselectedBoardingHouseId", cur.getBoardingHouseId());
                            }
                            request.setAttribute("preselectedRoomId", c.getRoomId());
                        }
                        if (!"STUDENT".equals(user.getRole())) {
                            if ("view".equals(action)) {
                                List<Integer> linkedIds = contractUserDAO.getByContractId(c.getId()).stream()
                                        .map(ContractUser::getUserId)
                                        .collect(Collectors.toList());
                                attachTenantPickerForForm(request, linkedIds);
                            } else {
                                request.setAttribute("availableTenants", userDAO.getByRole("STUDENT"));
                            }
                        }
                        request.getRequestDispatcher("/views/contract/" + action + ".jsp").forward(request, response);
                        return;
                    }
                }
            }
            response.sendRedirect(request.getContextPath() + "/contract");
            return;
        }
        if ("add".equals(action)) {
            if ("STUDENT".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/contract");
                return;
            }
            List<Room> rooms = attachRoomsAndBoardingHousesForContractForm(request, user);
            Integer preRoom = null;
            Integer preBh = null;
            String roomIdStr = request.getParameter("roomId");
            if (roomIdStr != null && !roomIdStr.isBlank()) {
                try {
                    int rid = Integer.parseInt(roomIdStr.trim());
                    Room match = rooms.stream().filter(r -> r.getId() != null && r.getId() == rid).findFirst().orElse(null);
                    if (match != null) {
                        preRoom = rid;
                        preBh = match.getBoardingHouseId();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            String bhIdStr = request.getParameter("boardingHouseId");
            if (bhIdStr != null && !bhIdStr.isEmpty() && preBh == null) {
                try {
                    preBh = Integer.parseInt(bhIdStr.trim());
                } catch (NumberFormatException ignored) {
                }
            }
            request.setAttribute("preselectedRoomId", preRoom);
            request.setAttribute("preselectedBoardingHouseId", preBh);
            List<Integer> prePicked = parsePickedTenantIdsFromRequest(request);
            setSelectedTenantsForAddForm(request, prePicked);
            attachTenantPickerForForm(request, prePicked);
            request.getRequestDispatcher("/views/contract/form.jsp").forward(request, response);
            return;
        }
        
        String boardingHouseIdStr = request.getParameter("boardingHouseId");
        Integer filterBoardingHouseId = null;
        if (boardingHouseIdStr != null && !boardingHouseIdStr.isEmpty()) {
            filterBoardingHouseId = Integer.parseInt(boardingHouseIdStr);
        }
        
        List<Contract> allUserContracts = getContractsForUser(user);
        List<Room> filteredRooms = getRoomsByBoardingHouse(filterBoardingHouseId, user);
        List<Integer> filteredRoomIds = filteredRooms.stream().map(Room::getId).collect(Collectors.toList());
        List<Contract> filteredContracts = allUserContracts.stream()
                .filter(c -> filteredRoomIds.contains(c.getRoomId()))
                .collect(Collectors.toList());
        
        request.setAttribute("contracts", filteredContracts);
        request.setAttribute("rooms", roomDAO.getAll());
        request.setAttribute("boardinghouses", getBoardingHousesForUser(user));
        request.setAttribute("filterBoardingHouseId", filterBoardingHouseId);
        request.getRequestDispatcher("/views/contract/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        if ("addTenant".equals(action)) {
            String contractIdStr = request.getParameter("contractId");
            String userIdStr = request.getParameter("userId");
            if (contractIdStr != null && userIdStr != null && !"STUDENT".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(contractIdStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                    Room room = roomDAO.getById(c.getRoomId());
                    int current = contractUserDAO.getByContractId(c.getId()).size();
                    int maxP = room != null && room.getMaxPerson() != null ? room.getMaxPerson() : 0;
                    if (room != null && current >= maxP) {
                        response.sendRedirect(request.getContextPath() + "/contract?action=view&id=" + contractIdStr + "&error=maxTenants");
                        return;
                    }
                    contractUserDAO.insert(new ContractUser(Integer.parseInt(contractIdStr), Integer.parseInt(userIdStr)));
                }
            }
            String cid = request.getParameter("contractId");
            String tenantQ = request.getParameter("tenantSearch");
            if (cid != null) {
                StringBuilder redir = new StringBuilder(request.getContextPath())
                        .append("/contract?action=view&id=")
                        .append(cid);
                if (tenantQ != null && !tenantQ.isBlank()) {
                    redir.append("&tenantSearch=").append(URLEncoder.encode(tenantQ.trim(), StandardCharsets.UTF_8));
                }
                response.sendRedirect(redir.toString());
            } else {
                response.sendRedirect(request.getContextPath() + "/contract");
            }
            return;
        }
        if ("removeTenant".equals(action)) {
            String contractIdStr = request.getParameter("contractId");
            String userIdStr = request.getParameter("userId");
            if (contractIdStr != null && userIdStr != null && !"STUDENT".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(contractIdStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId())))
                    contractUserDAO.delete(Integer.parseInt(contractIdStr), Integer.parseInt(userIdStr));
            }
            response.sendRedirect(request.getContextPath() + "/contract?action=view&id=" + contractIdStr);
            return;
        }
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !"STUDENT".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(idStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                    int roomId = c.getRoomId();
                    contractDAO.delete(Integer.parseInt(idStr));
                    syncRoomRentalStatus(roomId);
                }
            }
            response.sendRedirect(request.getContextPath() + "/contract");
            return;
        }
        if ("save".equals(action) && !"STUDENT".equals(user.getRole())) {
            String roomIdStr = request.getParameter("roomId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String depositStr = request.getParameter("deposit");
            String rentPriceStr = request.getParameter("rentPrice");
            String status = request.getParameter("status");
            String idStr = request.getParameter("id");
            if (roomIdStr == null || startDateStr == null || endDateStr == null) {
                response.sendRedirect(request.getContextPath() + "/contract");
                return;
            }
            int roomId;
            Date startDate;
            Date endDate;
            double deposit;
            double rentPrice;
            try {
                roomId = Integer.parseInt(roomIdStr);
                startDate = Date.valueOf(startDateStr);
                endDate = Date.valueOf(endDateStr);
                deposit = depositStr != null && !depositStr.isEmpty() ? Double.parseDouble(depositStr) : 0;
                rentPrice = rentPriceStr != null && !rentPriceStr.isEmpty() ? Double.parseDouble(rentPriceStr) : 0;
            } catch (IllegalArgumentException e) {
                if (idStr != null && !idStr.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/contract");
                } else {
                    forwardAddFormWithError(request, response, user, "Dữ liệu nhập không hợp lệ.");
                }
                return;
            }
            if (status == null) {
                status = "active";
            }
            if (!getRoomsForUser(user).stream().anyMatch(r -> r.getId() == roomId)) {
                response.sendRedirect(request.getContextPath() + "/contract");
                return;
            }
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int cid = Integer.parseInt(idStr);
                    Contract c = contractDAO.getById(cid);
                    if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                        if ("active".equalsIgnoreCase(status) && hasConflictingActiveContract(roomId, cid)) {
                            forwardEditFormWithError(request, response, user, cid, roomId, startDate, endDate, deposit, rentPrice, status,
                                    "Phòng này đã có hợp đồng đang hoạt động khác.");
                            return;
                        }
                        int oldRoomId = c.getRoomId();
                        c.setRoomId(roomId);
                        c.setStartDate(startDate);
                        c.setEndDate(endDate);
                        c.setDeposit(deposit);
                        c.setRentPrice(rentPrice);
                        c.setStatus(status);
                        contractDAO.update(c);
                        syncRoomRentalStatus(oldRoomId);
                        if (oldRoomId != roomId) {
                            syncRoomRentalStatus(roomId);
                        }
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/contract");
                    return;
                }
            } else {
                if ("active".equalsIgnoreCase(status) && hasConflictingActiveContract(roomId, null)) {
                    forwardAddFormWithError(request, response, user, "Phòng này đã có hợp đồng đang hoạt động.");
                    return;
                }
                Room room = roomDAO.getById(roomId);
                if (room == null) {
                    forwardAddFormWithError(request, response, user, "Không tìm thấy phòng.");
                    return;
                }
                List<Integer> tenantIds = parseDistinctTenantUserIds(request);
                if (tenantIds == null) {
                    forwardAddFormWithError(request, response, user, "Dữ liệu người thuê không hợp lệ.");
                    return;
                }
                int maxP = room.getMaxPerson() != null ? room.getMaxPerson() : 0;
                if (tenantIds.size() > maxP) {
                    forwardAddFormWithError(request, response, user,
                            "Số người thuê không được vượt quá số chỗ tối đa của phòng (" + maxP + ").");
                    return;
                }
                for (Integer tid : tenantIds) {
                    User tu = userDAO.getUserById(tid);
                    if (tu == null || !"STUDENT".equals(tu.getRole()) || !Boolean.TRUE.equals(tu.getIsActive())) {
                        forwardAddFormWithError(request, response, user, "Một hoặc nhiều tài khoản người thuê không hợp lệ.");
                        return;
                    }
                }
                Contract c = new Contract(null, roomId, startDate, endDate, deposit, rentPrice, status);
                Integer newId = contractDAO.insert(c);
                if (newId == null) {
                    forwardAddFormWithError(request, response, user, "Không thể tạo hợp đồng. Vui lòng thử lại.");
                    return;
                }
                for (Integer tid : tenantIds) {
                    contractUserDAO.insert(new ContractUser(newId, tid));
                }
                syncRoomRentalStatus(roomId);
                response.sendRedirect(request.getContextPath() + "/contract?action=view&id=" + newId);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/contract");
    }
}
