package com.swp391.group6.dao;

import com.swp391.group6.model.Task;
import com.swp391.group6.model.User;
import com.swp391.group6.util.DBContext;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private static final String BASE_SELECT =
        "SELECT t.*, " +
        "assigner.fullName AS assignerName, " +
        "assignee.fullName AS assigneeName " +
        "FROM tasks t " +
        "JOIN users assigner ON t.assignerId = assigner.id " +
        "JOIN users assignee ON t.assigneeId = assignee.id ";

    /**
     * Get tasks with filters and pagination
     * Dynamic query building to avoid ? IS NULL trap
     */
    public List<Task> findWithFilters(String search, String status,
                                       String priority, String taskType,
                                       Integer assigneeId, int page,
                                       int pageSize) {
        List<Task> tasks = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        sql.append("WHERE t.isDeleted = 0 ");

        // Search filter
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (t.title LIKE ? OR assignee.fullName LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
        }

        // Status filter
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND t.status = ? ");
            params.add(status);
        }

        // Priority filter
        if (priority != null && !priority.trim().isEmpty()) {
            sql.append("AND t.priority = ? ");
            params.add(priority);
        }

        // Task type filter
        if (taskType != null && !taskType.trim().isEmpty()) {
            sql.append("AND t.taskType = ? ");
            params.add(taskType);
        }

        // Assignee filter (for Staff - only see their tasks)
        if (assigneeId != null) {
            sql.append("AND t.assigneeId = ? ");
            params.add(assigneeId);
        }

        // Order: due date ASC (nulls last), then priority DESC
        sql.append("ORDER BY ");
        sql.append("CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END, ");
        sql.append("t.dueDate ASC, ");
        sql.append("FIELD(t.priority, 'URGENT', 'HIGH', 'MEDIUM', 'LOW') ");

        // Pagination
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters dynamically
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
                    tasks.add(mapResultSetToTask(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("TaskDAO.findWithFilters failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Count tasks with filters (for pagination)
     */
    public int countWithFilters(String search, String status,
                                 String priority, String taskType,
                                 Integer assigneeId) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM tasks t " +
            "JOIN users assignee ON t.assigneeId = assignee.id " +
            "WHERE t.isDeleted = 0 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (t.title LIKE ? OR assignee.fullName LIKE ?) ");
            String keyword = "%" + search.trim() + "%";
            params.add(keyword);
            params.add(keyword);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND t.status = ? ");
            params.add(status);
        }

        if (priority != null && !priority.trim().isEmpty()) {
            sql.append("AND t.priority = ? ");
            params.add(priority);
        }

        if (taskType != null && !taskType.trim().isEmpty()) {
            sql.append("AND t.taskType = ? ");
            params.add(taskType);
        }

        if (assigneeId != null) {
            sql.append("AND t.assigneeId = ? ");
            params.add(assigneeId);
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
            System.err.println("TaskDAO.countWithFilters failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Find task by ID
     */
    public Task findById(int id) {
        String sql = BASE_SELECT + "WHERE t.id = ? AND t.isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("TaskDAO.findById failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create a new task
     */
    public boolean create(Task task) {
        String sql = "INSERT INTO tasks (title, description, assignerId, " +
                     "assigneeId, relatedContractId, relatedCustomerId, " +
                     "taskType, status, priority, dueDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getAssignerId());
            ps.setInt(4, task.getAssigneeId());
            ps.setObject(5, task.getRelatedContractId());
            ps.setObject(6, task.getRelatedCustomerId());
            ps.setString(7, task.getTaskType());
            ps.setString(8, task.getStatus());
            ps.setString(9, task.getPriority());

            if (task.getDueDate() != null) {
                ps.setTimestamp(10, Timestamp.valueOf(task.getDueDate()));
            } else {
                ps.setNull(10, Types.TIMESTAMP);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("TaskDAO.create failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update task (Manager - full update)
     */
    public boolean update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, " +
                     "assigneeId = ?, taskType = ?, status = ?, " +
                     "priority = ?, dueDate = ?, completedAt = ?, " +
                     "completionNotes = ?, updatedAt = NOW() " +
                     "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getAssigneeId());
            ps.setString(4, task.getTaskType());
            ps.setString(5, task.getStatus());
            ps.setString(6, task.getPriority());

            if (task.getDueDate() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(task.getDueDate()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }

            if (task.getCompletedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(task.getCompletedAt()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            ps.setString(9, task.getCompletionNotes());
            ps.setInt(10, task.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("TaskDAO.update failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update task status and notes only (Staff - limited update)
     */
    public boolean updateStatusAndNotes(int taskId, String status,
                                         String completionNotes,
                                         LocalDateTime completedAt) {
        String sql = "UPDATE tasks SET status = ?, completionNotes = ?, " +
                     "completedAt = ?, updatedAt = NOW() " +
                     "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, completionNotes);

            if (completedAt != null) {
                ps.setTimestamp(3, Timestamp.valueOf(completedAt));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setInt(4, taskId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("TaskDAO.updateStatusAndNotes failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Soft delete task
     */
    public boolean softDelete(int taskId) {
        String sql = "UPDATE tasks SET isDeleted = 1, updatedAt = NOW() " +
                     "WHERE id = ? AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("TaskDAO.softDelete failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get list of active Staff users (for assignee dropdown)
     */
    public List<User> getActiveStaffList() {
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
            System.err.println("TaskDAO.getActiveStaffList failed: " +
                               e.getMessage());
            e.printStackTrace();
        }

        return staffList;
    }

    /**
     * Check if user is assignee of a task
     */
    public boolean isAssignee(int taskId, int userId) {
        String sql = "SELECT 1 FROM tasks WHERE id = ? AND assigneeId = ? " +
                     "AND isDeleted = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("TaskDAO.isAssignee failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Map ResultSet to Task object
     */
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();

        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setAssignerId(rs.getInt("assignerId"));
        task.setAssigneeId(rs.getInt("assigneeId"));

        int relatedContractId = rs.getInt("relatedContractId");
        task.setRelatedContractId(rs.wasNull() ? null : relatedContractId);

        int relatedCustomerId = rs.getInt("relatedCustomerId");
        task.setRelatedCustomerId(rs.wasNull() ? null : relatedCustomerId);

        task.setTaskType(rs.getString("taskType"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));

        Timestamp dueDate = rs.getTimestamp("dueDate");
        task.setDueDate(dueDate != null ? dueDate.toLocalDateTime() : null);

        Timestamp completedAt = rs.getTimestamp("completedAt");
        task.setCompletedAt(completedAt != null ?
                           completedAt.toLocalDateTime() : null);

        task.setCompletionNotes(rs.getString("completionNotes"));
        task.setDeleted(rs.getBoolean("isDeleted"));

        Timestamp createdAt = rs.getTimestamp("createdAt");
        task.setCreatedAt(createdAt != null ?
                         createdAt.toLocalDateTime() : null);

        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        task.setUpdatedAt(updatedAt != null ?
                         updatedAt.toLocalDateTime() : null);

        // Joined fields
        try {
            task.setAssignerName(rs.getString("assignerName"));
            task.setAssigneeName(rs.getString("assigneeName"));
        } catch (SQLException e) {
            // Columns may not exist in some queries
        }

        return task;
    }
}
