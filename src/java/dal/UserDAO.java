/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author Admin
 */

import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBContext {

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM [user]";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                User u = new User();

                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setCccd(rs.getString("cccd"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setIsActive(rs.getBoolean("is_active"));
                u.setPassword(rs.getString("password"));

                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM [user] WHERE email = ? AND password = ? AND is_active = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setCccd(rs.getString("cccd"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setIsActive(rs.getBoolean("is_active"));
                u.setPassword(rs.getString("password"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM [user] WHERE email = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email.trim());
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM [user] WHERE phone = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, phone.trim());
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getByRole(String role) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [user] WHERE role = ? AND is_active = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, role);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setCccd(rs.getString("cccd"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setIsActive(rs.getBoolean("is_active"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String escapeLikePattern(String raw) {
        if (raw == null || raw.isEmpty()) {
            return raw;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == '\\' || c == '%' || c == '_') {
                sb.append('\\').append(c);
            } else if (c == '[') {
                sb.append("[[]");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Sinh viên đang hoạt động, khớp gần đúng email hoặc SĐT (LIKE, có escape).
     * Trả về tối đa 50 bản ghi; keyword cắt còn tối đa 100 ký tự.
     */
    public List<User> searchActiveStudentsByEmailOrPhone(String keyword) {
        List<User> list = new ArrayList<>();
        if (keyword == null) {
            return list;
        }
        String k = keyword.trim();
        if (k.length() < 2) {
            return list;
        }
        if (k.length() > 100) {
            k = k.substring(0, 100);
        }
        String pattern = "%" + escapeLikePattern(k) + "%";
        String sql = "SELECT TOP (50) id, full_name, phone, cccd, email, role, is_active FROM [user] "
                + "WHERE role = ? AND is_active = 1 "
                + "AND ((email IS NOT NULL AND email LIKE ? ESCAPE '\\') OR (phone IS NOT NULL AND phone LIKE ? ESCAPE '\\')) "
                + "ORDER BY full_name";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "STUDENT");
            st.setString(2, pattern);
            st.setString(3, pattern);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setCccd(rs.getString("cccd"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setIsActive(rs.getBoolean("is_active"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUserById(int id) {

        String sql = "SELECT * FROM [user] WHERE id = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                User u = new User();

                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setPhone(rs.getString("phone"));
                u.setCccd(rs.getString("cccd"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setIsActive(rs.getBoolean("is_active"));

                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertUser(User u) {

        String sql = "INSERT INTO [user](full_name, phone, cccd, email, role, is_active, password) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, u.getFullName());
            st.setString(2, u.getPhone());
            st.setString(3, u.getCccd());
            st.setString(4, u.getEmail());
            st.setString(5, u.getRole());
            st.setBoolean(6, u.getIsActive());
            st.setString(7, u.getPassword() != null ? u.getPassword() : "");

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User u) {

        String sql = "UPDATE [user] SET full_name=?, phone=?, cccd=?, email=?, role=?, is_active=? WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, u.getFullName());
            st.setString(2, u.getPhone());
            st.setString(3, u.getCccd());
            st.setString(4, u.getEmail());
            st.setString(5, u.getRole());
            st.setBoolean(6, u.getIsActive());
            st.setInt(7, u.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserWithPassword(User u) {
        String sql = "UPDATE [user] SET full_name=?, phone=?, cccd=?, email=?, role=?, is_active=?, password=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, u.getFullName());
            st.setString(2, u.getPhone());
            st.setString(3, u.getCccd());
            st.setString(4, u.getEmail());
            st.setString(5, u.getRole());
            st.setBoolean(6, u.getIsActive());
            st.setString(7, u.getPassword() != null ? u.getPassword() : "");
            st.setInt(8, u.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {

        String sql = "DELETE FROM [user] WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
