package jdbcex;
import java.sql.*;
import java.util.Scanner;

public class Q4_UpdateCGPA {

    static final String DRIVER   = "org.postgresql.Driver";
    static final String DB_URL   = "jdbc:postgresql://192.168.1.17/mca_25_db";
    static final String USER     = "25mmce38";
    static final String PASSWORD = "25mmce38";

    public static void main(String[] args) {

        Connection con = null;
        Statement  smt = null;
        Scanner    sc  = new Scanner(System.in);

        try {
            // ── Input & Validation ─────────────────────────────────────────
            System.out.print("Enter Roll number of the student: ");
            String rollStr = sc.nextLine().trim();

            System.out.print("Enter new CGPA                  : ");
            String cgpaStr = sc.nextLine().trim();

            int roll;
            try {
                roll = Integer.parseInt(rollStr);
                if (roll <= 0) {
                    System.out.println("Roll number must be a positive integer.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid roll number. Please enter a valid integer.");
                return;
            }

            double newCgpa;
            try {
                newCgpa = Double.parseDouble(cgpaStr);
                if (newCgpa < 0.0 || newCgpa > 10.0) {
                    System.out.println("CGPA must be between 0.0 and 10.0.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid CGPA. Please enter a valid decimal number.");
                return;
            }

            // ── Connect ────────────────────────────────────────────────────
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // ── Step 4: First check if the student exists ──────────────────
            String checkQry = "SELECT name, cgpa FROM student WHERE roll=" + roll;
            smt = con.createStatement();
            ResultSet rs = smt.executeQuery(checkQry);

            if (!rs.next()) {
                System.out.println("No student found with Roll = " + roll);
                rs.close();
                return;
            }

            String studentName = rs.getString("name");
            double oldCgpa     = rs.getDouble("cgpa");
            rs.close();

            // ── Step 5: Execute UPDATE ─────────────────────────────────────
            String updateQry = "UPDATE student SET cgpa=" + newCgpa + " WHERE roll=" + roll;
            int rows = smt.executeUpdate(updateQry);

            if (rows == 1) {
                System.out.println("CGPA updated successfully!");
                System.out.println("  Student : " + studentName);
                System.out.println("  Old CGPA: " + oldCgpa);
                System.out.println("  New CGPA: " + newCgpa);
            } else {
                System.out.println("Update failed. Please try again.");
            }

        } catch (ClassNotFoundException ce) {
            System.out.println("ERROR: PostgreSQL Driver not found.");
            ce.printStackTrace();

        } catch (SQLException se) {
            System.out.println("Database error: " + se.getMessage());
            System.out.println("SQL State : " + se.getSQLState());
            System.out.println("Error Code: " + se.getErrorCode());
            se.printStackTrace();

        } finally {
            try { if (smt != null) smt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
            sc.close();
        }
    }
}