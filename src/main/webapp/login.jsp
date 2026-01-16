<%-- 
    Document   : login.jsp
    Created on : Jan 16, 2026, 1:04:30 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Login</h1>
        <% String error = (String) request.getAttribute("error");%>
        <% if(error != null) { %>
            <p style="color: red;"><%= error %></p>
        <% } %>
        
        <form action="login" method="post">
            <table>
                    <tr>
                        <td>Email:</td>
                        <td><input type="text" name="email" required/></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" required/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <button type="submit">Login</button>
                        </td>
                    </tr>
            </table>
        </form>
    </body>
</html>
