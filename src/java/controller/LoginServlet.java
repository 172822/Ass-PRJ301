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

    /** Relative path only; blocks open redirects. */
    private static String safeInternalNext(String next) {
        if (next == null) {
            return null;
        }
        String n = next.trim();
        if (n.isEmpty() || !n.startsWith("/")) {
            return null;
        }
        if (n.contains("..") || n.toLowerCase().contains("://")) {
            return null;
        }
        if (n.indexOf('\r') >= 0 || n.indexOf('\n') >= 0) {
            return null;
        }
        return n;
    }

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
            request.setAttribute("loginNext", request.getParameter("next"));
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        User user = userDAO.getUserByEmailAndPassword(email.trim(), password);
        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            request.setAttribute("loginNext", request.getParameter("next"));
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        String next = safeInternalNext(request.getParameter("next"));
        if (next != null) {
            response.sendRedirect(request.getContextPath() + next);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
