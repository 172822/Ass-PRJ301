package controller;

import dal.AreaDAO;
import dal.BoardingHouseDAO;
import dal.SubAreaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BoardingHouseListItem;
import models.SubArea;
import models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FindRoomServlet extends HttpServlet {

    private final AreaDAO areaDAO = new AreaDAO();
    private final SubAreaDAO subAreaDAO = new SubAreaDAO();
    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();

    private static Integer parseIntOrNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer areaId = parseIntOrNull(request.getParameter("areaId"));
        Integer subAreaId = parseIntOrNull(request.getParameter("subAreaId"));
        String q = request.getParameter("q");

        if (subAreaId != null) {
            SubArea sa = subAreaDAO.getById(subAreaId);
            if (sa == null) {
                subAreaId = null;
            } else if (areaId != null && !areaId.equals(sa.getAreaId())) {
                subAreaId = null;
            } else if (areaId == null) {
                areaId = sa.getAreaId();
            }
        }

        final Integer resolvedAreaId = areaId;
        List<SubArea> subAreas = new ArrayList<>();
        if (resolvedAreaId != null) {
            subAreas = subAreaDAO.getAll().stream()
                    .filter(s -> resolvedAreaId.equals(s.getAreaId()))
                    .sorted(Comparator.comparing(SubArea::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                    .collect(Collectors.toList());
        }

        List<BoardingHouseListItem> results = boardingHouseDAO.search(areaId, subAreaId, q);

        request.setAttribute("areas", areaDAO.getAll());
        request.setAttribute("subAreas", subAreas);
        request.setAttribute("results", results);
        request.setAttribute("selectedAreaId", areaId);
        request.setAttribute("selectedSubAreaId", subAreaId);
        request.setAttribute("q", q != null ? q : "");

        request.getRequestDispatcher("/views/findroom.jsp").forward(request, response);
    }
}
