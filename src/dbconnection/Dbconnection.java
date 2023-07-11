package dbconnection;

import java.util.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
public class Dbconnection
{
    public Connection connectDb(HttpServletResponse res, Logger log) throws IOException, SQLException
    {
        Connection connection=null;
        List<Map<String, Object>> list= new ArrayList<>();;
        Map<String, Object> map=new HashMap<>();
        try
        {
            // Create a database connection
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/myDB";
            String username = "postgres";
            String password = "sangeepsql";
            connection=getConnection(url, username, password);

        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            map.put("error:", "Failed to load the PostgreSQL JDBC driver.");
            list.add(map);
            toJson(res, list);
            log.error("Failed to load the PostgreSQL JDBC driver.", e);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            map.put("error:", "An error occurred during CRUD operations.");
            list.add(map);
            toJson(res, list);
            log.error("An error occurred during CRUD operations.", e);
        }
       return connection;
    }
    public void toJson(HttpServletResponse res, List list)throws IOException //converting response as json
    {
        ObjectMapper objectMapper = new ObjectMapper(); //Create JSON response using ObjectMapper
        String jsonOutput = objectMapper.writeValueAsString(list);
        res.setContentType("application/json");
        res.getWriter().write(jsonOutput); // Send a response back to the client

    }
}

