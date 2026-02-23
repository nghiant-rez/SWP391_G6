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

@WebServlet(name = "TaskDeleteServlet", urlPatterns = {"/management/tasks/delete"})
public class TaskDeleteServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final AuthorizationService authService = new AuthorizationService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        // Double-check permission (AuthorizationFilter should catch this,
        // but defense in depth)
        if (!authService.hasPermission(currentUser.getId(), "TASK_DELETE")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage",
                "Ban khong co quyen xoa cong viec");
            request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                   .forward(request, response);
            return;
        }

        // Get task ID
        String taskIdStr = request.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            String error = URLEncoder.encode(
                "Khong tim thay cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(taskIdStr);
        } catch (NumberFormatException e) {
            String error = URLEncoder.encode(
                "ID cong viec khong hop le", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Verify task exists
        Task task = taskDAO.findById(taskId);
        if (task == null) {
            String error = URLEncoder.encode(
                "Khong tim thay cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
            return;
        }

        // Soft delete
        boolean success = taskDAO.softDelete(taskId);

        if (success) {
            String message = URLEncoder.encode(
                "Xoa cong viec thanh cong", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?message=" + message);
        } else {
            String error = URLEncoder.encode(
                "Khong the xoa cong viec", StandardCharsets.UTF_8
            );
            response.sendRedirect(request.getContextPath() +
                                  "/management/tasks?error=" + error);
        }
    }
}
