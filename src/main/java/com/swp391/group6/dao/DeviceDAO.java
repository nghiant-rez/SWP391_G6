package com.swp391.group6.dao;

import com.swp391.group6.model.Device;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    /**
     * Get all active devices with product information
     */
    public List<Device> getAllActiveDevices() {
        List<Device> devices = new ArrayList<>();
        String sql = "SELECT d.*, p.name as productName " +
                    "FROM devices d " +
                    "LEFT JOIN products p ON d.productId = p.id " +
                    "WHERE d.isDeleted = 0 " +
                    "ORDER BY d.createdAt DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                devices.add(extractDeviceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    /**
     * Get device by ID
     */
    public Device getDeviceById(int id) {
        String sql = "SELECT d.*, p.name as productName " +
                    "FROM devices d " +
                    "LEFT JOIN products p ON d.productId = p.id " +
                    "WHERE d.id = ? AND d.isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractDeviceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Search devices by criteria
     */
    public List<Device> searchDevices(String keyword, Integer productId, String status) {
        List<Device> devices = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT d.*, p.name as productName " +
            "FROM devices d " +
            "LEFT JOIN products p ON d.productId = p.id " +
            "WHERE d.isDeleted = 0 "
        );

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (d.serialNumber LIKE ? OR p.name LIKE ?) ");
            String keywordPattern = "%" + keyword.trim() + "%";
            params.add(keywordPattern);
            params.add(keywordPattern);
        }

        if (productId != null) {
            sql.append("AND d.productId = ? ");
            params.add(productId);
        }

        if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
            sql.append("AND d.status = ? ");
            params.add(status);
        }

        sql.append("ORDER BY d.createdAt DESC");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    devices.add(extractDeviceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    /**
     * Get device count by status
     */
    public int getDeviceCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM devices WHERE status = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Extract Device object from ResultSet
     */
    private Device extractDeviceFromResultSet(ResultSet rs) throws SQLException {
        Device device = new Device();
        device.setId(rs.getInt("id"));
        device.setProductId(rs.getInt("productId"));
        device.setProductName(rs.getString("productName"));
        device.setSerialNumber(rs.getString("serialNumber"));
        device.setStatus(rs.getString("status"));
        device.setCondition(rs.getString("condition"));
        device.setCurrentLocation(rs.getString("currentLocation"));
        device.setNotes(rs.getString("notes"));
        device.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAtTs = rs.getTimestamp("createdAt");
        device.setCreatedAt(createdAtTs != null ? createdAtTs.toLocalDateTime() : null);
        Timestamp updatedAtTs = rs.getTimestamp("updatedAt");
        device.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);

        int createdBy = rs.getInt("createdBy");
        device.setCreatedBy(rs.wasNull() ? null : createdBy);

        return device;
    }
}
