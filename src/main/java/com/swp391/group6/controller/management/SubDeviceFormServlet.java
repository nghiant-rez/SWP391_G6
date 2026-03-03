package com.swp391.group6.controller.management;

import com.swp391.group6.dao.DeviceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.swp391.group6.model.Device;

import java.io.IOException;

@WebServlet("/management/devices/sub-devices/form")
public class SubDeviceFormServlet extends HttpServlet {
    private DeviceDAO deviceDAO;

    @Override
    public void init() throws ServletException {
        deviceDAO = new DeviceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get parent device ID
        String deviceIdStr = request.getParameter("deviceId");
        if (deviceIdStr == null || deviceIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/management/devices");
            return;
        }

        int deviceId;
        try {
            deviceId = Integer.parseInt(deviceIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/management/devices");
            return;
        }

        // Get parent device
        Device device = deviceDAO.getDeviceById(deviceId);
        if (device == null) {
            response.sendRedirect(request.getContextPath() + "/management/devices");
            return;
        }

        request.setAttribute("device", device);
        request.getRequestDispatcher("/WEB-INF/management/sub-device-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String deviceIdStr = request.getParameter("deviceId");
        String serialNumbers = request.getParameter("serialNumbers");

        if (deviceIdStr == null || serialNumbers == null || serialNumbers.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập số seri");
            request.getRequestDispatcher("/WEB-INF/management/sub-device-form.jsp").forward(request, response);
            return;
        }

        // TODO: Implement actual sub-device creation logic
        // For now, just redirect back to list
        response.sendRedirect(request.getContextPath() + "/management/devices/sub-devices?deviceId=" + deviceIdStr);
    }
}
