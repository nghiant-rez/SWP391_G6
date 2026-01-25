package com.swp391.group6.model;

import java.time.LocalDateTime;

public class PassResetRequests {
    private int id;
    private int userId;
    private String email;
    private String status;
    private String newPassword;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;

    public PassResetRequests(int id, int userId, String email, String status, String newPassword, LocalDateTime requestedAt, LocalDateTime processedAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.status = status;
        this.newPassword = newPassword;
        this.requestedAt = requestedAt;
        this.processedAt = processedAt;
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

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return "PassResetRequests{" +
                "id=" + id +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", requestedAt=" + requestedAt +
                ", processedAt=" + processedAt +
                '}';
    }
}
