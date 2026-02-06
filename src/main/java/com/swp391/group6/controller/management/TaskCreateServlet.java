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

@WebServlet(name = "TaskCreateServlet", urlPatterns = {"/management/tasks/create"})
public class TaskCreateServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Load staff list for dropdown
        List<User> staffList = taskDAO.getActiveStaffList();
        request.setAttribute("staffList", staffList);

        // Dropdown options
        request.setAttribute("taskTypes", Task.TASK_TYPES);
        request.setAttribute("priorities", Task.PRIORITIES);

        request.getRequestDispatcher("/WEB-INF/management/task-form.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        // Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String assigneeIdStr = request.getParameter("assigneeId");
        String taskType = request.getParameter("taskType");
        String priority = request.getParameter("priority");
        String dueDateStr = request.getParameter("dueDate");

        // Validation
        StringBuilder errors = new StringBuilder();

        // Title validation
        if (title == null || title.trim().isEmpty()) {
            errors.append("Tieu de khong duoc de trong. ");
        } else if (title.trim().length() > 200) {
            errors.append("Tieu de khong duoc vuot qua 200 ky tu. ");
        }

        // Assignee validation
        int assigneeId = 0;
        if (assigneeIdStr == null || assigneeIdStr.trim().isEmpty()) {
            errors.append("Vui long chon nguoi thuc hien. ");
        } else {
            try {
                assigneeId = Integer.parseInt(assigneeIdStr);
            } catch (NumberFormatException e) {
                errors.append("Nguoi thuc hien khong hop le. ");
            }
        }

        // Task type validation
        if (taskType == null || taskType.trim().isEmpty()) {
            errors.append("Vui long chon loai cong viec. ");
        }

        // Priority validation
        if (priority == null || priority.trim().isEmpty()) {
            priority = Task.PRIORITY_MEDIUM;
        }

        // Due date parsing
        LocalDateTime dueDate = null;
        if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                dueDate = LocalDateTime.parse(dueDateStr, formatter);
            } catch (DateTimeParseException e) {
                errors.append("Dinh dang ngay khong hop le. ");
            }
        }

        // Description length check
        if (description != null && description.length() > 1000) {
            errors.append("Mo ta khong duoc vuot qua 1000 ky tu. ");
        }

        // If validation errors, return to form
        if (errors.length() > 0) {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setAssigneeId(assigneeId);
            task.setTaskType(taskType);
            task.setPriority(priority);
            task.setDueDate(dueDate);

            request.setAttribute("task", task);
            request.setAttribute("error", errors.toString());
            request.setAttribute("staffList", taskDAO.getActiveStaffList());
            request.setAttribute("taskTypes", Task.TASK_TYPES);
            request.setAttribute("priorities", Task.PRIORITIES);

            request.getRequestDispatcher("/WEB-INF/management/task-form.jsp")
                   .forward(request, response);
            return;
        }

        // Create task
        Task task = new Task();
        task.setTitle(title.trim());
        task.setDescription(description);
        task.setAssignerId(currentUser.getId());
        task.setAssigneeId(assigneeId);
        task.setTaskType(taskType);
        task.setPriority(priority);
        task.setStatus(Task.STATUS_TODO);
        task.setDueDate(dueDate);

        boolean success = taskDAO.create(task);

        if (success) {
            String message = URLEncoder.encode(
                "Tao cong viec thanh cong", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?message=" + message);
        } else {
            request.setAttribute("task", task);
            request.setAttribute("error", "Khong the tao cong viec. " +
                                          "Vui long thu lai.");
            request.setAttribute("staffList", taskDAO.getActiveStaffList());
            request.setAttribute("taskTypes", Task.TASK_TYPES);
            request.setAttribute("priorities", Task.PRIORITIES);

            request.getRequestDispatcher("/WEB-INF/management/task-form.jsp")
                   .forward(request, response);
        }
    }
}
