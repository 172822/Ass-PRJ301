package controller;

import dal.BoardingHouseDAO;
import dal.ContractDAO;
import dal.InvoiceDAO;
import dal.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import models.BoardingHouse;
import models.Contract;
import models.Invoice;
import models.Room;

public class DashboardServlet extends HttpServlet {

    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String role = user.getRole();
        if ("ADMIN".equals(role)) {
            request.setAttribute("boardingHouseCount", boardingHouseDAO.getAll().size());
            request.setAttribute("roomCount", roomDAO.getAll().size());
            request.setAttribute("contractCount", contractDAO.getAll().size());
            request.setAttribute("invoiceCount", invoiceDAO.getAll().size());
        } else if ("LANDLORD".equals(role)) {
            List<BoardingHouse> myHouses = boardingHouseDAO.getAll().stream()
                    .filter(b -> user.getId().equals(b.getLandlordId()))
                    .collect(Collectors.toList());
            List<Integer> houseIds = myHouses.stream().map(BoardingHouse::getId).collect(Collectors.toList());
            List<Room> myRooms = roomDAO.getAll().stream()
                    .filter(r -> houseIds.contains(r.getBoardingHouseId()))
                    .collect(Collectors.toList());
            List<Integer> roomIds = myRooms.stream().map(Room::getId).collect(Collectors.toList());
            long contractCount = contractDAO.getAll().stream()
                    .filter(c -> roomIds.contains(c.getRoomId())).count();
            long invoiceCount = invoiceDAO.getAll().stream()
                    .filter(i -> roomIds.contains(i.getRoomId())).count();
            request.setAttribute("boardingHouseCount", myHouses.size());
            request.setAttribute("roomCount", myRooms.size());
            request.setAttribute("contractCount", contractCount);
            request.setAttribute("invoiceCount", invoiceCount);
        } else if ("STUDENT".equals(role)) {
            List<Contract> myContracts = contractDAO.getByUserId(user.getId());
            List<Integer> myRoomIds = myContracts.stream().map(Contract::getRoomId).collect(Collectors.toList());
            List<Invoice> myInvoices = invoiceDAO.getAll().stream()
                    .filter(i -> myRoomIds.contains(i.getRoomId()))
                    .filter(i -> i.getStatus() == null || !"paid".equalsIgnoreCase(i.getStatus()))
                    .collect(Collectors.toList());
            request.setAttribute("contracts", myContracts);
            request.setAttribute("invoices", myInvoices);
        }
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}
