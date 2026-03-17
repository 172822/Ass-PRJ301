package controller;

import dal.BoardingHouseDAO;
import dal.SubAreaDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouse;
import models.User;
import java.io.IOException;
import java.util.List;

public class BoardingHouseServlet extends HttpServlet {

    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final SubAreaDAO subAreaDAO = new SubAreaDAO();
    private final UserDAO userDAO = new UserDAO();

    private List<BoardingHouse> getListForUser(User user) {
        if (user == null) return List.of();
        if ("admin".equals(user.getRole())) return boardingHouseDAO.getAll();
        return boardingHouseDAO.getByLandlordId(user.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || ("tenant".equals(user.getRole()))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                BoardingHouse b = boardingHouseDAO.getById(Integer.parseInt(idStr));
                if (b != null && ("admin".equals(user.getRole()) || user.getId().equals(b.getLandlordId()))) {
                    request.setAttribute("boardinghouse", b);
                }
            }
            request.setAttribute("subareas", subAreaDAO.getAll());
            if ("admin".equals(user.getRole())) request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("subareas", subAreaDAO.getAll());
            if ("admin".equals(user.getRole())) request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("boardinghouses", getListForUser(user));
        request.setAttribute("subareas", subAreaDAO.getAll());
        request.getRequestDispatcher("/views/boardinghouse/list.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                BoardingHouse b = boardingHouseDAO.getById(Integer.parseInt(idStr));
                if (b != null && ("admin".equals(user.getRole()) || user.getId().equals(b.getLandlordId())))
                    boardingHouseDAO.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/boardinghouse");
            return;
        }
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String subAreaIdStr = request.getParameter("subAreaId");
        if (name == null || name.trim().isEmpty() || subAreaIdStr == null || subAreaIdStr.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            request.setAttribute("subareas", subAreaDAO.getAll());
            if ("admin".equals(user.getRole())) request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
            return;
        }
        int landlordId = user.getId();
        if ("admin".equals(user.getRole())) {
            String lid = request.getParameter("landlordId");
            if (lid != null && !lid.isEmpty()) landlordId = Integer.parseInt(lid);
        }
        int subAreaId = Integer.parseInt(subAreaIdStr);
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            BoardingHouse b = boardingHouseDAO.getById(Integer.parseInt(idStr));
            if (b != null && ("admin".equals(user.getRole()) || user.getId().equals(b.getLandlordId()))) {
                b.setName(name.trim());
                b.setAddress(address != null ? address.trim() : "");
                b.setSubAreaId(subAreaId);
                b.setLandlordId(landlordId);
                boardingHouseDAO.update(b);
            }
        } else {
            BoardingHouse b = new BoardingHouse(null, landlordId, subAreaId, name.trim(), address != null ? address.trim() : "");
            boardingHouseDAO.insert(b);
        }
        response.sendRedirect(request.getContextPath() + "/boardinghouse");
    }
}
