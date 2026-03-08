package dal;

import models.SubArea;
import java.sql.*;
import java.util.*;

public class SubAreaDAO extends DBContext {

    public List<SubArea> getAll() {
        List<SubArea> list = new ArrayList<>();
        String sql = "SELECT * FROM sub_area";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new SubArea(
                        rs.getInt("id"),
                        rs.getInt("area_id"),
                        rs.getString("name")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(SubArea s) {

        String sql = "INSERT INTO sub_area(area_id,name) VALUES(?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, s.getAreaId());
            st.setString(2, s.getName());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(SubArea s) {

        String sql = "UPDATE sub_area SET area_id=?,name=? WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, s.getAreaId());
            st.setString(2, s.getName());
            st.setInt(3, s.getId());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM sub_area WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
