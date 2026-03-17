package controller;

import dal.BoardingHouseDAO;
import dal.MeterReadingDAO;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouse;
import models.MeterReading;
import models.Room;
import models.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MeterReadingServlet extends HttpServlet {

    private final MeterReadingDAO meterReadingDAO = new MeterReadingDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();

    private List<Room> getRoomsForUser(User user) {
        if (user == null) return List.of();
        if ("ADMIN".equals(user.getRole())) return roomDAO.getAll();
        List<BoardingHouse> houses = boardingHouseDAO.getByLandlordId(user.getId());
        List<Integer> houseIds = houses.stream().map(BoardingHouse::getId).collect(Collectors.toList());
        return roomDAO.getAll().stream().filter(r -> houseIds.contains(r.getBoardingHouseId())).collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || "STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                MeterReading m = meterReadingDAO.getById(Integer.parseInt(idStr));
                if (m != null && getRoomsForUser(user).stream().anyMatch(r -> r.getId().equals(m.getRoomId()))) {
                    request.setAttribute("meterreading", m);
                }
            }
            request.setAttribute("rooms", getRoomsForUser(user));
            request.getRequestDispatcher("/views/meterreading/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("rooms", getRoomsForUser(user));
            request.getRequestDispatcher("/views/meterreading/form.jsp").forward(request, response);
            return;
        }
        List<MeterReading> all = meterReadingDAO.getAll();
        List<Room> myRooms = getRoomsForUser(user);
        List<Integer> roomIds = myRooms.stream().map(Room::getId).collect(Collectors.toList());
        List<MeterReading> list = all.stream().filter(m -> roomIds.contains(m.getRoomId())).collect(Collectors.toList());
        request.setAttribute("meterreadings", list);
        request.setAttribute("rooms", myRooms);
        request.getRequestDispatcher("/views/meterreading/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || "STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                MeterReading m = meterReadingDAO.getById(Integer.parseInt(idStr));
                if (m != null && getRoomsForUser(user).stream().anyMatch(r -> r.getId().equals(m.getRoomId())))
                    meterReadingDAO.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/meterreading");
            return;
        }
        String roomIdStr = request.getParameter("roomId");
        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");
        String electricStartStr = request.getParameter("electricStart");
        String electricEndStr = request.getParameter("electricEnd");
        String waterStartStr = request.getParameter("waterStart");
        String waterEndStr = request.getParameter("waterEnd");
        if (roomIdStr == null || monthStr == null || yearStr == null) {
            response.sendRedirect(request.getContextPath() + "/meterreading");
            return;
        }
        int roomId = Integer.parseInt(roomIdStr);
        if (getRoomsForUser(user).stream().noneMatch(r -> r.getId() == roomId)) {
            response.sendRedirect(request.getContextPath() + "/meterreading");
            return;
        }
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);
        int electricStart = electricStartStr != null && !electricStartStr.isEmpty() ? Integer.parseInt(electricStartStr) : 0;
        int electricEnd = electricEndStr != null && !electricEndStr.isEmpty() ? Integer.parseInt(electricEndStr) : 0;
        int waterStart = waterStartStr != null && !waterStartStr.isEmpty() ? Integer.parseInt(waterStartStr) : 0;
        int waterEnd = waterEndStr != null && !waterEndStr.isEmpty() ? Integer.parseInt(waterEndStr) : 0;
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            MeterReading m = meterReadingDAO.getById(Integer.parseInt(idStr));
            if (m != null && getRoomsForUser(user).stream().anyMatch(r -> r.getId().equals(m.getRoomId()))) {
                m.setRoomId(roomId);
                m.setMonth(month);
                m.setYear(year);
                m.setElectricStart(electricStart);
                m.setElectricEnd(electricEnd);
                m.setWaterStart(waterStart);
                m.setWaterEnd(waterEnd);
                meterReadingDAO.update(m);
            }
        } else {
            MeterReading m = new MeterReading(null, roomId, month, year, electricStart, electricEnd, waterStart, waterEnd);
            meterReadingDAO.insert(m);
        }
        response.sendRedirect(request.getContextPath() + "/meterreading");
    }
}
