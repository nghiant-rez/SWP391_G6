package com.swp391.group6.controller.management;

import com.swp391.group6.dao.DeviceDAO;
import com.swp391.group6.dao.ProductDAO;
import com.swp391.group6.model.Device;
import com.swp391.group6.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/management/devices")
public class DeviceListServlet extends HttpServlet {
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

        // Get filter parameters
        String keyword = request.getParameter("keyword");
        String productIdStr = request.getParameter("productId");
        String status = request.getParameter("status");

        Integer productId = null;
        if (productIdStr != null && !productIdStr.isEmpty() && !"ALL".equals(productIdStr)) {
            try {
                productId = Integer.parseInt(productIdStr);
            } catch (NumberFormatException e) {
                // Ignore invalid productId
            }
        }

        // Search devices
        List<Device> devices;
        if (keyword != null || productId != null || status != null) {
            devices = deviceDAO.searchDevices(keyword, productId, status);
        } else {
            devices = deviceDAO.getAllActiveDevices();
        }

        // Get statistics
        int availableCount = deviceDAO.getDeviceCountByStatus("AVAILABLE");
        int soldCount = deviceDAO.getDeviceCountByStatus("SOLD");
        int maintenanceCount = deviceDAO.getDeviceCountByStatus("MAINTENANCE");
        int totalCount = availableCount + soldCount + maintenanceCount 
                        + deviceDAO.getDeviceCountByStatus("DECOMMISSIONED");

        // Get all products for filter dropdown
        List<Product> products = productDAO.getAllActiveProducts();

        // Set attributes
        request.setAttribute("devices", devices);
        request.setAttribute("products", products);
        request.setAttribute("availableCount", availableCount);
        request.setAttribute("soldCount", soldCount);
        request.setAttribute("maintenanceCount", maintenanceCount);
        request.setAttribute("totalCount", totalCount);

        // Keep filter values
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedProductId", productIdStr);
        request.setAttribute("selectedStatus", status);

        request.getRequestDispatcher("/WEB-INF/management/device-list.jsp").forward(request, response);
    }
}
