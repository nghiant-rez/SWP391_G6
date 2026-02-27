package com.swp391.group6.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_DRAFT = "DRAFT";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_COMPLETED= "COMPLETED";

    public static final String[] STATUS = {
            STATUS_DRAFT, STATUS_PENDING, STATUS_APPROVED,
            STATUS_REJECTED, STATUS_COMPLETED
    };

    private int id;
    private String contractCode;
    private int customerId;
    private int staffId;
    private Integer managerId;

    private String title;
    private BigDecimal totalAmount;
    private Date saleDate;

    private String status;
    private String rejectionReason;
    private Timestamp approvedAt;

    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerName;
    private String staffName;
    private String managerName;

    public Contract() {
    }

    public Contract(int id, String contractCode, int customerId, int staffId, Integer managerId, String title, BigDecimal totalAmount, Date saleDate, String status, String rejectionReason, Timestamp approvedAt, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, String customerName, String staffName, String managerName) {
        this.id = id;
        this.contractCode = contractCode;
        this.customerId = customerId;
        this.staffId = staffId;
        this.managerId = managerId;
        this.title = title;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.approvedAt = approvedAt;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerName = customerName;
        this.staffName = staffName;
        this.managerName = managerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Timestamp getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
