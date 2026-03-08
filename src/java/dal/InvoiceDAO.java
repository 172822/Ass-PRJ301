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
}