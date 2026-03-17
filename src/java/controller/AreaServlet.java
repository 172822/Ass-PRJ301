package controller;

import dal.AreaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Area;
import models.User;
import java.io.IOException;

public class AreaServlet extends HttpServlet {

    private final AreaDAO areaDAO = new AreaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Area a = areaDAO.getById(Integer.parseInt(idStr));
                request.setAttribute("area", a);
            }
            request.getRequestDispatcher("/views/area/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.getRequestDispatcher("/views/area/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("areas", areaDAO.getAll());
        request.getRequestDispatcher("/views/area/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) areaDAO.delete(Integer.parseInt(idStr));
            response.sendRedirect(request.getContextPath() + "/area");
            return;
        }
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên khu vực không được để trống.");
            request.getRequestDispatcher("/views/area/form.jsp").forward(request, response);
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Area a = areaDAO.getById(Integer.parseInt(idStr));
            if (a != null) {
                a.setName(name.trim());
                areaDAO.update(a);
            }
        } else {
            Area a = new Area(null, name.trim());
            areaDAO.insert(a);
        }
        response.sendRedirect(request.getContextPath() + "/area");
    }
}
