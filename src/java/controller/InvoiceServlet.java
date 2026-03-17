package controller;

import dal.BoardingHouseDAO;
import dal.ContractDAO;
import dal.ContractUserDAO;
import dal.InvoiceDAO;
import dal.MeterReadingDAO;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouse;
import models.ContractUser;
import models.Invoice;
import models.MeterReading;
import models.Room;
import models.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceServlet extends HttpServlet {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final MeterReadingDAO meterReadingDAO = new MeterReadingDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final ContractUserDAO contractUserDAO = new ContractUserDAO();

    private List<Room> getRoomsForUser(User user) {
        if (user == null) return List.of();
        if ("ADMIN".equals(user.getRole())) return roomDAO.getAll();
        if ("STUDENT".equals(user.getRole())) {
            List<Integer> contractIds = contractUserDAO.getByUserId(user.getId()).stream()
                    .map(ContractUser::getContractId).collect(Collectors.toList());
            return contractDAO.getAll().stream()
                    .filter(c -> contractIds.contains(c.getId()))
                    .map(c -> roomDAO.getById(c.getRoomId()))
                    .filter(r -> r != null)
                    .distinct()
                    .collect(Collectors.toList());
        }
        List<BoardingHouse> houses = boardingHouseDAO.getByLandlordId(user.getId());
        List<Integer> houseIds = houses.stream().map(BoardingHouse::getId).collect(Collectors.toList());
        return roomDAO.getAll().stream().filter(r -> houseIds.contains(r.getBoardingHouseId())).collect(Collectors.toList());
    }

    private List<Invoice> getInvoicesForUser(User user) {
        if (user == null) return List.of();
        List<Room> myRooms = getRoomsForUser(user);
        List<Integer> roomIds = myRooms.stream().map(Room::getId).collect(Collectors.toList());
        return invoiceDAO.getAll().stream().filter(i -> roomIds.contains(i.getRoomId())).collect(Collectors.toList());
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
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Invoice inv = invoiceDAO.getById(Integer.parseInt(idStr));
                if (inv != null && getInvoicesForUser(user).stream().anyMatch(i -> i.getId().equals(inv.getId()))) {
                    request.setAttribute("invoice", inv);
                }
            }
            request.setAttribute("rooms", getRoomsForUser(user));
            request.getRequestDispatcher("/views/invoice/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            if ("STUDENT".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/invoice");
                return;
            }
            request.setAttribute("rooms", getRoomsForUser(user));
            request.getRequestDispatcher("/views/invoice/form.jsp").forward(request, response);
            return;
        }
        if ("view".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Invoice inv = invoiceDAO.getById(Integer.parseInt(idStr));
                if (inv != null && getInvoicesForUser(user).stream().anyMatch(i -> i.getId().equals(inv.getId()))) {
                    request.setAttribute("invoice", inv);
                    request.setAttribute("room", roomDAO.getById(inv.getRoomId()));
                    request.getRequestDispatcher("/views/invoice/view.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/invoice");
            return;
        }
        request.setAttribute("invoices", getInvoicesForUser(user));
        request.setAttribute("rooms", roomDAO.getAll());
        request.setAttribute("boardinghouses", boardingHouseDAO.getAll());
        request.getRequestDispatcher("/views/invoice/list.jsp").forward(request, response);
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
        if ("delete".equals(action) && !"STUDENT".equals(user.getRole())) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Invoice inv = invoiceDAO.getById(Integer.parseInt(idStr));
                if (inv != null && getInvoicesForUser(user).stream().anyMatch(i -> i.getId().equals(inv.getId())))
                    invoiceDAO.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/invoice");
            return;
        }
        if ("save".equals(action) && !"STUDENT".equals(user.getRole())) {
            String roomIdStr = request.getParameter("roomId");
            String monthStr = request.getParameter("month");
            String yearStr = request.getParameter("year");
            String roomPriceStr = request.getParameter("roomPrice");
            String electricPriceStr = request.getParameter("electricPrice");
            String waterPriceStr = request.getParameter("waterPrice");
            String status = request.getParameter("status");
            if (roomIdStr != null && monthStr != null && yearStr != null) {
                int roomId = Integer.parseInt(roomIdStr);
                if (getRoomsForUser(user).stream().anyMatch(r -> r.getId() == roomId)) {
                    int month = Integer.parseInt(monthStr);
                    int year = Integer.parseInt(yearStr);
                    double roomPrice = roomPriceStr != null && !roomPriceStr.isEmpty() ? Double.parseDouble(roomPriceStr) : 0;
                    double electricPrice = electricPriceStr != null && !electricPriceStr.isEmpty() ? Double.parseDouble(electricPriceStr) : 0;
                    double waterPrice = waterPriceStr != null && !waterPriceStr.isEmpty() ? Double.parseDouble(waterPriceStr) : 0;
                    double totalPrice = roomPrice + electricPrice + waterPrice;
                    if (status == null) status = "unpaid";
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        Invoice inv = invoiceDAO.getById(Integer.parseInt(idStr));
                        if (inv != null && getInvoicesForUser(user).stream().anyMatch(i -> i.getId().equals(inv.getId()))) {
                            inv.setRoomId(roomId);
                            inv.setMonth(month);
                            inv.setYear(year);
                            inv.setRoomPrice(roomPrice);
                            inv.setElectricPrice(electricPrice);
                            inv.setWaterPrice(waterPrice);
                            inv.setTotalPrice(totalPrice);
                            inv.setStatus(status);
                            invoiceDAO.update(inv);
                        }
                    } else {
                        Invoice inv = new Invoice(null, roomId, month, year, roomPrice, electricPrice, waterPrice, totalPrice, status);
                        invoiceDAO.insert(inv);
                    }
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/invoice");
    }
}
