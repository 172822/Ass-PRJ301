package dal;

import models.BoardingHouse;
import models.BoardingHouseListItem;
import java.sql.*;
import java.util.*;

public class BoardingHouseDAO extends DBContext {

    public List<BoardingHouse> getAll() {

        List<BoardingHouse> list = new ArrayList<>();
        String sql = "SELECT * FROM boarding_house";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                list.add(new BoardingHouse(
                        rs.getInt("id"),
                        rs.getInt("landlord_id"),
                        rs.getInt("sub_area_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("image_path")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public BoardingHouse getById(int id) {
        String sql = "SELECT * FROM boarding_house WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                BoardingHouse bh = new BoardingHouse(
                        rs.getInt("id"),
                        rs.getInt("landlord_id"),
                        rs.getInt("sub_area_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("image_path"));
                return bh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BoardingHouse> getByLandlordId(int landlordId) {
        List<BoardingHouse> list = new ArrayList<>();
        String sql = "SELECT * FROM boarding_house WHERE landlord_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, landlordId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new BoardingHouse(
                        rs.getInt("id"),
                        rs.getInt("landlord_id"),
                        rs.getInt("sub_area_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(BoardingHouse b) {

        String sql = "INSERT INTO boarding_house(landlord_id,sub_area_id,name,address,image_path) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, b.getLandlordId());
            st.setInt(2, b.getSubAreaId());
            st.setString(3, b.getName());
            st.setString(4, b.getAddress());
            st.setString(5, b.getImagePath());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(BoardingHouse b) {
        String sql = "UPDATE boarding_house SET landlord_id=?, sub_area_id=?, name=?, address=?, image_path=? WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, b.getLandlordId());
            st.setInt(2, b.getSubAreaId());
            st.setString(3, b.getName());
            st.setString(4, b.getAddress());
            st.setString(5, b.getImagePath());
            st.setInt(6, b.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM boarding_house WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public List<BoardingHouseListItem> search(Integer areaId, Integer subAreaId, String nameKeyword) {
        List<BoardingHouseListItem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT bh.id, bh.name, bh.address, bh.image_path, sa.name AS sub_area_name, a.name AS area_name, "
                        + "lu.full_name AS landlord_name, lu.phone AS landlord_phone, lu.email AS landlord_email "
                        + "FROM boarding_house bh "
                        + "INNER JOIN sub_area sa ON bh.sub_area_id = sa.id "
                        + "INNER JOIN area a ON sa.area_id = a.id "
                        + "LEFT JOIN [user] lu ON bh.landlord_id = lu.id "
                        + "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (subAreaId != null) {
            sql.append("AND bh.sub_area_id = ? ");
            params.add(subAreaId);
        } else if (areaId != null) {
            sql.append("AND sa.area_id = ? ");
            params.add(areaId);
        }
        String kw = nameKeyword == null ? "" : nameKeyword.trim();
        if (!kw.isEmpty()) {
            sql.append("AND bh.name LIKE ? ESCAPE '\\' ");
            params.add("%" + escapeLikePattern(kw) + "%");
        }
        sql.append("ORDER BY a.name, sa.name, bh.name");
        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    st.setInt(i + 1, (Integer) p);
                } else {
                    st.setString(i + 1, (String) p);
                }
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new BoardingHouseListItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("area_name"),
                        rs.getString("sub_area_name"),
                        rs.getString("landlord_name"),
                        rs.getString("landlord_phone"),
                        rs.getString("landlord_email"),
                        rs.getString("image_path")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}