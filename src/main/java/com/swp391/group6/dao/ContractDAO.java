package com.swp391.group6.dao;

import com.swp391.group6.model.Contract;
import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContractDAO {

    private static final String BASE_SELECT =
        "SELECT c.*, " +
        "cu.fullName AS customerName, " +
        "s.fullName  AS staffName, " +
        "m.fullName  AS managerName " +
        "FROM contracts c " +
        "JOIN users cu ON c.customerId = cu.id " +
        "JOIN users s  ON c.staffId    = s.id " +
        "LEFT JOIN users m ON c.managerId  = m.id " +
        "WHERE c.isDeleted = 0 ";

    // Whitelist for sortBy to prevent SQL injection
    private static final Set<String> VALID_SORT_COLUMNS = Set.of(
        "c.id", "cu.fullName", "c.createdAt"
    );

    private String resolveSortColumn(String sortBy) {
        if ("Khach hang".equals(sortBy) || "customer".equals(sortBy)) {
            return "cu.fullName";
        } else if ("Ngay tao".equals(sortBy) || "createdAt".equals(sortBy)) {
            return "c.createdAt";
        }
        return "c.id"; // default
    }

    private String resolveOrder(String order) {
        return "DESC".equalsIgnoreCase(order) ? "DESC" : "ASC";
    }

    /**
     * Get contracts with filters and pagination
     */
    public List<Contract> findWithFilters(String search, String sortBy,
                                          String order, Integer staffId,
                                          int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(BASE_SELECT);

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (cu.fullName LIKE ? OR c.contractCode LIKE ? " +
                       "OR c.title LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (staffId != null) {
            sql.append("AND c.staffId = ? ");
            params.add(staffId);
        }

        String sortCol = resolveSortColumn(sortBy);
        String sortOrder = resolveOrder(order);
        sql.append("ORDER BY ").append(sortCol).append(" ").append(sortOrder)
           .append(" ");

        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    ps.setInt(i + 1, (Integer) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contracts.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("ContractDAO.findWithFilters failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return contracts;
    }

    /**
     * Count contracts with filters (for pagination)
     */
    public int countWithFilters(String search, String sortBy,
                                String order, Integer staffId) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM contracts c " +
            "JOIN users cu ON c.customerId = cu.id " +
            "JOIN users s  ON c.staffId    = s.id " +
            "WHERE c.isDeleted = 0 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (cu.fullName LIKE ? OR c.contractCode LIKE ? " +
                       "OR c.title LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (staffId != null) {
            sql.append("AND c.staffId = ? ");
            params.add(staffId);
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    ps.setInt(i + 1, (Integer) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("ContractDAO.countWithFilters failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Find contract by ID
     */
    public Contract findById(int id) {
        String sql = BASE_SELECT + "AND c.id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("ContractDAO.findById failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all staff users for creator filter dropdown
     */
    public List<User> getAllStaff() {
        List<User> staffList = new ArrayList<>();

        String sql = "SELECT u.id, u.fullName, u.email " +
                     "FROM users u " +
                     "JOIN roles r ON u.roleId = r.id " +
                     "WHERE r.name = 'STAFF' " +
                     "AND u.status = 1 " +
                     "AND u.isDeleted = 0 " +
                     "ORDER BY u.fullName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User staff = new User();
                staff.setId(rs.getInt("id"));
                staff.setFullName(rs.getString("fullName"));
                staff.setEmail(rs.getString("email"));
                staffList.add(staff);
            }

        } catch (SQLException e) {
            System.err.println("ContractDAO.getAllStaff failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return staffList;
    }

    /**
     * Deactivate a contract (set status = REJECTED)
     */
    public boolean deactivate(int id, int managerId) {
        String sql = "UPDATE contracts SET status = 'REJECTED', " +
                     "managerId = ?, approvedAt = NOW(), " +
                     "updatedAt = NOW() " +
                     "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("ContractDAO.deactivate failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Map ResultSet row to Contract object
     */
    private Contract mapResultSet(ResultSet rs) throws SQLException {
        Contract c = new Contract();

        c.setId(rs.getInt("id"));
        c.setContractCode(rs.getString("contractCode"));
        c.setCustomerId(rs.getInt("customerId"));
        c.setStaffId(rs.getInt("staffId"));

        int managerId = rs.getInt("managerId");
        c.setManagerId(rs.wasNull() ? null : managerId);

        c.setTitle(rs.getString("title"));
        c.setTotalAmount(rs.getBigDecimal("totalAmount"));
        c.setSaleDate(rs.getDate("saleDate"));
        c.setStatus(rs.getString("status"));
        c.setRejectionReason(rs.getString("rejectionReason"));
        c.setApprovedAt(rs.getTimestamp("approvedAt"));
        c.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        c.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        c.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        c.setCustomerName(rs.getString("customerName"));
        c.setStaffName(rs.getString("staffName"));
        c.setManagerName(rs.getString("managerName"));

        return c;
    }
}

