package com.swp391.group6.dao;

import com.swp391.group6.model.ServiceRequest;
import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    private static final String BASE_SELECT =
        "SELECT sr.*, " +
        "c.fullName AS customerName, " +
        "s.fullName AS assignedToName, " +
        "d.serialNumber AS deviceSerialNumber " +
        "FROM service_requests sr " +
        "JOIN users c ON sr.customerId = c.id " +
        "LEFT JOIN users s ON sr.assignedTo = s.id " +
        "LEFT JOIN devices d ON sr.deviceId = d.id ";

    /**
     * Get service requests with filters and pagination
     */
    public List<ServiceRequest> findWithFilters(
            String search, String status,
            String requestType, String priority,
            Integer customerId, int page,
            int pageSize) {
        List<ServiceRequest> list = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        sql.append("WHERE sr.isDeleted = 0 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(
                "AND (sr.requestCode LIKE ? " +
                "OR sr.subject LIKE ? " +
                "OR c.fullName LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND sr.status = ? ");
            params.add(status);
        }

        if (requestType != null
                && !requestType.trim().isEmpty()) {
            sql.append("AND sr.requestType = ? ");
            params.add(requestType);
        }

        if (priority != null
                && !priority.trim().isEmpty()) {
            sql.append("AND sr.priority = ? ");
            params.add(priority);
        }

        // Customer sees only own requests
        if (customerId != null) {
            sql.append("AND sr.customerId = ? ");
            params.add(customerId);
        }

        sql.append("ORDER BY ");
        sql.append("FIELD(sr.priority, ");
        sql.append("'URGENT','HIGH','MEDIUM','LOW'), ");
        sql.append("sr.createdAt DESC ");

        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql.toString())) {

            System.out.println(
                "[ServiceRequestDAO] SQL: "
                + sql);
            System.out.println(
                "[ServiceRequestDAO] params: "
                + params);

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(
                        i + 1, (String) param);
                } else if (param instanceof Integer) {
                    ps.setInt(
                        i + 1, (Integer) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.findWithFilters: "
                + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(
                "ServiceRequestDAO.findWithFilters"
                + " unexpected: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Count service requests with filters
     */
    public int countWithFilters(
            String search, String status,
            String requestType, String priority,
            Integer customerId) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM service_requests sr "
            + "JOIN users c ON sr.customerId = c.id "
            + "WHERE sr.isDeleted = 0 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(
                "AND (sr.requestCode LIKE ? " +
                "OR sr.subject LIKE ? " +
                "OR c.fullName LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND sr.status = ? ");
            params.add(status);
        }

        if (requestType != null
                && !requestType.trim().isEmpty()) {
            sql.append("AND sr.requestType = ? ");
            params.add(requestType);
        }

        if (priority != null
                && !priority.trim().isEmpty()) {
            sql.append("AND sr.priority = ? ");
            params.add(priority);
        }

        if (customerId != null) {
            sql.append("AND sr.customerId = ? ");
            params.add(customerId);
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(
                        i + 1, (String) param);
                } else if (param instanceof Integer) {
                    ps.setInt(
                        i + 1, (Integer) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.countWithFilters: "
                + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Find service request by ID
     */
    public ServiceRequest findById(int id) {
        String sql = BASE_SELECT
            + "WHERE sr.id = ? AND sr.isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.findById: "
                + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create a new service request
     */
    public boolean create(ServiceRequest sr) {
        String code = generateNextCode();
        if (code == null) return false;

        String sql =
            "INSERT INTO service_requests " +
            "(requestCode, customerId, contractId, " +
            "deviceId, requestType, subject, " +
            "description, priority, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.setInt(2, sr.getCustomerId());
            ps.setObject(3, sr.getContractId());
            ps.setObject(4, sr.getDeviceId());
            ps.setString(5, sr.getRequestType());
            ps.setString(6, sr.getSubject());
            ps.setString(7, sr.getDescription());
            ps.setString(8, sr.getPriority());
            ps.setString(9,
                ServiceRequest.STATUS_OPEN);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.create: "
                + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update status, assignedTo, resolution
     * (Staff/Manager processing)
     */
    public boolean updateProcess(
            int id, String status,
            Integer assignedTo, String resolution,
            LocalDateTime resolvedAt) {
        String sql =
            "UPDATE service_requests SET " +
            "status = ?, assignedTo = ?, " +
            "resolution = ?, resolvedAt = ?, " +
            "updatedAt = NOW() " +
            "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setObject(2, assignedTo);
            ps.setString(3, resolution);

            if (resolvedAt != null) {
                ps.setTimestamp(4,
                    Timestamp.valueOf(resolvedAt));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.updateProcess: "
                + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Soft delete service request
     */
    public boolean softDelete(int id) {
        String sql =
            "UPDATE service_requests SET " +
            "isDeleted = 1, updatedAt = NOW() " +
            "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.softDelete: "
                + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Generate next request code: SR-0001, SR-0002...
     */
    public String generateNextCode() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 "
            + "FROM service_requests";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int nextId = rs.getInt(1);
                return String.format(
                    "SR-%04d", nextId);
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.generateNextCode: "
                + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get list of active Staff users
     * (for assignedTo dropdown)
     */
    public List<User> getActiveStaffList() {
        List<User> staffList = new ArrayList<>();

        String sql =
            "SELECT u.id, u.fullName, u.email " +
            "FROM users u " +
            "JOIN roles r ON u.roleId = r.id " +
            "WHERE r.name = 'STAFF' " +
            "AND u.status = 1 " +
            "AND u.isDeleted = 0 " +
            "ORDER BY u.fullName ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User staff = new User();
                staff.setId(rs.getInt("id"));
                staff.setFullName(
                    rs.getString("fullName"));
                staff.setEmail(
                    rs.getString("email"));
                staffList.add(staff);
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.getActiveStaffList: "
                + e.getMessage());
            e.printStackTrace();
        }

        return staffList;
    }

    /**
     * Get devices owned by a customer
     * (linked through contracts)
     */
    public List<String[]> getCustomerDevices(
            int customerId) {
        List<String[]> devices = new ArrayList<>();

        String sql =
            "SELECT DISTINCT d.id, d.serialNumber, " +
            "p.name AS productName " +
            "FROM devices d " +
            "JOIN products p ON d.productId = p.id " +
            "JOIN contract_details cd " +
            "    ON d.id = cd.deviceId " +
            "JOIN contracts ct " +
            "    ON cd.contractId = ct.id " +
            "WHERE ct.customerId = ? " +
            "AND d.isDeleted = 0 " +
            "ORDER BY p.name, d.serialNumber";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    devices.add(new String[]{
                        String.valueOf(
                            rs.getInt("id")),
                        rs.getString("serialNumber"),
                        rs.getString("productName")
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println(
                "ServiceRequestDAO.getCustomerDevices: "
                + e.getMessage());
            e.printStackTrace();
        }

        return devices;
    }

    /**
     * Map ResultSet to ServiceRequest object
     */
    private ServiceRequest mapResultSet(ResultSet rs)
            throws SQLException {
        ServiceRequest sr = new ServiceRequest();

        sr.setId(rs.getInt("id"));
        sr.setRequestCode(
            rs.getString("requestCode"));
        sr.setCustomerId(rs.getInt("customerId"));

        int contractId = rs.getInt("contractId");
        sr.setContractId(
            rs.wasNull() ? null : contractId);

        int deviceId = rs.getInt("deviceId");
        sr.setDeviceId(
            rs.wasNull() ? null : deviceId);

        sr.setRequestType(
            rs.getString("requestType"));
        sr.setSubject(rs.getString("subject"));
        sr.setDescription(
            rs.getString("description"));
        sr.setPriority(rs.getString("priority"));
        sr.setStatus(rs.getString("status"));

        int assignedTo = rs.getInt("assignedTo");
        sr.setAssignedTo(
            rs.wasNull() ? null : assignedTo);

        sr.setResolution(rs.getString("resolution"));

        Timestamp resolvedAt =
            rs.getTimestamp("resolvedAt");
        sr.setResolvedAt(resolvedAt != null
            ? resolvedAt.toLocalDateTime() : null);

        sr.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt =
            rs.getTimestamp("createdAt");
        sr.setCreatedAt(createdAt != null
            ? createdAt.toLocalDateTime() : null);

        Timestamp updatedAt =
            rs.getTimestamp("updatedAt");
        sr.setUpdatedAt(updatedAt != null
            ? updatedAt.toLocalDateTime() : null);

        // Joined fields
        try {
            sr.setCustomerName(
                rs.getString("customerName"));
            sr.setAssignedToName(
                rs.getString("assignedToName"));
            sr.setDeviceSerialNumber(
                rs.getString("deviceSerialNumber"));
        } catch (SQLException e) {
            // Columns may not exist in some queries
        }

        return sr;
    }
}
