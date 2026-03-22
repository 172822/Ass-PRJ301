package controller;

import dal.BoardingHouseDAO;
import dal.ContractDAO;
import dal.RoomDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouse;
import models.LandlordContact;
import models.Room;
import models.RoomStatus;
import models.User;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final UserDAO userDAO = new UserDAO();

    private List<BoardingHouse> getManagedHouses(User user) {
        if (user == null) {
            return List.of();
        }
        if ("ADMIN".equals(user.getRole())) {
            return boardingHouseDAO.getAll();
        }
        return boardingHouseDAO.getByLandlordId(user.getId());
    }

    private static boolean isStudent(User user) {
        return user != null && "STUDENT".equals(user.getRole());
    }

    private static boolean isLandlord(User user) {
        return user != null && "LANDLORD".equals(user.getRole());
    }

    private static boolean managedContainsHouseId(List<BoardingHouse> managed, int boardingHouseId) {
        return managed.stream().anyMatch(h -> h.getId() != null && h.getId() == boardingHouseId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String role = user.getRole();
        if (!"ADMIN".equals(role) && !"LANDLORD".equals(role) && !"STUDENT".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<BoardingHouse> managed = getManagedHouses(user);
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            if (isStudent(user)) {
                response.sendRedirect(request.getContextPath() + "/findroom");
                return;
            }
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Room r = roomDAO.getById(Integer.parseInt(idStr));
                if (r != null && r.getBoardingHouseId() != null && managedContainsHouseId(managed, r.getBoardingHouseId())) {
                    request.setAttribute("room", r);
                } else if (isLandlord(user)) {
                    response.sendRedirect(request.getContextPath() + "/findroom");
                    return;
                }
            }
            request.setAttribute("boardinghouses", managed);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            if (isStudent(user)) {
                response.sendRedirect(request.getContextPath() + "/findroom");
                return;
            }
            String pBh = request.getParameter("boardingHouseId");
            if (pBh != null && !pBh.isBlank()) {
                try {
                    int pre = Integer.parseInt(pBh.trim());
                    if (managedContainsHouseId(managed, pre)) {
                        request.setAttribute("prefillBoardingHouseId", pre);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            request.setAttribute("boardinghouses", managed);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }

        String bhIdStr = request.getParameter("boardingHouseId");
        List<Room> rooms;
        List<BoardingHouse> boardinghousesForView;
        boolean readOnly;

        if (isStudent(user)) {
            if (bhIdStr == null || bhIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/findroom");
                return;
            }
            int bhId = Integer.parseInt(bhIdStr);
            BoardingHouse bh = boardingHouseDAO.getById(bhId);
            if (bh == null) {
                response.sendRedirect(request.getContextPath() + "/findroom");
                return;
            }
            rooms = roomDAO.getByBoardingHouseId(bhId);
            boardinghousesForView = List.of(bh);
            readOnly = true;
            request.setAttribute("filterBoardingHouseId", bhId);
        } else {
            if (bhIdStr != null && !bhIdStr.isEmpty()) {
                int bhId = Integer.parseInt(bhIdStr);
                boolean inManaged = managedContainsHouseId(managed, bhId);
                if ("ADMIN".equals(role) || inManaged) {
                    rooms = roomDAO.getByBoardingHouseId(bhId);
                    boardinghousesForView = managed;
                    readOnly = false;
                    request.setAttribute("filterBoardingHouseId", bhId);
                } else {
                    BoardingHouse bh = boardingHouseDAO.getById(bhId);
                    if (bh == null) {
                        response.sendRedirect(request.getContextPath() + "/room");
                        return;
                    }
                    rooms = roomDAO.getByBoardingHouseId(bhId);
                    boardinghousesForView = List.of(bh);
                    readOnly = true;
                    request.setAttribute("filterBoardingHouseId", bhId);
                }
            } else {
                List<Integer> houseIds = managed.stream().map(BoardingHouse::getId).collect(Collectors.toList());
                rooms = roomDAO.getAll().stream()
                        .filter(r -> houseIds.contains(r.getBoardingHouseId()))
                        .collect(Collectors.toList());
                boardinghousesForView = managed;
                readOnly = false;
            }
        }

        request.setAttribute("rooms", rooms);
        request.setAttribute("boardinghouses", boardinghousesForView);
        request.setAttribute("roomListReadOnly", readOnly);
        if (!readOnly && rooms != null && !rooms.isEmpty()) {
            List<Integer> roomIds = rooms.stream().map(Room::getId).filter(Objects::nonNull).collect(Collectors.toList());
            Map<Integer, Integer> activeByRoom = contractDAO.getActiveContractIdByRoomIds(roomIds);
            Map<Integer, Integer> latestByRoom = contractDAO.getLatestContractIdByRoomIds(roomIds);
            Map<Integer, Integer> editContractByRoom = new HashMap<>(activeByRoom);
            for (Room r : rooms) {
                Integer rid = r.getId();
                if (rid == null || editContractByRoom.containsKey(rid)) {
                    continue;
                }
                if ("RENTED".equals(r.getStatus())) {
                    Integer latest = latestByRoom.get(rid);
                    if (latest != null) {
                        editContractByRoom.put(rid, latest);
                    }
                }
            }
            request.setAttribute("roomContractViewId", editContractByRoom);
        } else {
            request.setAttribute("roomContractViewId", Collections.emptyMap());
        }
        if (readOnly && boardinghousesForView.size() == 1) {
            BoardingHouse hb = boardinghousesForView.get(0);
            User lu = userDAO.getUserById(hb.getLandlordId());
            if (lu != null) {
                request.setAttribute("landlordContact", new LandlordContact(
                        lu.getFullName(), lu.getPhone(), lu.getEmail()));
            }
        }
        request.getRequestDispatcher("/views/room/list.jsp").forward(request, response);
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
        if (isStudent(user)) {
            response.sendRedirect(request.getContextPath() + "/findroom");
            return;
        }
        List<BoardingHouse> managed = getManagedHouses(user);
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Room r = roomDAO.getById(Integer.parseInt(idStr));
                if (r != null && r.getBoardingHouseId() != null && managedContainsHouseId(managed, r.getBoardingHouseId())) {
                    roomDAO.delete(Integer.parseInt(idStr));
                }
            }
            response.sendRedirect(request.getContextPath() + "/room");
            return;
        }
        String roomCode = request.getParameter("roomCode");
        String priceStr = request.getParameter("price");
        String maxPersonStr = request.getParameter("maxPerson");
        String status = request.getParameter("status");
        String boardingHouseIdStr = request.getParameter("boardingHouseId");
        if (roomCode == null || roomCode.trim().isEmpty() || boardingHouseIdStr == null || boardingHouseIdStr.isBlank()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            request.setAttribute("boardinghouses", managed);
            try {
                if (boardingHouseIdStr != null && !boardingHouseIdStr.isBlank()) {
                    request.setAttribute("prefillBoardingHouseId", Integer.parseInt(boardingHouseIdStr.trim()));
                }
            } catch (NumberFormatException ignored) {
            }
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        int boardingHouseId;
        try {
            boardingHouseId = Integer.parseInt(boardingHouseIdStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Nhà trọ không hợp lệ.");
            request.setAttribute("boardinghouses", managed);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        if (!managedContainsHouseId(managed, boardingHouseId)) {
            request.setAttribute("error", "Bạn không có quyền thêm/sửa phòng cho nhà trọ này.");
            request.setAttribute("boardinghouses", managed);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        double price;
        int maxPerson;
        try {
            price = priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr.trim()) : 0;
            maxPerson = maxPersonStr != null && !maxPersonStr.isEmpty() ? Integer.parseInt(maxPersonStr.trim()) : 1;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá hoặc số người không hợp lệ.");
            request.setAttribute("boardinghouses", managed);
            request.setAttribute("prefillBoardingHouseId", boardingHouseId);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        status = RoomStatus.parse(status).name();
        String trimmedCode = roomCode.trim();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isBlank()) {
            Room r = roomDAO.getById(Integer.parseInt(idStr.trim()));
            if (r != null && r.getId() != null && r.getBoardingHouseId() != null && managedContainsHouseId(managed, r.getBoardingHouseId())) {
                if (roomDAO.existsRoomCodeInBoardingHouse(boardingHouseId, trimmedCode, r.getId())) {
                    request.setAttribute("error", "Mã phòng này đã tồn tại trong nhà trọ. Vui lòng nhập mã khác.");
                    request.setAttribute("boardinghouses", managed);
                    request.setAttribute("room", r);
                    request.setAttribute("prefillBoardingHouseId", boardingHouseId);
                    request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
                    return;
                }
                r.setRoomCode(trimmedCode);
                r.setPrice(price);
                r.setMaxPerson(maxPerson);
                r.setStatus(status);
                r.setBoardingHouseId(boardingHouseId);
                roomDAO.update(r);
            }
        } else {
            if (roomDAO.existsRoomCodeInBoardingHouse(boardingHouseId, trimmedCode, null)) {
                request.setAttribute("error", "Mã phòng này đã tồn tại trong nhà trọ. Vui lòng nhập mã khác.");
                request.setAttribute("boardinghouses", managed);
                request.setAttribute("prefillBoardingHouseId", boardingHouseId);
                request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
                return;
            }
            Room r = new Room(null, boardingHouseId, trimmedCode, price, maxPerson, status);
            if (!roomDAO.insert(r)) {
                request.setAttribute("error", "Không thể lưu phòng (lỗi CSDL). Thử lại sau.");
                request.setAttribute("boardinghouses", managed);
                request.setAttribute("prefillBoardingHouseId", boardingHouseId);
                request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/room?boardingHouseId=" + boardingHouseId);
    }
}
