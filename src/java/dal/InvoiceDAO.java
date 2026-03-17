package dal;

import models.Invoice;
import java.sql.*;
import java.util.*;

public class InvoiceDAO extends DBContext {

    public List<Invoice> getAll() {

        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice";

        try {

            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                list.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getDouble("room_price"),
                        rs.getDouble("electric_price"),
                        rs.getDouble("water_price"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Invoice getById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Invoice(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getDouble("room_price"),
                        rs.getDouble("electric_price"),
                        rs.getDouble("water_price"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getByRoomId(int roomId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE room_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getDouble("room_price"),
                        rs.getDouble("electric_price"),
                        rs.getDouble("water_price"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Invoice i) {
        String sql = "INSERT INTO invoice(room_id, month, year, room_price, electric_price, water_price, total_price, status) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, i.getRoomId());
            st.setInt(2, i.getMonth());
            st.setInt(3, i.getYear());
            st.setDouble(4, i.getRoomPrice());
            st.setDouble(5, i.getElectricPrice());
            st.setDouble(6, i.getWaterPrice());
            st.setDouble(7, i.getTotalPrice());
            st.setString(8, i.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Invoice i) {
        String sql = "UPDATE invoice SET room_id=?, month=?, year=?, room_price=?, electric_price=?, water_price=?, total_price=?, status=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, i.getRoomId());
            st.setInt(2, i.getMonth());
            st.setInt(3, i.getYear());
            st.setDouble(4, i.getRoomPrice());
            st.setDouble(5, i.getElectricPrice());
            st.setDouble(6, i.getWaterPrice());
            st.setDouble(7, i.getTotalPrice());
            st.setString(8, i.getStatus());
            st.setInt(9, i.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}