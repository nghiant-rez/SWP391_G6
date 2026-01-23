/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.swp391.group6.controller;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.dao.UserDaoImpl;
import com.swp391.group6.model.User;
import com.swp391.group6.util.PasswordUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDao;
    
    @Override
    public void init(){
        userDao = new UserDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        //validate input
        if(email == null || email.isEmpty()|| password == null || password.isEmpty()){
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        //tim user
        User user = userDao.findByEmail(email);
        
        //check email co ton tai khong
        if(user == null){
            request.setAttribute("error", "Tài khoản không tồn tại");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        //check tài khoản da bi xoa chua.
        if(user.isDeleted()){
            request.setAttribute("error", "tài khoản đã bị xóa");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        //check password voi BCrypt
        if(!PasswordUtil.checkPassword(password, user.getPassword())){
            request.setAttribute("error", "Mật khẩu không đúng");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        //Login thanh cong - tao session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("id", user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("role", user.getRoleId());
        
        //Redirict ve home
        response.sendRedirect("home.jsp");
    }
}
