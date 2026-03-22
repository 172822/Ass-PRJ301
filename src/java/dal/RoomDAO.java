package dal;

import models.Room;
import models.RoomStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO extends DBContext {

    private static Room mapRoomRow(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getInt("boarding_house_id"),
                rs.getString("room_code"),
                rs.getDouble("price"),
                rs.getInt("max_person"),
                RoomStatus.parse(rs.getString("status")).dbValue());
    }

    public List<Room> getAll() {

        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room";

        try {

            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapRoomRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Room getById(int id) {
        String sql = "SELECT * FROM room WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return mapRoomRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param excludeRoomId null khi thêm mới; khi sửa thì trừ chính phòng đang sửa (tránh báo trùng với chính nó).
     */
    public boolean existsRoomCodeInBoardingHouse(int boardingHouseId, String roomCode, Integer excludeRoomId) {
        String code = roomCode == null ? "" : roomCode.trim();
        if (code.isEmpty()) {
            return false;
        }
        String sql = excludeRoomId == null
                ? "SELECT COUNT(*) FROM room WHERE boarding_house_id = ? AND LOWER(LTRIM(RTRIM(room_code))) = LOWER(?)"
                : "SELECT COUNT(*) FROM room WHERE boarding_house_id = ? AND LOWER(LTRIM(RTRIM(room_code))) = LOWER(?) AND id <> ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, boardingHouseId);
            st.setString(2, code);
            if (excludeRoomId != null) {
                st.setInt(3, excludeRoomId);
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Room> getByBoardingHouseId(int boardingHouseId) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE boarding_house_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, boardingHouseId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapRoomRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Room r) {
        String sql = "INSERT INTO room(boarding_house_id,room_code,price,max_person,status) VALUES(?,?,?,?,?)";
        String dbStatus = RoomStatus.parse(r.getStatus()).dbValue();
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, r.getBoardingHouseId());
            st.setString(2, r.getRoomCode());
            st.setDouble(3, r.getPrice());
            st.setInt(4, r.getMaxPerson());
            st.setString(5, dbStatus);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update(Room r) {
        String sql = "UPDATE room SET boarding_house_id=?, room_code=?, price=?, max_person=?, status=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, r.getBoardingHouseId());
            st.setString(2, r.getRoomCode());
            st.setDouble(3, r.getPrice());
            st.setInt(4, r.getMaxPerson());
            st.setString(5, RoomStatus.parse(r.getStatus()).dbValue());
            st.setInt(6, r.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM room WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
