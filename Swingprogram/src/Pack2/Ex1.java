package jdbcex;
import java.sql.*;

public class Q1_CreateStudentTable {

    static final String DRIVER   = "org.postgresql.Driver";
    static final String DB_URL   = "jdbc:postgresql://192.168.1.17/mca_25_db";
    static final String USER     = "25mmce38";
    static final String PASSWORD = "25mmce38";

    public static void main(String[] args) {

        Connection con = null;
        Statement  smt = null;

        try {
            // Step 2: Load Driver
            Class.forName(DRIVER);

            // Step 3: Establish Connection
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to database...");

            // Step 4: Write SQL query
            String qry = "CREATE TABLE IF NOT EXISTS student (" +
                    "roll     INT PRIMARY KEY, " +
                    "name     VARCHAR(100) NOT NULL, " +
                    "mobile   BIGINT, " +
                    "email    VARCHAR(255), " +
                    "address  VARCHAR(500), " +
                    "gender   VARCHAR(10), " +
                    "cgpa     DOUBLE PRECISION" +
                    ")";

            // Step 5: Execute query
            smt = con.createStatement();
            int result = smt.executeUpdate(qry);

            if (result == 0) {
                System.out.println("Student table created successfully.");
            }

        } catch (ClassNotFoundException ce) {
            System.out.println("ERROR: PostgreSQL Driver not found.");
            System.out.println("Make sure postgresql.jar is in your classpath.");
            ce.printStackTrace();

        } catch (SQLException se) {
            // PSQLException state codes
            if (se.getSQLState().equals("42P07")) {
                System.out.println("Table 'student' already exists.");
            } else {
                System.out.println("Database error: " + se.getMessage());
                System.out.println("SQL State : " + se.getSQLState());
                System.out.println("Error Code: " + se.getErrorCode());
                se.printStackTrace();
            }

        } finally {
            // Step 6: Close resources safely
            try {
                if (smt != null) smt.close();
            } catch (SQLException e) {
                System.out.println("Failed to close Statement: " + e.getMessage());
            }
            try {
                if (con != null) con.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close Connection: " + e.getMessage());
            }
        }
    }
}