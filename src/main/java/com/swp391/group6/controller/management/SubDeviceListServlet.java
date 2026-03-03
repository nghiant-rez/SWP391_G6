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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/management/devices/sub-devices")
public class SubDeviceListServlet extends HttpServlet {
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

        // Get filter parameters
        String serialFilter = request.getParameter("serialFilter");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        // TODO: Implement actual sub-device logic
        // For now, return empty list
        List<Object> subDevices = new ArrayList<>();

        request.setAttribute("device", device);
        request.setAttribute("subDevices", subDevices);
        request.setAttribute("serialFilter", serialFilter);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        
        request.getRequestDispatcher("/WEB-INF/management/sub-device-list.jsp").forward(request, response);
    }
}
