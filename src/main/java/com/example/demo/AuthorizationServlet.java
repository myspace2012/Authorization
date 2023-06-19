
/**
 * @AUTHOR DAV
 * @create 6/18/2023 8:10 PM
 */
package com.example.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "5595";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Perform database search and authentication here
        boolean isValid = searchInDatabase(username, password);

        if (isValid) {
            request.getSession().setAttribute("message", "Login successful!");
        } else {
            request.getSession().setAttribute("message", "Invalid username or password.");
        }

        response.sendRedirect("match.jsp");
    }

    private boolean searchInDatabase(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Return true if a matching record is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if there is an error or no matching record is found
    }
}
