package com.swp391.group6.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    // Request Type Enum values
    public static final String TYPE_REPAIR = "REPAIR";
    public static final String TYPE_MAINTENANCE = "MAINTENANCE";
    public static final String TYPE_COMPLAINT = "COMPLAINT";
    public static final String TYPE_INQUIRY = "INQUIRY";
    public static final String TYPE_WARRANTY = "WARRANTY";
    public static final String TYPE_OTHER = "OTHER";

    // Status Enum values
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_RESOLVED = "RESOLVED";
    public static final String STATUS_CLOSED = "CLOSED";

    // Priority Enum values
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";

    // Arrays for dropdowns
    public static final String[] REQUEST_TYPES = {
        TYPE_REPAIR, TYPE_MAINTENANCE, TYPE_COMPLAINT,
        TYPE_INQUIRY, TYPE_WARRANTY, TYPE_OTHER
    };

    public static final String[] STATUSES = {
        STATUS_OPEN, STATUS_IN_PROGRESS,
        STATUS_RESOLVED, STATUS_CLOSED
    };

    public static final String[] PRIORITIES = {
        PRIORITY_LOW, PRIORITY_MEDIUM,
        PRIORITY_HIGH, PRIORITY_URGENT
    };

    // Fields matching database schema
    private int id;
    private String requestCode;
    private int customerId;
    private Integer contractId;
    private Integer deviceId;
    private String requestType;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private Integer assignedTo;
    private String resolution;
    private LocalDateTime resolvedAt;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Joined fields (from users table)
    private String customerName;
    private String assignedToName;
    // Joined field (from devices table)
    private String deviceSerialNumber;

    // Constructors
    public ServiceRequest() {
        this.requestType = TYPE_INQUIRY;
        this.status = STATUS_OPEN;
        this.priority = PRIORITY_MEDIUM;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(
            String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    // Date formatting for JSP display
    private static final DateTimeFormatter
        VN_FORMAT = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy HH:mm");

    public String getCreatedAtFormatted() {
        return createdAt != null
            ? createdAt.format(VN_FORMAT) : null;
    }

    public String getUpdatedAtFormatted() {
        return updatedAt != null
            ? updatedAt.format(VN_FORMAT) : null;
    }

    public String getResolvedAtFormatted() {
        return resolvedAt != null
            ? resolvedAt.format(VN_FORMAT) : null;
    }

    // Helper methods
    public String getRequestTypeDisplay() {
        return switch (requestType) {
            case TYPE_REPAIR -> "Sua chua";
            case TYPE_MAINTENANCE -> "Bao tri";
            case TYPE_COMPLAINT -> "Khieu nai";
            case TYPE_INQUIRY -> "Yeu cau thong tin";
            case TYPE_WARRANTY -> "Bao hanh";
            default -> "Khac";
        };
    }

    public String getStatusDisplay() {
        return switch (status) {
            case STATUS_OPEN -> "Mo";
            case STATUS_IN_PROGRESS -> "Dang xu ly";
            case STATUS_RESOLVED -> "Da giai quyet";
            case STATUS_CLOSED -> "Da dong";
            default -> status;
        };
    }

    public String getPriorityDisplay() {
        return switch (priority) {
            case PRIORITY_LOW -> "Thap";
            case PRIORITY_MEDIUM -> "Trung binh";
            case PRIORITY_HIGH -> "Cao";
            case PRIORITY_URGENT -> "Khan cap";
            default -> priority;
        };
    }

    /**
     * Validate status transition
     * OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED
     */
    public static boolean isValidTransition(
            String oldStatus, String newStatus) {
        if (oldStatus.equals(newStatus)) {
            return true;
        }
        return switch (oldStatus) {
            case STATUS_OPEN ->
                STATUS_IN_PROGRESS.equals(newStatus);
            case STATUS_IN_PROGRESS ->
                STATUS_RESOLVED.equals(newStatus);
            case STATUS_RESOLVED ->
                STATUS_CLOSED.equals(newStatus);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
            "id=" + id +
            ", requestCode='" + requestCode + '\'' +
            ", customerId=" + customerId +
            ", status='" + status + '\'' +
            ", priority='" + priority + '\'' +
            '}';
    }
}
