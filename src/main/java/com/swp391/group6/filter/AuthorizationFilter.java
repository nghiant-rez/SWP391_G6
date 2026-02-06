package com.swp391.group6.filter;

import com.swp391.group6.model.User;
import com.swp391.group6.service.AuthorizationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralized Authorization Filter
 * Checks permissions for protected URLs before allowing access
 * <p>
 * SECURITY: Uses longest-prefix-first matching to prevent privilege escalation
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    // URL -> Required Permission mapping
    private static final Map<String, String> PERMISSION_MAP =
            new LinkedHashMap<>();

    static {
        // === IMPORTANT: Order from MOST SPECIFIC to LEAST SPECIFIC ===

        //Admin User Management (most specific first)
        PERMISSION_MAP.put("/admin/users/delete", "USER_DELETE");
        PERMISSION_MAP.put("/admin/users/edit", "USER_UPDATE");
        PERMISSION_MAP.put("/admin/users/update", "USER_UPDATE");
        PERMISSION_MAP.put("/admin/users/create", "USER_CREATE");
        PERMISSION_MAP.put("/admin/users/add", "USER_CREATE");
        PERMISSION_MAP.put("/admin/users/toggle", "USER_UPDATE");
        PERMISSION_MAP.put("/admin/users/view", "USER_READ");
        PERMISSION_MAP.put("/admin/users", "USER_READ");

        //Admin Advanced (Role Management)
        PERMISSION_MAP.put("/admin/roles/permissions", "ROLE_UPDATE");
        PERMISSION_MAP.put("/admin/roles/toggle", "ROLE_UPDATE");
        PERMISSION_MAP.put("/admin/roles/edit", "ROLE_UPDATE");
        PERMISSION_MAP.put("/admin/roles/view", "ROLE_READ");
        PERMISSION_MAP.put("/admin/roles", "ROLE_READ");

        //Admin Password Reset Management
        PERMISSION_MAP.put("/admin/password-reset", "PASSWORD_RESET_MANAGE");

        // Task Management (most specific first) - /management path
        PERMISSION_MAP.put("/management/tasks/delete", "TASK_DELETE");
        PERMISSION_MAP.put("/management/tasks/edit", "TASK_UPDATE");
        PERMISSION_MAP.put("/management/tasks/create", "TASK_CREATE");
        PERMISSION_MAP.put("/management/tasks/view", "TASK_READ");
        PERMISSION_MAP.put("/management/tasks", "TASK_READ");
    }

    private AuthorizationService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authService = new AuthorizationService();
        // TODO: Replace System.out with Logger (SLF4J/Log4j)
        System.out.println("[AuthorizationFilter] Initialized with " +
                PERMISSION_MAP.size() + " protected URLs");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Skip static resources
        if (isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Skip public pages (login, mock-login, home, etc.)
        if (isPublicPage(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if this path requires a permission
        String requiredPermission = getRequiredPermission(path);

        if (requiredPermission != null) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute("user") == null) {
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }

            User currentUser = (User) session.getAttribute("user");

            if (!authService.hasPermission(currentUser.getId(),
                    requiredPermission)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("errorMessage",
                        "Access Denied: You need permission '" +
                                requiredPermission + "'");
                request.setAttribute("requestedUrl", path);
                request.getRequestDispatcher("/WEB-INF/error/403.jsp")
                        .forward(request, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Check if the path is a static resource
     */
    private boolean isStaticResource(String path) {
        return path.startsWith("/assets/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.endsWith(".ico");
    }

    /**
     * Check if the path is a public page (no authentication required)
     */
    private boolean isPublicPage(String path) {
        return path.equals("/") ||
                path.equals("/login") ||
                path.equals("/mock-login") ||
                path.equals("/home") ||
                path.equals("/index.jsp") ||
                path.startsWith("/demo");
    }

    /**
     * Find the required permission for a given path
     * Uses LONGEST PREFIX MATCHING to prevent privilege escalation
     */
    private String getRequiredPermission(String path) {
        // Exact match first
        if (PERMISSION_MAP.containsKey(path)) {
            return PERMISSION_MAP.get(path);
        }

        // Longest prefix match
        String longestMatch = null;
        String matchedPermission = null;

        for (Map.Entry<String, String> entry : PERMISSION_MAP.entrySet()) {
            String pattern = entry.getKey();

            if (path.startsWith(pattern)) {
                if (longestMatch == null ||
                        pattern.length() > longestMatch.length()) {
                    longestMatch = pattern;
                    matchedPermission = entry.getValue();
                }
            }
        }

        return matchedPermission;
    }

    @Override
    public void destroy() {
        System.out.println("[AuthorizationFilter] Destroyed");
    }
}