package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email và mật khẩu.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        User user = userDAO.getUserByEmailAndPassword(email.trim(), password);
        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
