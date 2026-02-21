package com.swp391.group6.controller.admin;

import com.swp391.group6.dao.UserDAO;
import com.swp391.group6.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@DisplayName("UserListServlet Tests")
class UserListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private UserDAO userDAO;

    private UserListServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new UserListServlet();
    }

    @Test
    @DisplayName("doGet with no parameters returns first page")
    void testDoGetNoParameters() throws ServletException, IOException {
        // Arrange
        List<User> mockUsers = createMockUsers(10);
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("users"), any(List.class));
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute(eq("totalPages"), anyInt());
        verify(request).setAttribute(eq("totalUsers"), anyInt());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with search parameter filters users")
    void testDoGetWithSearch() throws ServletException, IOException {
        // Arrange
        String searchKeyword = "john";
        when(request.getParameter("search")).thenReturn(searchKeyword);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("users"), any(List.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with valid page parameter")
    void testDoGetWithValidPage() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("2");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 2);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with invalid page parameter defaults to 1")
    void testDoGetWithInvalidPage() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("invalid");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
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
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("-1");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with zero page parameter defaults to 1")
    void testDoGetWithZeroPage() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("0");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 1);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with empty search parameter")
    void testDoGetWithEmptySearch() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn("");
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("users"), any(List.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet calculates pagination correctly")
    void testDoGetPaginationCalculation() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("3");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 3);
        verify(request).setAttribute(eq("totalPages"), anyInt());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet forwards to correct JSP")
    void testDoGetForwardsToCorrectJSP() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/admin/user-list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with large page number")
    void testDoGetWithLargePageNumber() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("999");
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute("currentPage", 999);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet with whitespace search parameter")
    void testDoGetWithWhitespaceSearch() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn("   ");
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("users"), any(List.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet sets all required attributes")
    void testDoGetSetsAllRequiredAttributes() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/admin/user-list.jsp"))
            .thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).setAttribute(eq("users"), any());
        verify(request).setAttribute(eq("currentPage"), any());
        verify(request).setAttribute(eq("totalPages"), any());
        verify(request).setAttribute(eq("totalUsers"), any());
        verify(dispatcher).forward(request, response);
    }

    private List<User> createMockUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setId(i);
            user.setEmail("user" + i + "@example.com");
            user.setFullName("User " + i);
            user.setStatus(true);
            users.add(user);
        }
        return users;
    }
}