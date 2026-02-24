package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.PassResetRequestDAO;
import com.swp391.group6.model.PasswordResetRequest;
import com.swp391.group6.model.User;
import com.swp391.group6.util.EmailUtil;
import com.swp391.group6.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminPasswordResetServlet", urlPatterns = {"/admin/password-reset"})
public class AdminPasswordResetServlet extends HttpServlet {

    private PassResetRequestDAO resetDAO;

    @Override
    public void init(){
        resetDAO = new PassResetRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        HttpSession session = request.getSession();

        //check 1: login
        if(session == null || session.getAttribute("user") == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        //Check 2: co phai la admin khong
        if(currentUser.getRoleId() == null || currentUser.getRoleId() != 1){
            response.sendRedirect(request.getContextPath() + "/home.jsp?error=access_denied");
            return;
        }

        //lay danh sach request PENDING
        List<PasswordResetRequest> pendingRequests = resetDAO.getPendingRequests();

        request.setAttribute("requests", pendingRequests);
        request.getRequestDispatcher("/admin-password-reset.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        HttpSession session = request.getSession();

        // check authentication & authorization
        if(session == null || session.getAttribute("user") == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if(currentUser.getRoleId() == null || currentUser.getRoleId() != 1){
            response.sendRedirect(request.getContextPath() + "/home.jsp?error=access_denied");
            return;
        }

        //lay parameters
        String action = request.getParameter("action");
        String requestIdStr = request.getParameter("requestId");

        if(requestIdStr == null || requestIdStr.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=invalid-request-request");
            return;
        }

        int requestId = Integer.parseInt(requestIdStr);

        //lay thong tin request
        PasswordResetRequest resetRequest = resetDAO.getRequestById(requestId);

        if(resetRequest == null){
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=request-not-found");
            return;
        }

        //xu ly theo action
        if("approve".equals(action)){
            handleApprove(resetRequest, currentUser.getId(), request, response);
        }else if("reject".equals(action)){
            handleReject(resetRequest, currentUser.getId(), request, response);
        }else {
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=invalid-request-action");
        }
    }

    //Xu ly APPROVE request
    private void handleApprove(PasswordResetRequest resetRequest, int adminId,
                               HttpServletRequest request, HttpServletResponse response)
        throws IOException{

        //1. Generate new password
        String newPlainPassword = PasswordUtil.generateRandomPassword();
        String newHashedPassword = PasswordUtil.hashPassword(newPlainPassword);

        //2 Update request status → APPROVED
        boolean requestUpdate = resetDAO.approveRequest(resetRequest.getId(), newHashedPassword, adminId);

        if(!requestUpdate){
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=approve_failed");
            return;
        }

        //3.Update in user table
        Boolean passwordUpdate = resetDAO.updateUserPassword(resetRequest.getUserId(), newHashedPassword);

        if(!passwordUpdate){
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=update_password_failed");
        }

        //4. Gui email cho user
        boolean emailSent = EmailUtil.sendPasswordResetEmail(
                resetRequest.getEmail(),
                resetRequest.getUserFullName(),
                newPlainPassword
        );

        if(emailSent){
            System.out.println("Password reset approved for: " + resetRequest.getEmail());
            System.out.println("New password: " + newPlainPassword);
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?success=approved");
        }else {
            System.err.println("Failed to send email to: " + resetRequest.getEmail());
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?warning=email_failed");
        }
    }

    // xu ly reject request
    private void handleReject(PasswordResetRequest resetRequest, int adminId,
                               HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        String reason = request.getParameter("reason");

        if(reason == null || reason.trim().isEmpty()){
            reason = "không đủ điều kiện";
        }

        boolean sucess = resetDAO.rejectRequest(resetRequest.getId(), reason, adminId);

        if(sucess){
            System.out.println("Password reset rejected for: " + resetRequest.getEmail());
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?success=rejected");
        }else {
            response.sendRedirect(request.getContextPath() + "/admin/password-reset?error=reject_failed");
        }
    }
}

