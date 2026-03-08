package dal;

import models.Room;
import java.sql.*;
import java.util.*;

public class RoomDAO extends DBContext {

    public List<Room> getAll() {

        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room";

        try {

            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                list.add(new Room(
                        rs.getInt("id"),
                        rs.getInt("boarding_house_id"),
                        rs.getString("room_code"),
                        rs.getDouble("price"),
                        rs.getInt("max_person"),
                        rs.getString("status")
                ));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(Room r) {

        String sql = "INSERT INTO room(boarding_house_id,room_code,price,max_person,status) VALUES(?,?,?,?,?)";

        try {

            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, r.getBoardingHouseId());
            st.setString(2, r.getRoomCode());
            st.setDouble(3, r.getPrice());
            st.setInt(4, r.getMaxPerson());
            st.setString(5, r.getStatus());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}