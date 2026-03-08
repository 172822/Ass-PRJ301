package dal;

import models.BoardingHouse;
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
                        rs.getString("address")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(BoardingHouse b) {

        String sql = "INSERT INTO boarding_house(landlord_id,sub_area_id,name,address) VALUES(?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, b.getLandlordId());
            st.setInt(2, b.getSubAreaId());
            st.setString(3, b.getName());
            st.setString(4, b.getAddress());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}