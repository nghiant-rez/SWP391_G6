package com.swp391.group6.controller.management;

import com.swp391.group6.dao.TaskDAO;
import com.swp391.group6.model.Task;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "TaskEditServlet", urlPatterns = {"/management/tasks/edit"})
public class TaskEditServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        // Get task ID
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            String error = URLEncoder.encode(
                "Khong tim thay cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            String error = URLEncoder.encode(
                "ID cong viec khong hop le", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Fetch task
        Task task = taskDAO.findById(taskId);
        if (task == null) {
            String error = URLEncoder.encode(
                "Khong tim thay cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Check access: Staff can only edit their own tasks
        boolean isManager = "MANAGER".equals(currentUser.getRoleName());
        if (!isManager && task.getAssigneeId() != currentUser.getId()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen chinh sua cong viec nay");
            request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                   .forward(request, response);
            return;
        }

        // Get allowed statuses for Staff
        String[] allowedStatuses = getAllowedStatusesForStaff(task.getStatus());

        request.setAttribute("task", task);
        request.setAttribute("isManager", isManager);
        request.setAttribute("staffList", taskDAO.getActiveStaffList());
        request.setAttribute("taskTypes", Task.TASK_TYPES);
        request.setAttribute("priorities", Task.PRIORITIES);
        request.setAttribute("statuses", Task.STATUSES);
        request.setAttribute("allowedStatuses", allowedStatuses);

        request.getRequestDispatcher("/WEB-INF/management/task-edit.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        boolean isManager = "MANAGER".equals(currentUser.getRoleName());

        // Get task ID
        String idParam = request.getParameter("id");
        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            String error = URLEncoder.encode(
                "ID cong viec khong hop le", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Fetch existing task
        Task existingTask = taskDAO.findById(taskId);
        if (existingTask == null) {
            String error = URLEncoder.encode(
                "Khong tim thay cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Check access
        if (!isManager &&
            existingTask.getAssigneeId() != currentUser.getId()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen chinh sua cong viec nay");
            request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                   .forward(request, response);
            return;
        }

        // Get form parameters
        String newStatus = request.getParameter("status");
        String completionNotes = request.getParameter("completionNotes");

        StringBuilder errors = new StringBuilder();

        // Validate status transition for Staff
        if (!isManager) {
            if (!Task.isValidStaffTransition(existingTask.getStatus(),
                                              newStatus)) {
                errors.append("Khong the chuyen trang thai tu '")
                      .append(existingTask.getStatusDisplay())
                      .append("' sang '")
                      .append(getStatusDisplay(newStatus))
                      .append("'. ");
            }
        }

        // Completion notes required when status is DONE
        if (Task.STATUS_DONE.equals(newStatus) &&
            (completionNotes == null || completionNotes.trim().isEmpty())) {
            errors.append("Ghi chu hoan thanh la bat buoc khi " +
                          "danh dau hoan thanh. ");
        }

        // Set completedAt when status changes to DONE
        LocalDateTime completedAt = existingTask.getCompletedAt();
        if (Task.STATUS_DONE.equals(newStatus) &&
            !Task.STATUS_DONE.equals(existingTask.getStatus())) {
            completedAt = LocalDateTime.now();
        }

        // SECURITY: Server-side role-based field filtering
        if (isManager) {
            // Manager can update all fields
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String assigneeIdStr = request.getParameter("assigneeId");
            String taskType = request.getParameter("taskType");
            String priority = request.getParameter("priority");
            String dueDateStr = request.getParameter("dueDate");

            // Validate Manager fields
            if (title == null || title.trim().isEmpty()) {
                errors.append("Tieu de khong duoc de trong. ");
            } else if (title.trim().length() > 200) {
                errors.append("Tieu de khong duoc vuot qua 200 ky tu. ");
            }

            int assigneeId = existingTask.getAssigneeId();
            if (assigneeIdStr != null && !assigneeIdStr.trim().isEmpty()) {
                try {
                    assigneeId = Integer.parseInt(assigneeIdStr);
                } catch (NumberFormatException e) {
                    errors.append("Nguoi thuc hien khong hop le. ");
                }
            }

            LocalDateTime dueDate = existingTask.getDueDate();
            if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    dueDate = LocalDateTime.parse(dueDateStr, formatter);
                } catch (DateTimeParseException e) {
                    errors.append("Dinh dang ngay khong hop le. ");
                }
            } else {
                dueDate = null;
            }

            // If errors, return to form
            if (errors.length() > 0) {
                existingTask.setTitle(title);
                existingTask.setDescription(description);
                existingTask.setAssigneeId(assigneeId);
                existingTask.setTaskType(taskType);
                existingTask.setPriority(priority);
                existingTask.setStatus(newStatus);
                existingTask.setDueDate(dueDate);
                existingTask.setCompletionNotes(completionNotes);

                returnToFormWithError(request, response, existingTask,
                                       errors.toString(), isManager);
                return;
            }

            // Update all fields for Manager
            existingTask.setTitle(title.trim());
            existingTask.setDescription(description);
            existingTask.setAssigneeId(assigneeId);
            existingTask.setTaskType(taskType);
            existingTask.setPriority(priority);
            existingTask.setStatus(newStatus);
            existingTask.setDueDate(dueDate);
            existingTask.setCompletedAt(completedAt);
            existingTask.setCompletionNotes(completionNotes);

            boolean success = taskDAO.update(existingTask);
            handleUpdateResult(request, response, taskId, success,
                               existingTask, isManager);

        } else {
            // Staff can only update status and completion notes
            if (errors.length() > 0) {
                existingTask.setStatus(newStatus);
                existingTask.setCompletionNotes(completionNotes);

                returnToFormWithError(request, response, existingTask,
                                       errors.toString(), isManager);
                return;
            }

            boolean success = taskDAO.updateStatusAndNotes(
                taskId, newStatus, completionNotes, completedAt
            );
            handleUpdateResult(request, response, taskId, success,
                               existingTask, isManager);
        }
    }

    private void returnToFormWithError(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Task task, String error,
                                        boolean isManager)
            throws ServletException, IOException {

        String[] allowedStatuses = getAllowedStatusesForStaff(task.getStatus());

        request.setAttribute("task", task);
        request.setAttribute("error", error);
        request.setAttribute("isManager", isManager);
        request.setAttribute("staffList", taskDAO.getActiveStaffList());
        request.setAttribute("taskTypes", Task.TASK_TYPES);
        request.setAttribute("priorities", Task.PRIORITIES);
        request.setAttribute("statuses", Task.STATUSES);
        request.setAttribute("allowedStatuses", allowedStatuses);

        request.getRequestDispatcher("/WEB-INF/management/task-edit.jsp")
               .forward(request, response);
    }

    private void handleUpdateResult(HttpServletRequest request,
                                     HttpServletResponse response,
                                     int taskId, boolean success,
                                     Task task, boolean isManager)
            throws ServletException, IOException {

        if (success) {
            String message = URLEncoder.encode(
                "Cap nhat cong viec thanh cong", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                "/management/tasks/view?id=" + taskId + "&message=" + message);
        } else {
            returnToFormWithError(request, response, task,
                "Khong the cap nhat cong viec. Vui long thu lai.", isManager);
        }
    }

    private String[] getAllowedStatusesForStaff(String currentStatus) {
        return switch (currentStatus) {
            case Task.STATUS_TODO ->
                new String[]{Task.STATUS_TODO, Task.STATUS_IN_PROGRESS};
            case Task.STATUS_IN_PROGRESS ->
                new String[]{Task.STATUS_IN_PROGRESS, Task.STATUS_DONE};
            case Task.STATUS_DONE ->
                new String[]{Task.STATUS_DONE};
            case Task.STATUS_CANCELLED ->
                new String[]{Task.STATUS_CANCELLED};
            default ->
                new String[]{currentStatus};
        };
    }

    private String getStatusDisplay(String status) {
        return switch (status) {
            case Task.STATUS_TODO -> "Chua lam";
            case Task.STATUS_IN_PROGRESS -> "Dang thuc hien";
            case Task.STATUS_DONE -> "Hoan thanh";
            case Task.STATUS_CANCELLED -> "Da huy";
            default -> status;
        };
    }
}
