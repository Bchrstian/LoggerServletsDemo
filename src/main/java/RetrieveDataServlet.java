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

@WebServlet("/RetrieveDataServlet")
public class RetrieveDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RetrieveDataServlet.class.getName());
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bestservlet";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "A$aprocky08";

    public RetrieveDataServlet() {
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
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter();

        logger.info("Received request to retrieve all user data");

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            logger.info("Database connection established");

            String sql = "SELECT id, firstname, lastname FROM users";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            out.println("<html><body>");
            out.println("<h1>All Users</h1>");
            out.println("<table border='1'><tr><th>ID</th><th>First Name</th><th>Last Name</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("id") + "</td><td>" + rs.getString("firstname") + "</td><td>" + rs.getString("lastname") + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body></html>");

            logger.info("Successfully retrieved all user data");
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
