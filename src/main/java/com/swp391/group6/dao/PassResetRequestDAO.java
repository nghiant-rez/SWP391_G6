package com.swp391.group6.dao;

import com.swp391.group6.model.PasswordResetRequest;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassResetRequestDAO {
    // create request reset new password
    public boolean createRequest(int userId, String email){
        String sql = "INSERT INTO `password_reset_requests` (`userId`, `email`, `status`) VALUES (?,?,'PENDING')";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, userId);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            System.err.println("Error creating password reset request: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    //Take all requests PENDING (for admin)
    public List<PasswordResetRequest> getPendingRequests(){
        List<PasswordResetRequest> requests = new ArrayList<>();
        String sql = "SELECT prr.*, u.fullName as userFullName " +
                     "FROM `password_reset_requests` prr " +
                     "JOIN `users` u ON prr.userId = u.id " +
                     "WHERE prr.status='PENDING' " +
                     "ORDER BY prr.requestDate DESC";

        try (Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                requests.add(mapResultSetToRequest(rs));
            }
        }catch (SQLException e){
            System.err.println("Error getting pending requests: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    //Approve request - update new password

    public boolean approveRequest(int requestId, String hashedPassword, int adminId){
        String sql = "UPDATE `password_reset_requests` " +
                     "SET `status` = 'APPROVED', `newPassword` = ?, " +
                     "`processedBy` = ?, `processedDate` = NOW() " +
                     "WHERE `id` = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, hashedPassword);
            ps.setInt(2, adminId);
            ps.setInt(3, requestId);

            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            System.err.println("Error approving request: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //Reject request
    public boolean rejectRequest(int requestId, String reason, int adminId){
        String sql = "UPDATE `password_reset_requests` " +
                "SET `status` = 'REJECTED', `reason` = ?, " +
                "`processedBy` = ?, `processedDate` = NOW() " +
                "WHERE `id` = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, reason);
            ps.setInt(2, adminId);
            ps.setInt(3, requestId);

            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            System.err.println("Error rejecting request: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //Update user password int users table
    public boolean updateUserPassword(int userId, String hashedPassword){
        String sql = "UPDATE `users` SET `password` = ? WHERE `id` = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        }catch (SQLException e){
            System.err.println("Error rejecting request: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //take request to id
    public PasswordResetRequest getRequestById(int requestId){
        String sql = "SELECT prr.*, u.fullName as userFullName " +
                     "FROM `password_reset_requests` prr " +
                     "JOIN `users` u ON prr.userId = u.id " +
                     "WHERE prr.id = ?";

        try (Connection conn = DBContext.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, requestId);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapResultSetToRequest(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting request by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //Map ResultSet to PasswordResetRequest
    private PasswordResetRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        PasswordResetRequest request = new PasswordResetRequest();

        request.setId(rs.getInt("id"));
        request.setUserId(rs.getInt("userId"));
        request.setEmail(rs.getString("email"));
        request.setStatus(rs.getString("status"));
        request.setNewPassword(rs.getString("newPassword"));

        Timestamp requestDate = rs.getTimestamp("requestDate");
        if(requestDate != null){
            request.setRequestDate(requestDate.toLocalDateTime());
        }

        Timestamp processedDate = rs.getTimestamp("processedDate");
        if(processedDate != null){
            request.setProcessedDate(processedDate.toLocalDateTime());
        }

        Integer processedBy = rs.getObject("processedBy", Integer.class);
        request.setProcessedBy(processedBy);

        request.setReason(rs.getString("reason"));
        request.setUserFullName(rs.getString("userFullName"));

        return request;
    }
}
