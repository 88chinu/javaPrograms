package jdbcex;
import java.sql.*;

public class Q3_DisplayStudents {

    static final String DRIVER   = "org.postgresql.Driver";
    static final String DB_URL   = "jdbc:postgresql://192.168.1.17/mca_25_db";
    static final String USER     = "25mmce38";
    static final String PASSWORD = "25mmce38";

    public static void main(String[] args) {

        Connection con = null;
        Statement  smt = null;
        ResultSet  rs  = null;

        try {
            // Step 2 & 3
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Step 4: SELECT query
            String sql = "SELECT roll, name, mobile, email, address, gender, cgpa "
                    + "FROM student "
                    + "ORDER BY roll ASC";

            smt = con.createStatement();

            // Step 5: executeQuery returns ResultSet
            rs = smt.executeQuery(sql);

            // Check if any rows exist
            boolean hasData = false;

            // Table header
            System.out.println("=".repeat(90));
            System.out.printf("%-6s %-20s %-12s %-25s %-15s %-8s %-6s%n",
                    "Roll", "Name", "Mobile", "Email", "Address", "Gender", "CGPA");
            System.out.println("=".repeat(90));

            // Iterate through each row
            while (rs.next()) {
                hasData = true;

                int    roll    = rs.getInt("roll");
                String name    = rs.getString("name");
                long   mobile  = rs.getLong("mobile");
                String email   = rs.getString("email");
                String address = rs.getString("address");
                String gender  = rs.getString("gender");
                double cgpa    = rs.getDouble("cgpa");

                // Handle NULL values safely
                if (rs.wasNull()) cgpa = 0.0;
                if (name    == null) name    = "N/A";
                if (email   == null) email   = "N/A";
                if (address == null) address = "N/A";
                if (gender  == null) gender  = "N/A";

                System.out.printf("%-6d %-20s %-12d %-25s %-15s %-8s %-6.2f%n",
                        roll, name, mobile, email, address, gender, cgpa);
            }

            System.out.println("=".repeat(90));

            if (!hasData) {
                System.out.println("No student records found in the database.");
            }

        } catch (ClassNotFoundException ce) {
            System.out.println("ERROR: PostgreSQL Driver not found.");
            ce.printStackTrace();

        } catch (SQLException se) {
            if (se.getSQLState().equals("42P01")) {
                System.out.println("ERROR: Table 'student' does not exist. Run Q1 first.");
            } else {
                System.out.println("Database error: " + se.getMessage());
                System.out.println("SQL State: " + se.getSQLState());
                se.printStackTrace();
            }

        } finally {
            // Close in reverse order: ResultSet → Statement → Connection
            try { if (rs  != null) rs.close();  } catch (SQLException e) { e.printStackTrace(); }
            try { if (smt != null) smt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}