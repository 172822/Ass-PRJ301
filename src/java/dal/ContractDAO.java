package dal;

import models.Contract;
import java.sql.*;
import java.util.*;

public class ContractDAO extends DBContext {

    public List<Contract> getAll() {

        List<Contract> list = new ArrayList<>();
        String sql = "SELECT * FROM contract";

        try {

            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                list.add(new Contract(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("deposit"),
                        rs.getDouble("rent_price"),
                        rs.getString("status")
                ));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Contract getById(int id) {
        String sql = "SELECT * FROM contract WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Contract(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("deposit"),
                        rs.getDouble("rent_price"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Contract> getByRoomId(int roomId) {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT * FROM contract WHERE room_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roomId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Contract(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("deposit"),
                        rs.getDouble("rent_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Contract> getByUserId(int userId) {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT c.* FROM contract c INNER JOIN contract_user cu ON c.id = cu.contract_id WHERE cu.user_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Contract(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getDouble("deposit"),
                        rs.getDouble("rent_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Contract c) {
        String sql = "INSERT INTO contract(room_id, start_date, end_date, deposit, rent_price, status) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, c.getRoomId());
            st.setDate(2, c.getStartDate());
            st.setDate(3, c.getEndDate());
            st.setDouble(4, c.getDeposit());
            st.setDouble(5, c.getRentPrice());
            st.setString(6, c.getStatus());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Contract c) {
        String sql = "UPDATE contract SET room_id=?, start_date=?, end_date=?, deposit=?, rent_price=?, status=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, c.getRoomId());
            st.setDate(2, c.getStartDate());
            st.setDate(3, c.getEndDate());
            st.setDouble(4, c.getDeposit());
            st.setDouble(5, c.getRentPrice());
            st.setString(6, c.getStatus());
            st.setInt(7, c.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM contract WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}