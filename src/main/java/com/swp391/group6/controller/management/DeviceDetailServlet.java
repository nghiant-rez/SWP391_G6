package com.swp391.group6.controller.management;

import com.swp391.group6.dao.DeviceDAO;
import com.swp391.group6.model.Device;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/management/devices/detail")
public class DeviceDetailServlet extends HttpServlet {
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

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/management/devices");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Device device = deviceDAO.getDeviceById(id);

            if (device == null) {
                request.setAttribute("errorMessage", "Device not found.");
                request.getRequestDispatcher("/WEB-INF/management/device-list.jsp").forward(request, response);
                return;
            }

            request.setAttribute("device", device);
            request.getRequestDispatcher("/WEB-INF/management/device-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/management/devices");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(request, response);
        } else if ("updateStatus".equals(action)) {
            handleStatusUpdate(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/management/devices");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                boolean success = deviceDAO.deleteDevice(id);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/management/devices?deleted=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/management/devices/detail?id=" + id + "&error=delete_failed");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/management/devices");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/management/devices");
        }
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String idParam = request.getParameter("id");
        String newStatus = request.getParameter("status");

        if (idParam != null && !idParam.isEmpty() && newStatus != null && !newStatus.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Device device = deviceDAO.getDeviceById(id);

                if (device != null) {
                    device.setStatus(newStatus);
                    boolean success = deviceDAO.updateDevice(device);

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/management/devices/detail?id=" + id + "&updated=true");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/management/devices/detail?id=" + id + "&error=update_failed");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/management/devices");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/management/devices");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/management/devices");
        }
    }
}
