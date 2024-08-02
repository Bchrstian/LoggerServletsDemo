import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TestingServlet")
public class TestingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TestingServlet.class.getName());
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bestservlet";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "A$aprocky08";

    public TestingServlet() {
        super();
        setupLogger();
    }

    private void setupLogger() {
        try {
            // Set up console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Set up file handler
            FileHandler fileHandler = new FileHandler("testing_servlet.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Failed to set up file handler: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement pstmt = null;

        logger.info("Received request to register a new user with ID: " + id);

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            logger.info("Database connection established");

            String sql = "INSERT INTO users (id, firstname, lastname, password) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, password);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("User registered successfully with ID: " + id);
                // Redirect to the welcome page with the user information
                request.setAttribute("id", id);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.getRequestDispatcher("welcome.jsp").forward(request, response);
            } else {
                logger.warning("Failed to register user with ID: " + id);
                PrintWriter out = response.getWriter();
                out.println("Error registering user.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            e.printStackTrace();
            PrintWriter out = response.getWriter();
            out.println("Database error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                logger.info("Database resources closed");
            } catch (SQLException e) {
                logger.severe("Error closing database resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
