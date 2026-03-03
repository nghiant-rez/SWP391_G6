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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/management/devices/deleted")
public class DeviceDeletedServlet extends HttpServlet {
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
        String categoryStr = request.getParameter("category");

        // TODO: Implement actual deleted devices logic
        // For now, return empty list or sample data
        List<Device> deletedDevices = new ArrayList<>();
        
        // Get all products for filter dropdown
        List<Product> products = productDAO.getAllActiveProducts();

        request.setAttribute("deletedDevices", deletedDevices);
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        
        request.getRequestDispatcher("/WEB-INF/management/device-deleted.jsp").forward(request, response);
    }
}
