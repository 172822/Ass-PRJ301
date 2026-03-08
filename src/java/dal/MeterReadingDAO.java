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
}