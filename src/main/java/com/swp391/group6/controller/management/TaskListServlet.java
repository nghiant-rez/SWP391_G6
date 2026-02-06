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
import java.util.List;

@WebServlet(name = "TaskListServlet", urlPatterns = {"/management/tasks"})
public class TaskListServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final AuthorizationService authService = new AuthorizationService();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        // Get filter parameters
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");
        String taskType = request.getParameter("taskType");

        // Pagination
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Determine assigneeId filter based on role
        // Staff can only see their own tasks
        Integer assigneeIdFilter = null;
        boolean isManager = "MANAGER".equals(currentUser.getRoleName());

        if (!isManager) {
            assigneeIdFilter = currentUser.getId();
        }

        // Fetch tasks with filters
        List<Task> tasks = taskDAO.findWithFilters(
            search, status, priority, taskType,
            assigneeIdFilter, currentPage, PAGE_SIZE
        );

        int totalTasks = taskDAO.countWithFilters(
            search, status, priority, taskType, assigneeIdFilter
        );

        int totalPages = (int) Math.ceil((double) totalTasks / PAGE_SIZE);

        // Check permissions for UI buttons
        boolean canCreate = authService.hasPermission(
            currentUser.getId(), "TASK_CREATE"
        );
        boolean canUpdate = authService.hasPermission(
            currentUser.getId(), "TASK_UPDATE"
        );
        boolean canDelete = authService.hasPermission(
            currentUser.getId(), "TASK_DELETE"
        );

        // Set attributes for JSP
        request.setAttribute("tasks", tasks);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalTasks", totalTasks);
        request.setAttribute("canCreate", canCreate);
        request.setAttribute("canUpdate", canUpdate);
        request.setAttribute("canDelete", canDelete);
        request.setAttribute("isManager", isManager);

        // Preserve filter values
        request.setAttribute("searchValue", search);
        request.setAttribute("statusValue", status);
        request.setAttribute("priorityValue", priority);
        request.setAttribute("taskTypeValue", taskType);

        // Dropdown options
        request.setAttribute("statuses", Task.STATUSES);
        request.setAttribute("priorities", Task.PRIORITIES);
        request.setAttribute("taskTypes", Task.TASK_TYPES);

        request.getRequestDispatcher("/WEB-INF/management/task-list.jsp")
               .forward(request, response);
    }
}
