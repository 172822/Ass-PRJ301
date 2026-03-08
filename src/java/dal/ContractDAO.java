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
}