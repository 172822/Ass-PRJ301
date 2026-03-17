package controller;

import dal.BoardingHouseDAO;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Room;
import models.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();

    private List<models.BoardingHouse> getHousesForUser(User user) {
        if (user == null) return List.of();
        if ("admin".equals(user.getRole())) return boardingHouseDAO.getAll();
        return boardingHouseDAO.getByLandlordId(user.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || "tenant".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        List<models.BoardingHouse> houses = getHousesForUser(user);
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Room r = roomDAO.getById(Integer.parseInt(idStr));
                if (r != null && houses.stream().anyMatch(h -> h.getId().equals(r.getBoardingHouseId()))) {
                    request.setAttribute("room", r);
                }
            }
            request.setAttribute("boardinghouses", houses);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("boardinghouses", houses);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        String bhIdStr = request.getParameter("boardingHouseId");
        List<Room> rooms;
        if (bhIdStr != null && !bhIdStr.isEmpty()) {
            int bhId = Integer.parseInt(bhIdStr);
            if (houses.stream().noneMatch(h -> h.getId() == bhId)) {
                response.sendRedirect(request.getContextPath() + "/room");
                return;
            }
            rooms = roomDAO.getByBoardingHouseId(bhId);
            request.setAttribute("filterBoardingHouseId", bhId);
        } else {
            List<Integer> houseIds = houses.stream().map(models.BoardingHouse::getId).collect(Collectors.toList());
            rooms = roomDAO.getAll().stream().filter(r -> houseIds.contains(r.getBoardingHouseId())).collect(Collectors.toList());
        }
        request.setAttribute("rooms", rooms);
        request.setAttribute("boardinghouses", houses);
        request.getRequestDispatcher("/views/room/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || "tenant".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        List<models.BoardingHouse> houses = getHousesForUser(user);
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Room r = roomDAO.getById(Integer.parseInt(idStr));
                if (r != null && houses.stream().anyMatch(h -> h.getId().equals(r.getBoardingHouseId())))
                    roomDAO.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/room");
            return;
        }
        String roomCode = request.getParameter("roomCode");
        String priceStr = request.getParameter("price");
        String maxPersonStr = request.getParameter("maxPerson");
        String status = request.getParameter("status");
        String boardingHouseIdStr = request.getParameter("boardingHouseId");
        if (roomCode == null || roomCode.trim().isEmpty() || boardingHouseIdStr == null || boardingHouseIdStr.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            request.setAttribute("boardinghouses", houses);
            request.getRequestDispatcher("/views/room/form.jsp").forward(request, response);
            return;
        }
        int boardingHouseId = Integer.parseInt(boardingHouseIdStr);
        if (houses.stream().noneMatch(h -> h.getId() == boardingHouseId)) {
            response.sendRedirect(request.getContextPath() + "/room");
            return;
        }
        double price = priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr) : 0;
        int maxPerson = maxPersonStr != null && !maxPersonStr.isEmpty() ? Integer.parseInt(maxPersonStr) : 1;
        if (status == null) status = "available";
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Room r = roomDAO.getById(Integer.parseInt(idStr));
            if (r != null && houses.stream().anyMatch(h -> h.getId().equals(r.getBoardingHouseId()))) {
                r.setRoomCode(roomCode.trim());
                r.setPrice(price);
                r.setMaxPerson(maxPerson);
                r.setStatus(status);
                r.setBoardingHouseId(boardingHouseId);
                roomDAO.update(r);
            }
        } else {
            Room r = new Room(null, boardingHouseId, roomCode.trim(), price, maxPerson, status);
            roomDAO.insert(r);
        }
        response.sendRedirect(request.getContextPath() + "/room?boardingHouseId=" + boardingHouseId);
    }
}
