package dal;

import models.ContractUser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractUserDAO extends DBContext {

    public List<ContractUser> getByContractId(int contractId) {
        List<ContractUser> list = new ArrayList<>();
        String sql = "SELECT * FROM contract_user WHERE contract_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, contractId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new ContractUser(rs.getInt("contract_id"), rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ContractUser> getByUserId(int userId) {
        List<ContractUser> list = new ArrayList<>();
        String sql = "SELECT * FROM contract_user WHERE user_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new ContractUser(rs.getInt("contract_id"), rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(ContractUser cu) {
        String sql = "INSERT INTO contract_user(contract_id, user_id) VALUES(?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, cu.getContractId());
            st.setInt(2, cu.getUserId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int contractId, int userId) {
        String sql = "DELETE FROM contract_user WHERE contract_id = ? AND user_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, contractId);
            st.setInt(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
