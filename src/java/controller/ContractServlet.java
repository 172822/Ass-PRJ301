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
import models.User;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractServlet extends HttpServlet {

    private final ContractDAO contractDAO = new ContractDAO();
    private final ContractUserDAO contractUserDAO = new ContractUserDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final UserDAO userDAO = new UserDAO();

    private List<Room> getRoomsForUser(User user) {
        if (user == null) return List.of();
        if ("admin".equals(user.getRole())) return roomDAO.getAll();
        List<BoardingHouse> houses = boardingHouseDAO.getByLandlordId(user.getId());
        List<Integer> houseIds = houses.stream().map(BoardingHouse::getId).collect(Collectors.toList());
        return roomDAO.getAll().stream().filter(r -> houseIds.contains(r.getBoardingHouseId())).collect(Collectors.toList());
    }

    private List<Contract> getContractsForUser(User user) {
        if (user == null) return List.of();
        if ("tenant".equals(user.getRole())) return contractDAO.getByUserId(user.getId());
        if ("admin".equals(user.getRole())) return contractDAO.getAll();
        List<Room> myRooms = getRoomsForUser(user);
        List<Integer> roomIds = myRooms.stream().map(Room::getId).collect(Collectors.toList());
        return contractDAO.getAll().stream().filter(c -> roomIds.contains(c.getRoomId())).collect(Collectors.toList());
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
                        if ("edit".equals(action) && !"tenant".equals(user.getRole())) {
                            request.setAttribute("rooms", getRoomsForUser(user));
                        }
                        if (!"tenant".equals(user.getRole())) {
                            request.setAttribute("availableTenants", userDAO.getByRole("tenant"));
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
            if ("tenant".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/contract");
                return;
            }
            request.setAttribute("rooms", getRoomsForUser(user));
            request.getRequestDispatcher("/views/contract/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("contracts", getContractsForUser(user));
        request.setAttribute("rooms", roomDAO.getAll());
        request.setAttribute("boardinghouses", boardingHouseDAO.getAll());
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
            if (contractIdStr != null && userIdStr != null && !"tenant".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(contractIdStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                    contractUserDAO.insert(new ContractUser(Integer.parseInt(contractIdStr), Integer.parseInt(userIdStr)));
                }
            }
            String cid = request.getParameter("contractId");
            response.sendRedirect(request.getContextPath() + "/contract" + (cid != null ? "?action=view&id=" + cid : ""));
            return;
        }
        if ("removeTenant".equals(action)) {
            String contractIdStr = request.getParameter("contractId");
            String userIdStr = request.getParameter("userId");
            if (contractIdStr != null && userIdStr != null && !"tenant".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(contractIdStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId())))
                    contractUserDAO.delete(Integer.parseInt(contractIdStr), Integer.parseInt(userIdStr));
            }
            response.sendRedirect(request.getContextPath() + "/contract?action=view&id=" + contractIdStr);
            return;
        }
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !"tenant".equals(user.getRole())) {
                Contract c = contractDAO.getById(Integer.parseInt(idStr));
                if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId())))
                    contractDAO.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/contract");
            return;
        }
        if ("save".equals(action) && !"tenant".equals(user.getRole())) {
            String roomIdStr = request.getParameter("roomId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String depositStr = request.getParameter("deposit");
            String rentPriceStr = request.getParameter("rentPrice");
            String status = request.getParameter("status");
            if (roomIdStr != null && startDateStr != null && endDateStr != null) {
                int roomId = Integer.parseInt(roomIdStr);
                if (getRoomsForUser(user).stream().anyMatch(r -> r.getId() == roomId)) {
                    Date startDate = Date.valueOf(startDateStr);
                    Date endDate = Date.valueOf(endDateStr);
                    double deposit = depositStr != null && !depositStr.isEmpty() ? Double.parseDouble(depositStr) : 0;
                    double rentPrice = rentPriceStr != null && !rentPriceStr.isEmpty() ? Double.parseDouble(rentPriceStr) : 0;
                    if (status == null) status = "active";
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        Contract c = contractDAO.getById(Integer.parseInt(idStr));
                        if (c != null && getContractsForUser(user).stream().anyMatch(a -> a.getId().equals(c.getId()))) {
                            c.setRoomId(roomId);
                            c.setStartDate(startDate);
                            c.setEndDate(endDate);
                            c.setDeposit(deposit);
                            c.setRentPrice(rentPrice);
                            c.setStatus(status);
                            contractDAO.update(c);
                        }
                    } else {
                        Contract c = new Contract(null, roomId, startDate, endDate, deposit, rentPrice, status);
                        contractDAO.insert(c);
                    }
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/contract");
    }
}
