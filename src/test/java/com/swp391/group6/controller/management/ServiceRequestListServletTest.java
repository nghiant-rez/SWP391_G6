package com.swp391.group6.controller.management;

import com.swp391.group6.dao.ServiceRequestDAO;
import com.swp391.group6.model.ServiceRequest;
import com.swp391.group6.model.User;
import com.swp391.group6.service.AuthorizationService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceRequestListServlet Tests")
class ServiceRequestListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServiceRequestDAO serviceRequestDAO;

    @Mock
    private AuthorizationService authService;

    private ServiceRequestListServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new ServiceRequestListServlet();
    }

    @Test
    @DisplayName("doGet with no filters returns all service requests")
    void testDoGetNoFilters() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("requests"), any(List.class));
        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with search filter")
    void testDoGetWithSearchFilter() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn("device");
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("searchValue"), eq("device"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with status filter")
    void testDoGetWithStatusFilter() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn("OPEN");
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("statusValue"), eq("OPEN"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with requestType filter")
    void testDoGetWithRequestTypeFilter() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn("REPAIR");
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("requestTypeValue"), eq("REPAIR"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with priority filter")
    void testDoGetWithPriorityFilter() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn("URGENT");
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("priorityValue"), eq("URGENT"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with all filters combined")
    void testDoGetWithAllFilters() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn("test");
        when(request.getParameter("status")).thenReturn("IN_PROGRESS");
        when(request.getParameter("requestType")).thenReturn("MAINTENANCE");
        when(request.getParameter("priority")).thenReturn("HIGH");
        when(request.getParameter("page")).thenReturn("2");
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("searchValue", "test");
        verify(request).setAttribute("statusValue", "IN_PROGRESS");
        verify(request).setAttribute("requestTypeValue", "MAINTENANCE");
        verify(request).setAttribute("priorityValue", "HIGH");
        verify(request).setAttribute("currentPage", 2);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with invalid page parameter defaults to 1")
    void testDoGetWithInvalidPage() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("abc");
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with negative page parameter defaults to 1")
    void testDoGetWithNegativePage() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("-5");
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet sets permission attributes correctly")
    void testDoGetSetsPermissionAttributes() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("canProcess"), anyBoolean());
        verify(request).setAttribute(eq("canDelete"), anyBoolean());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet forwards to correct JSP")
    void testDoGetForwardsToCorrectJSP() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/management/service-request-list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with empty string filters")
    void testDoGetWithEmptyStringFilters() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn("");
        when(request.getParameter("status")).thenReturn("");
        when(request.getParameter("requestType")).thenReturn("");
        when(request.getParameter("priority")).thenReturn("");
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("searchValue"), eq(""));
        verify(request).setAttribute(eq("statusValue"), eq(""));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet calculates total pages correctly")
    void testDoGetCalculatesTotalPages() throws ServletException, IOException {
        // Arrange
        User mockUser = createMockUser(1, "admin@example.com");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("status")).thenReturn(null);
        when(request.getParameter("requestType")).thenReturn(null);
        when(request.getParameter("priority")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/management/service-request-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("totalPages"), anyInt());
        verify(request).setAttribute(eq("totalRequests"), anyInt());
        verify(dispatcher).forward(request, response);
    }

    private User createMockUser(int id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFullName("Mock User");
        return user;
    }

    private List<ServiceRequest> createMockServiceRequests(int count) {
        List<ServiceRequest> requests = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ServiceRequest sr = new ServiceRequest();
            sr.setId(i);
            sr.setRequestCode("SR-" + String.format("%04d", i));
            sr.setSubject("Test Request " + i);
            sr.setStatus(ServiceRequest.STATUS_OPEN);
            sr.setPriority(ServiceRequest.PRIORITY_MEDIUM);
            requests.add(sr);
        }
        return requests;
    }
}