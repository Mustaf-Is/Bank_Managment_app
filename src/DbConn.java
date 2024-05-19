import java.sql.*;

/**
 * Connects the project with mySql database
 */
public class DbConn {

    private static Connection con;
    private static String URL = "jdbc:mysql://localhost:3306/bankapplication";
    private static String USER = "root";
    private static String PASSWORD = "";
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static Connection getConnection() throws SQLException{
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
    
}
