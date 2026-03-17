package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

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
                User u = userDAO.getUserById(Integer.parseInt(idStr));
                if (u != null) {
                    request.setAttribute("editUser", u);
                }
            }
            request.getRequestDispatcher("/views/user/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.getRequestDispatcher("/views/user/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("users", userDAO.getAllUsers());
        request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
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
            if (idStr != null && !idStr.equals(user.getId().toString())) {
                userDAO.deleteUser(Integer.parseInt(idStr));
            }
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String cccd = request.getParameter("cccd");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String isActiveStr = request.getParameter("isActive");
        String password = request.getParameter("password");
        if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty() || role == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin.");
            request.setAttribute("editUser", new User(null, fullName, phone, cccd, email, role, "1".equals(isActiveStr)));
            request.getRequestDispatcher("/views/user/form.jsp").forward(request, response);
            return;
        }
        boolean isActive = "1".equals(isActiveStr) || "on".equalsIgnoreCase(isActiveStr);
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            User u = userDAO.getUserById(Integer.parseInt(idStr));
            if (u != null) {
                u.setFullName(fullName.trim());
                u.setPhone(phone != null ? phone.trim() : "");
                u.setCccd(cccd != null ? cccd.trim() : "");
                u.setEmail(email.trim());
                u.setRole(role);
                u.setIsActive(isActive);
                if (password != null && !password.isEmpty()) {
                    u.setPassword(password);
                    userDAO.updateUserWithPassword(u);
                } else {
                    userDAO.updateUser(u);
                }
            }
        } else {
            User u = new User();
            u.setFullName(fullName.trim());
            u.setPhone(phone != null ? phone.trim() : "");
            u.setCccd(cccd != null ? cccd.trim() : "");
            u.setEmail(email.trim());
            u.setRole(role);
            u.setIsActive(isActive);
            u.setPassword(password != null ? password : "");
            userDAO.insertUser(u);
        }
        response.sendRedirect(request.getContextPath() + "/user");
    }
}
