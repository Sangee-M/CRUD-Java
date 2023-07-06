import org.jetbrains.annotations.NotNull;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.sql.DriverManager.getConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/Student")

public class Studentservlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Studentservlet.class);

    @Override
    protected void doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {

        int rno = Integer.parseInt(request.getParameter("rno"));

        try {

            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/myDB";
            String username = "postgres";
            String password = "sangeepsql";
            Connection connection = getConnection(url, username, password);
            String selectQuery = "SELECT * FROM Student WHERE rno = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, rno);
            ResultSet resultSet = selectStatement.executeQuery();
            List<Map<String, Object>> studentList = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("RNO", resultSet.getInt("rno"));
                student.put("Student name", resultSet.getString("sname"));
                student.put("Age", resultSet.getInt("age"));
                student.put("Dept name", resultSet.getString("dept"));
                student.put("DOB", resultSet.getString("dob"));
                studentList.add(student);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonOutput = objectMapper.writeValueAsString(studentList);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON output to the response
            response.getWriter().write(jsonOutput);
            logger.info("READ operation completed successfully.");
            resultSet.close();
            selectStatement.close();
            connection.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Failed to load the PostgreSQL JDBC driver.");
            logger.error("Failed to load the PostgreSQL JDBC driver.", e);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("An error occurred during CRUD operations.");
            logger.error("An error occurred during CRUD operations.", e);
        }

    }


    @Override
    protected void doDelete(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from the request
        int rno = Integer.parseInt(request.getParameter("rno"));

        // Perform CREATE operation
        try {
            Class.forName("org.postgresql.Driver");
            // Create a database connection
            String url = "jdbc:postgresql://localhost:5432/myDB";
            String username = "postgres";
            String password = "sangeepsql";
            Connection connection = getConnection(url, username, password);

            // DELETE
            String deleteQuery;
            deleteQuery = "DELETE FROM Student WHERE rno = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, rno);

            int rowsAffected;
            rowsAffected = deleteStatement.executeUpdate();
            deleteStatement.close();
            connection.close();

            // Send a response back to the client
            if (rowsAffected > 0) {
                response.setContentType("text/plain");
                response.getWriter().println("DELETE operation completed successfully.");
                logger.info("DELETE operation completed successfully.");
            } else {
                response.setContentType("text/plain");
                response.getWriter().println("Seems like no item in table like that you've given ");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Failed to load the PostgreSQL JDBC driver.");
            logger.error("Failed to load the PostgreSQL JDBC driver.", e);
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("An error occurred during CRUD operations.");
            logger.error("An error occurred during CRUD operations.", e);
        }
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from the request
        int rno = Integer.parseInt(request.getParameter("rno"));
        String sname = request.getParameter("sname");
        int age = Integer.parseInt(request.getParameter("age"));
        String dept = request.getParameter("dept");
        String dob=request.getParameter("dob");
        // Perform CREATE operation
        try {
            Class.forName("org.postgresql.Driver");
            // Create a database connection
            String url = "jdbc:postgresql://localhost:5432/myDB";
            String username = "postgres";
            String password = "sangeepsql";
            Connection connection = getConnection(url, username, password);


            String insertQuery;
            insertQuery = "INSERT INTO Student (rno,sname,age,dept,dob) VALUES (?,?,?,?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, rno);
            insertStatement.setString(2, sname);
            insertStatement.setInt(3, age);
            insertStatement.setString(4, dept);
            insertStatement.setDate(5, java.sql.Date.valueOf(dob));

            insertStatement.executeUpdate();
            insertStatement.close();

            // Close the database connection
            connection.close();

            // Send a response back to the client
            response.setContentType("text/plain");
            response.getWriter().println("CREATE operation completed successfully.");
            logger.info("CREATE operation completed successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Failed to load the PostgreSQL JDBC driver.");
            logger.error("Failed to load the PostgreSQL JDBC driver.", e);
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("An error occurred during CRUD operations.");
            logger.error("An error occurred during CRUD operations.", e);
        }
    }

    protected void doPut(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from the request
        int rno = Integer.parseInt(request.getParameter("rno"));
        String sname = request.getParameter("sname");
        int age = Integer.parseInt(request.getParameter("age"));
        String dept = request.getParameter("dept");
        String dob=request.getParameter("dob");

        // Perform CREATE operation
        try {
            Class.forName("org.postgresql.Driver");
            // Create a database connection
            String url = "jdbc:postgresql://localhost:5432/myDB";
            String username = "postgres";
            String password = "sangeepsql";
            Connection connection = DriverManager.getConnection(url, username, password);

            // UPDATE
            String updateQuery = "UPDATE Student SET sname = ?,age = ?,dept = ?,dob = ? WHERE rno = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, sname);
            updateStatement.setInt(2, age);
            updateStatement.setString(3, dept);
            updateStatement.setDate(4, java.sql.Date.valueOf(dob));
            updateStatement.setInt(5, rno);
            updateStatement.executeUpdate();
            updateStatement.close();

            // Close the database connection
            connection.close();

            // Send a response back to the client
            response.setContentType("text/plain");
            response.getWriter().println("UPDATE operation completed successfully.");
            logger.info("UPDATE operation completed successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Failed to load the PostgreSQL JDBC driver.");
            logger.error("Failed to load the PostgreSQL JDBC driver.", e);
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("An error occurred during CRUD operations.");
            logger.error("An error occurred during CRUD operations.", e);
        }
    }

}
