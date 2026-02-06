package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.RoleDAO;
import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.Role;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Add new user - Member 3 (Phạm Xuân Ba)
 */
@WebServlet(name = "UserCreateServlet", urlPatterns = {"/admin/users/create", "/admin/users/add"})
public class UserCreateServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Role> roles = roleDAO.getAllRoles(false);
        request.setAttribute("roles", roles);
        request.setAttribute("isEdit", false);
        request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String gender = request.getParameter("gender");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String avatarUrl = request.getParameter("avatarUrl");
            String roleIdStr = request.getParameter("roleId");
            boolean status = "true".equals(request.getParameter("status"));

            if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                fullName == null || fullName.trim().isEmpty() ||
                roleIdStr == null || roleIdStr.trim().isEmpty()) {
                
                List<Role> roles = roleDAO.getAllRoles(false);
                request.setAttribute("roles", roles);
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc!");
                request.setAttribute("isEdit", false);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
                return;
            }

            if (userDAO.isEmailExists(email, null)) {
                List<Role> roles = roleDAO.getAllRoles(false);
                request.setAttribute("roles", roles);
                request.setAttribute("error", "Email đã tồn tại trong hệ thống!");
                request.setAttribute("isEdit", false);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
                return;
            }

            User newUser = new User();
            newUser.setEmail(email.trim());
            newUser.setPassword(password);
            newUser.setFullName(fullName.trim());
            newUser.setGender(gender);
            newUser.setPhone(phone != null ? phone.trim() : null);
            newUser.setAddress(address != null ? address.trim() : null);
            newUser.setAvatarUrl(avatarUrl != null ? avatarUrl.trim() : null);
            newUser.setRoleId(Integer.parseInt(roleIdStr));
            newUser.setStatus(status);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            int createdBy = currentUser.getId();

            boolean success = userDAO.createUser(newUser, createdBy);

            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/admin/users?message=" + java.net.URLEncoder.encode("Thêm người dùng thành công!", "UTF-8"));
            } else {
                List<Role> roles = roleDAO.getAllRoles(false);
                request.setAttribute("roles", roles);
                request.setAttribute("error", "Có lỗi xảy ra khi thêm người dùng!");
                request.setAttribute("isEdit", false);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            List<Role> roles = roleDAO.getAllRoles(false);
            request.setAttribute("roles", roles);
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("isEdit", false);
            request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
        }
    }
}