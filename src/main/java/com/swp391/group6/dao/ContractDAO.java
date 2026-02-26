package com.swp391.group6.dao;

import com.swp391.group6.util.DBContext;

import java.sql.*;

public class ContractDAO {

    public boolean toggleDeleted(int contractId) {
        Integer isDeleted = getIsDeletedByTd(contractId);
        if (isDeleted == null) return false;

        int newIsDeleted = (isDeleted == 1) ? 0 : 1;

        String query = "UPDATE Contracts SET isDeleted=? , updatedAt = CURRENT_TIMESTAMP WHERE id=?";

        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newIsDeleted);
            ps.setInt(2, contractId);
        }catch (SQLException e){
            System.err.println("ContractDAO.toggleDeleted failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Integer getIsDeletedByTd(int contractId) {
        String sql = "SELECT isDeleted FROM contracts WHERE id = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, contractId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {return rs.getInt("isDeleted");}
            }
        }catch (SQLException e){
            System.err.println("ContractDAO.getIsDeletedByTd failed : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
