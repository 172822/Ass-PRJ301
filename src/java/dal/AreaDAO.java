package dal;

import models.Area;
import java.sql.*;
import java.util.*;

public class AreaDAO extends DBContext {

    public List<Area> getAll() {
        List<Area> list = new ArrayList<>();
        String sql = "SELECT * FROM area";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Area(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Area getById(int id) {
        String sql = "SELECT * FROM area WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Area(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insert(Area a) {
        String sql = "INSERT INTO area(name) VALUES(?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, a.getName());
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Area a) {
        String sql = "UPDATE area SET name=? WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, a.getName());
            st.setInt(2, a.getId());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM area WHERE id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
