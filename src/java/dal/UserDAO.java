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

        String sql = "INSERT INTO [user](full_name, phone, cccd, email, role, is_active) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, u.getFullName());
            st.setString(2, u.getPhone());
            st.setString(3, u.getCccd());
            st.setString(4, u.getEmail());
            st.setString(5, u.getRole());
            st.setBoolean(6, u.getIsActive());

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
