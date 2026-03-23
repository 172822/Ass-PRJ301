package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterServlet extends HttpServlet {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String cccd = request.getParameter("cccd");
        String password = request.getParameter("password");
        String password2 = request.getParameter("passwordConfirm");

        request.setAttribute("registerFullName", fullName != null ? fullName : "");
        request.setAttribute("registerEmail", email != null ? email : "");
        request.setAttribute("registerPhone", phone != null ? phone : "");
        request.setAttribute("registerCccd", cccd != null ? cccd : "");

        if (fullName == null || fullName.trim().length() < 2) {
            request.setAttribute("error", "Họ tên cần ít nhất 2 ký tự.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (email == null || email.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập email.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        String emailTrim = email.trim();
        if (!EMAIL_PATTERN.matcher(emailTrim).matches()) {
            request.setAttribute("error", "Email không hợp lệ.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập số điện thoại.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        String phoneTrim = phone.trim();
        if (!PHONE_PATTERN.matcher(phoneTrim).matches()) {
            request.setAttribute("error", "Số điện thoại phải là 10 số bắt đầu bằng 0.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (userDAO.existsByPhone(phoneTrim)) {
            request.setAttribute("error", "Số điện thoại này đã được đăng ký.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Mật khẩu cần ít nhất 6 ký tự.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (!password.equals(password2)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (userDAO.existsByEmail(emailTrim)) {
            request.setAttribute("error", "Email này đã được đăng ký.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        User u = new User();
        u.setFullName(fullName.trim());
        u.setEmail(emailTrim);
        u.setPhone(phoneTrim);
        u.setCccd(cccd != null ? cccd.trim() : "");
        u.setRole("STUDENT");
        u.setIsActive(true);
        u.setPassword(password);
        userDAO.insertUser(u);

        response.sendRedirect(request.getContextPath() + "/login?registered=1");
    }
}
