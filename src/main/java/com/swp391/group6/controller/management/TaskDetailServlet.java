package com.swp391.group6.controller.management;

import com.swp391.group6.dao.TaskDAO;
import com.swp391.group6.model.Task;
import com.swp391.group6.model.User;
import com.swp391.group6.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "TaskDetailServlet", urlPatterns = {"/management/tasks/view"})
public class TaskDetailServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final AuthorizationService authService = new AuthorizationService();

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

        // Check access: Staff can only view their own tasks
        boolean isManager = "MANAGER".equals(currentUser.getRoleName());
        if (!isManager && task.getAssigneeId() != currentUser.getId()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen xem cong viec nay");
            request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                   .forward(request, response);
            return;
        }

        // Check permissions for action buttons
        boolean canUpdate = authService.hasPermission(
            currentUser.getId(), "TASK_UPDATE"
        );
        boolean canDelete = authService.hasPermission(
            currentUser.getId(), "TASK_DELETE"
        );

        request.setAttribute("task", task);
        request.setAttribute("canUpdate", canUpdate);
        request.setAttribute("canDelete", canDelete);
        request.setAttribute("isManager", isManager);

        request.getRequestDispatcher("/WEB-INF/management/task-detail.jsp")
               .forward(request, response);
    }
}
