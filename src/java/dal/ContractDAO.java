package dal;

import models.Contract;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Với mỗi phòng, trả về id hợp đồng đang {@code active} (nếu có nhiều, lấy id lớn nhất).
     */
    public Map<Integer, Integer> getActiveContractIdByRoomIds(Collection<Integer> roomIds) {
        Map<Integer, Integer> map = new HashMap<>();
        if (roomIds == null || roomIds.isEmpty()) {
            return map;
        }
        List<Integer> ids = roomIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return map;
        }
        StringBuilder sql = new StringBuilder(
                "SELECT id, room_id FROM contract WHERE status = 'active' AND room_id IN (");
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            for (int i = 0; i < ids.size(); i++) {
                st.setInt(i + 1, ids.get(i));
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int cid = rs.getInt("id");
                int rid = rs.getInt("room_id");
                map.merge(rid, cid, Math::max);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /** Mỗi phòng → id hợp đồng mới nhất (id lớn nhất), mọi trạng thái. */
    public Map<Integer, Integer> getLatestContractIdByRoomIds(Collection<Integer> roomIds) {
        Map<Integer, Integer> map = new HashMap<>();
        if (roomIds == null || roomIds.isEmpty()) {
            return map;
        }
        List<Integer> ids = roomIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return map;
        }
        StringBuilder sql = new StringBuilder("SELECT id, room_id FROM contract WHERE room_id IN (");
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            for (int i = 0; i < ids.size(); i++) {
                st.setInt(i + 1, ids.get(i));
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int cid = rs.getInt("id");
                int rid = rs.getInt("room_id");
                map.merge(rid, cid, Math::max);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
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