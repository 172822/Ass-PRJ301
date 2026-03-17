package dal;

import models.MeterReading;
import java.sql.*;
import java.util.*;

public class MeterReadingDAO extends DBContext {

    public List<MeterReading> getAll() {

        List<MeterReading> list = new ArrayList<>();
        String sql = "SELECT * FROM meter_reading";

        try {

            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                list.add(new MeterReading(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getInt("electric_start"),
                        rs.getInt("electric_end"),
                        rs.getInt("water_start"),
                        rs.getInt("water_end")
                ));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public MeterReading getById(int id) {
        String sql = "SELECT * FROM meter_reading WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new MeterReading(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getInt("electric_start"),
                        rs.getInt("electric_end"),
                        rs.getInt("water_start"),
                        rs.getInt("water_end")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MeterReading getByRoomIdAndMonthYear(int roomId, int month, int year) {
        String sql = "SELECT * FROM meter_reading WHERE room_id = ? AND month = ? AND year = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roomId);
            st.setInt(2, month);
            st.setInt(3, year);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new MeterReading(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getInt("electric_start"),
                        rs.getInt("electric_end"),
                        rs.getInt("water_start"),
                        rs.getInt("water_end")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(MeterReading m) {
        String sql = "INSERT INTO meter_reading(room_id, month, year, electric_start, electric_end, water_start, water_end) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, m.getRoomId());
            st.setInt(2, m.getMonth());
            st.setInt(3, m.getYear());
            st.setInt(4, m.getElectricStart());
            st.setInt(5, m.getElectricEnd());
            st.setInt(6, m.getWaterStart());
            st.setInt(7, m.getWaterEnd());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(MeterReading m) {
        String sql = "UPDATE meter_reading SET room_id=?, month=?, year=?, electric_start=?, electric_end=?, water_start=?, water_end=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, m.getRoomId());
            st.setInt(2, m.getMonth());
            st.setInt(3, m.getYear());
            st.setInt(4, m.getElectricStart());
            st.setInt(5, m.getElectricEnd());
            st.setInt(6, m.getWaterStart());
            st.setInt(7, m.getWaterEnd());
            st.setInt(8, m.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM meter_reading WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}