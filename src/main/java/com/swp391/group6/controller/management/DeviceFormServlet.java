package com.swp391.group6.controller.management;

import com.swp391.group6.dao.DeviceDAO;
import com.swp391.group6.dao.ProductDAO;
import com.swp391.group6.model.Device;
import com.swp391.group6.model.Product;
import com.swp391.group6.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/management/devices/form")
public class DeviceFormServlet extends HttpServlet {
    private DeviceDAO deviceDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        deviceDAO = new DeviceDAO();
        productDAO = new ProductDAO();
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
        Device device = null;

        // If editing, load the device
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                device = deviceDAO.getDeviceById(id);
                if (device == null) {
                    request.setAttribute("errorMessage", "Device not found.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid device ID.");
            }
        }

        // Get all products for dropdown
        List<Product> products = productDAO.getAllActiveProducts();

        request.setAttribute("device", device);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/management/device-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            String idParam = request.getParameter("id");
            String productIdStr = request.getParameter("productId");
            String serialNumber = request.getParameter("serialNumber");
            String status = request.getParameter("status");
            String condition = request.getParameter("condition");
            String currentLocation = request.getParameter("currentLocation");
            String notes = request.getParameter("notes");

            // Validate required fields
            if (productIdStr == null || productIdStr.isEmpty() ||
                serialNumber == null || serialNumber.trim().isEmpty() ||
                status == null || status.isEmpty() ||
                condition == null || condition.isEmpty()) {
                
                request.setAttribute("errorMessage", "Please fill in all required fields.");
                doGet(request, response);
                return;
            }

            int productId = Integer.parseInt(productIdStr);

            // Check for duplicate serial number
            Integer deviceId = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : null;
            if (deviceDAO.serialNumberExists(serialNumber.trim(), deviceId)) {
                request.setAttribute("errorMessage", "Serial number already exists.");
                doGet(request, response);
                return;
            }

            Device device = new Device();
            device.setProductId(productId);
            device.setSerialNumber(serialNumber.trim());
            device.setStatus(status);
            device.setCondition(condition);
            device.setCurrentLocation(currentLocation != null ? currentLocation.trim() : "");
            device.setNotes(notes != null ? notes.trim() : "");

            boolean success;
            if (deviceId != null) {
                // Update existing device
                device.setId(deviceId);
                success = deviceDAO.updateDevice(device);
            } else {
                // Create new device
                device.setCreatedBy(user.getId());
                success = deviceDAO.insertDevice(device);
            }

            if (success) {
                response.sendRedirect(request.getContextPath() + "/management/devices?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to save device. Please try again.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format.");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }
}
