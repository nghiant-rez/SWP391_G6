package com.swp391.group6.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PasswordResetRequest implements Serializable {
    private int id;
    private int userId;
    private String email;
    private String status;
    private String newPassword;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private Integer processedBy;
    private String reason;

    // User info(để hiển thị)
    private String userFullName;
    private String adminName;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(int userId, String email) {
        this.userId = userId;
        this.email = email;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }

    public Integer getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "PassResetRequests{" +
                "id=" + id +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", requestDate=" + requestDate +
                ", processedDate=" + processedDate +
                ", processedBy=" + processedBy +
                ", reason='" + reason + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
