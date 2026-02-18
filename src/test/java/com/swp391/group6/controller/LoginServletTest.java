package com.swp391.group6.controller;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import com.swp391.group6.util.PasswordUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LoginServletTest {

    private LoginServlet servlet;
    private UserDAO userDao;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LoginServlet();
        userDao = mock(UserDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        session = mock(HttpSession.class);

        setUserDao(servlet, userDao);
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);
    }

    @Test
    void doPost_withMissingCredentials_forwardsWithValidationError() throws Exception {
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        verifyNoInteractions(userDao);
    }

    @Test
    void doPost_withUnknownUser_forwardsWithNotFoundError() throws Exception {
        when(request.getParameter("email")).thenReturn("missing@example.com");
        when(request.getParameter("password")).thenReturn("irrelevant");
        when(userDao.findByEmail("missing@example.com")).thenReturn(null);

        servlet.doPost(request, response);

        verify(userDao).findByEmail("missing@example.com");
        verify(request).setAttribute("error", "Tài khoản không tồn tại");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doPost_withWrongPassword_forwardsWithPasswordError() throws Exception {
        String hashedPassword = PasswordUtil.hashPassword("correctPass");
        User user = new User();
        user.setPassword(hashedPassword);

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("password")).thenReturn("wrongPass");
        when(userDao.findByEmail("user@example.com")).thenReturn(user);

        servlet.doPost(request, response);

        verify(userDao).findByEmail("user@example.com");
        verify(request).setAttribute("error", "Mật khẩu không đúng");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doPost_withValidCredentials_createsSessionAndRedirectsHome() throws Exception {
        User user = new User();
        user.setId(5);
        user.setEmail("user@example.com");
        user.setFullName("Example User");
        user.setRoleId(2);
        user.setPassword(PasswordUtil.hashPassword("secret123"));

        when(request.getParameter("email")).thenReturn("user@example.com");
        when(request.getParameter("password")).thenReturn("secret123");
        when(userDao.findByEmail("user@example.com")).thenReturn(user);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(userDao).findByEmail("user@example.com");
        verify(session).setAttribute("user", user);
        verify(session).setAttribute("id", user.getId());
        verify(session).setAttribute("email", user.getEmail());
        verify(session).setAttribute("fullName", user.getFullName());
        verify(session).setAttribute("role", user.getRoleId());
        verify(response).sendRedirect("home.jsp");
        verify(dispatcher, never()).forward(any(), any());
    }

    private static void setUserDao(LoginServlet servlet, UserDAO userDao) throws Exception {
        Field field = LoginServlet.class.getDeclaredField("userDao");
        field.setAccessible(true);
        field.set(servlet, userDao);
    }
}
