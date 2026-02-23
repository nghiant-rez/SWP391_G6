package com.swp391.group6.controller;

import com.swp391.group6.dao.PassResetRequestDAO;
import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private UserDAO userDAO;
    private PassResetRequestDAO resetDAO;

    @Override
    public void init(){
        userDAO = new UserDAO();
        resetDAO = new PassResetRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        //Hien thi from nhap email
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String email = request.getParameter("email");

        //Validate input
        if(email == null||email.trim().isEmpty()){
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        //kiem tra email co ton tai khong
        User user = userDAO.findByEmail(email.trim());

        if(user == null){
            request.setAttribute("error", "Email không tồn tại trong hệ thống");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        //kiem tra tai khoan da bi xoa hoac banned
        if(user.isDeleted()){
            request.setAttribute("error", "Tài khoản đã bị xóa");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        if(!user.isStatus()){
            request.setAttribute("error", "Tài khoản đã bị khóa");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        // Create request reset password
        boolean success = resetDAO.createRequest(user.getId(), user.getEmail());

        if(success){
            request.setAttribute("success", "yêu cầu đặt lại mật khẩu đã được gửi. Vui lòng chờ admin xác nhận.");
            System.out.println("Password reset request created for: " + email);
        }else {
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }
}
