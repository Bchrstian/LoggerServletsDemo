import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

@WebServlet("/RetrieveByIdServlet")
public class RetrieveByIdServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RetrieveByIdServlet.class.getName());
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bestservlet";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "A$aprocky08";

    public RetrieveByIdServlet() {
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
            FileHandler fileHandler = new FileHandler("servlet.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Failed to set up file handler: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        logger.info("Received request to retrieve user with ID=" + id);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            logger.info("Database connection established");

            String sql = "SELECT id, firstname, lastname FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            out.println("<html><body>");
            if (rs.next()) {
                logger.info("User found with ID=" + id);
                out.println("<h1>User Details</h1>");
                out.println("<p>ID: " + rs.getString("id") + "</p>");
                out.println("<p>First Name: " + rs.getString("firstname") + "</p>");
                out.println("<p>Last Name: " + rs.getString("lastname") + "</p>");
            } else {
                logger.warning("No user found with ID=" + id);
                out.println("<p>User not found.</p>");
            }
            out.println("</body></html>");
        } catch (ClassNotFoundException | SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            e.printStackTrace();
            out.println("Database error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
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
