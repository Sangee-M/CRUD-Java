package studentservlet;

import java.util.*;
import javax.servlet.*;
import java.sql.*;
import javax.servlet.http.*;
import org.jetbrains.annotations.NotNull;
//import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dbconnection.Dbconnection;


//@WebServlet(urlPatterns = {"/student", "/all-students"})

public class Student extends HttpServlet
{
    private static final Logger logger = LogManager.getLogger(Student.class);
    Dbconnection obj = new Dbconnection();
    Connection connection;
    List<Map<String, Object>> list;
    Map<String, Object> map;
    PreparedStatement selectStatement;
    ResultSet resultSet;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
            // Perform READ operation

            String url = request.getRequestURI();
            String selectQuery;
            try
            {
                connection = obj.connectDb(request, response, logger); //connecting database by user-defined method "connectDb"

                if (url.endsWith("/student")) //Retrieving data of a student by roll no.
                {
                    int rno = Integer.parseInt(request.getParameter("rno")); // Retrieve parameters from the request
                    selectQuery = "SELECT * FROM Student WHERE rno = ?";
                    selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, rno);
                }
                else if(url.endsWith("/all-students")) //Retrieving all student's data
                {
                    selectQuery = "SELECT * FROM Student";
                    selectStatement = connection.prepareStatement(selectQuery);
                }
                resultSet = selectStatement.executeQuery();

                list = new ArrayList<>();
                map = new HashMap<>();

                if(resultSet.next())
                {
                    do
                    {
                        map.put("RNO", resultSet.getInt("rno"));
                        map.put("Student name", resultSet.getString("sname"));
                        map.put("Age", resultSet.getInt("age"));
                        map.put("Dept name", resultSet.getString("dept"));
                        map.put("DOB", resultSet.getString("dob"));
                        list.add(map);
                        map = new HashMap<>();
                    }while(resultSet.next());
                    logger.info("READ operation completed successfully.");// Log the READ operation
                }
                else
                {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    map.put("warning:", "student not found");
                    list.add(map);
                    logger.warn("Data not found");
                }
                obj.toJson(response, list);//invoking user-defined method "toJson" to convert response as json
                resultSet.close();
                selectStatement.close();
                connection.close();// Close the database connection
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    @Override
    protected void doDelete(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException
    {
        // Perform DELETE operation
        int rno = Integer.parseInt(request.getParameter("rno"));// Retrieve parameters from the request

        try
        {
            connection = obj.connectDb(request, response, logger); //connecting database by user-defined method "connectDb"

            String deleteQuery;
            deleteQuery = "DELETE FROM Student WHERE rno = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, rno);

            int rowsAffected;
            rowsAffected = deleteStatement.executeUpdate();
            deleteStatement.close();
            connection.close(); // Close the database connection

            list = new ArrayList<>();
            map = new HashMap<>();

            if (rowsAffected > 0)
            {
                map.put("Info:", "DELETE operation completed successfully.");
                list.add(map);
                obj.toJson(response, list);
                logger.info("DELETE operation completed successfully.");// Log the DELETE operation
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                map.put("warning:", "student not found");
                list.add(map);
                obj.toJson(response, list); //invoking user-defined method "toJson" to convert response as json
                logger.warn("Data not found");
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException
    {
        // Perform CREATE operation

        // Retrieve parameters from the request
        int rno = Integer.parseInt(request.getParameter("rno"));
        String sname = request.getParameter("sname");
        int age = Integer.parseInt(request.getParameter("age"));
        String dept = request.getParameter("dept");
        String dob = request.getParameter("dob");

        try
        {
            connection = obj.connectDb(request, response, logger);

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
            connection.close();

            list = new ArrayList<>();
            map = new HashMap<>();
            map.put("Info:", "CREATE operation completed successfully.");
            list.add(map);
            obj.toJson(response, list); //invoking user-defined method to convert response as json
            logger.info("CREATE operation completed successfully."); // Log the CREATE operation
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    protected void doPut(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException
    {
        // Perform UPDATE operation
        int rno = Integer.parseInt(request.getParameter("rno"));
        String sname = request.getParameter("sname");
        int age = Integer.parseInt(request.getParameter("age"));
        String dept = request.getParameter("dept");
        String dob = request.getParameter("dob");

        try {
            Connection connection = obj.connectDb(request, response, logger);
            String updateQuery = "UPDATE Student SET sname = ?,age = ?,dept = ?,dob = ? WHERE rno = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, sname);
            updateStatement.setInt(2, age);
            updateStatement.setString(3, dept);
            updateStatement.setDate(4, java.sql.Date.valueOf(dob));
            updateStatement.setInt(5, rno);
            updateStatement.executeUpdate();

            updateStatement.close();
            connection.close();

            list = new ArrayList<>();
            map = new HashMap<>();
            map.put("Info:", "UPDATE operation completed successfully.");
            list.add(map);
            obj.toJson(response, list);
            logger.info("UPDATE operation completed successfully.");// Log the UPDATE operation
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}



