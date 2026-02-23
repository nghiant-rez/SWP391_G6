package com.swp391.group6.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    // Task Type Enum values
    public static final String TYPE_FOLLOW_UP = "FOLLOW_UP";
    public static final String TYPE_SITE_VISIT = "SITE_VISIT";
    public static final String TYPE_DELIVERY = "DELIVERY";
    public static final String TYPE_INSTALLATION = "INSTALLATION";
    public static final String TYPE_MAINTENANCE = "MAINTENANCE";
    public static final String TYPE_OTHER = "OTHER";

    // Status Enum values
    public static final String STATUS_TODO = "TODO";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_DONE = "DONE";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Priority Enum values
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";

    // Arrays for dropdowns
    public static final String[] TASK_TYPES = {
        TYPE_FOLLOW_UP, TYPE_SITE_VISIT, TYPE_DELIVERY,
        TYPE_INSTALLATION, TYPE_MAINTENANCE, TYPE_OTHER
    };

    public static final String[] STATUSES = {
        STATUS_TODO, STATUS_IN_PROGRESS, STATUS_DONE, STATUS_CANCELLED
    };

    public static final String[] PRIORITIES = {
        PRIORITY_LOW, PRIORITY_MEDIUM, PRIORITY_HIGH, PRIORITY_URGENT
    };

    // Fields matching database schema
    private int id;
    private String title;
    private String description;
    private int assignerId;
    private int assigneeId;
    private Integer relatedContractId;
    private Integer relatedCustomerId;
    private String taskType;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private String completionNotes;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Joined fields (from users table)
    private String assignerName;
    private String assigneeName;

    // Constructors
    public Task() {
        this.taskType = TYPE_OTHER;
        this.status = STATUS_TODO;
        this.priority = PRIORITY_MEDIUM;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(int assignerId) {
        this.assignerId = assignerId;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Integer getRelatedContractId() {
        return relatedContractId;
    }

    public void setRelatedContractId(Integer relatedContractId) {
        this.relatedContractId = relatedContractId;
    }

    public Integer getRelatedCustomerId() {
        return relatedCustomerId;
    }

    public void setRelatedCustomerId(Integer relatedCustomerId) {
        this.relatedCustomerId = relatedCustomerId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
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

    public String getAssignerName() {
        return assignerName;
    }

    public void setAssignerName(String assignerName) {
        this.assignerName = assignerName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    // Helper methods
    public boolean isOverdue() {
        if (dueDate == null) {
            return false;
        }
        if (STATUS_DONE.equals(status) || STATUS_CANCELLED.equals(status)) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    public String getTaskTypeDisplay() {
        return switch (taskType) {
            case TYPE_FOLLOW_UP -> "Theo doi";
            case TYPE_SITE_VISIT -> "Kham sat";
            case TYPE_DELIVERY -> "Giao hang";
            case TYPE_INSTALLATION -> "Lap dat";
            case TYPE_MAINTENANCE -> "Bao tri";
            default -> "Khac";
        };
    }

    public String getStatusDisplay() {
        return switch (status) {
            case STATUS_TODO -> "Chua lam";
            case STATUS_IN_PROGRESS -> "Dang thuc hien";
            case STATUS_DONE -> "Hoan thanh";
            case STATUS_CANCELLED -> "Da huy";
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
     * Validate staff status transition
     * Staff can only: TODO -> IN_PROGRESS -> DONE
     */
    public static boolean isValidStaffTransition(String oldStatus,
                                                  String newStatus) {
        if (oldStatus.equals(newStatus)) {
            return true;
        }
        return switch (oldStatus) {
            case STATUS_TODO -> STATUS_IN_PROGRESS.equals(newStatus);
            case STATUS_IN_PROGRESS -> STATUS_DONE.equals(newStatus);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", assigneeId=" + assigneeId +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
