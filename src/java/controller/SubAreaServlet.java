package controller;

import dal.AreaDAO;
import dal.SubAreaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.SubArea;
import models.User;
import java.io.IOException;

public class SubAreaServlet extends HttpServlet {

    private final SubAreaDAO subAreaDAO = new SubAreaDAO();
    private final AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                SubArea s = subAreaDAO.getById(Integer.parseInt(idStr));
                request.setAttribute("subarea", s);
            }
            request.setAttribute("areas", areaDAO.getAll());
            request.getRequestDispatcher("/views/subarea/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("areas", areaDAO.getAll());
            request.getRequestDispatcher("/views/subarea/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("subareas", subAreaDAO.getAll());
        request.setAttribute("areas", areaDAO.getAll());
        request.getRequestDispatcher("/views/subarea/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) subAreaDAO.delete(Integer.parseInt(idStr));
            response.sendRedirect(request.getContextPath() + "/subarea");
            return;
        }
        String name = request.getParameter("name");
        String areaIdStr = request.getParameter("areaId");
        if (name == null || name.trim().isEmpty() || areaIdStr == null || areaIdStr.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            request.setAttribute("areas", areaDAO.getAll());
            request.getRequestDispatcher("/views/subarea/form.jsp").forward(request, response);
            return;
        }
        int areaId = Integer.parseInt(areaIdStr);
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            SubArea s = subAreaDAO.getById(Integer.parseInt(idStr));
            if (s != null) {
                s.setAreaId(areaId);
                s.setName(name.trim());
                subAreaDAO.update(s);
            }
        } else {
            SubArea s = new SubArea(null, areaId, name.trim());
            subAreaDAO.insert(s);
        }
        response.sendRedirect(request.getContextPath() + "/subarea");
    }
}
