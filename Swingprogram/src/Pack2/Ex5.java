package jdbcex;
import java.sql.*;
import java.util.Scanner;

public class Q5_DeleteStudent {

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
            System.out.print("Enter Roll number to delete: ");
            String rollStr = sc.nextLine().trim();

            int roll;
            try {
                roll = Integer.parseInt(rollStr);
                if (roll <= 0) {
                    System.out.println("Roll must be a positive integer.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid roll number. Please enter digits only.");
                return;
            }

            // ── Connect ────────────────────────────────────────────────────
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            smt = con.createStatement();

            // ── Check if student exists before deleting ────────────────────
            ResultSet rs = smt.executeQuery(
                    "SELECT name, email FROM student WHERE roll=" + roll);

            if (!rs.next()) {
                System.out.println("No student found with Roll = " + roll + ". Nothing deleted.");
                rs.close();
                return;
            }

            String name  = rs.getString("name");
            String email = rs.getString("email");
            rs.close();

            // ── Confirmation prompt ────────────────────────────────────────
            System.out.println("Found student → Roll: " + roll
                    + "  Name: " + name + "  Email: " + email);
            System.out.print("Are you sure you want to delete? (yes/no): ");
            String confirm = sc.nextLine().trim().toLowerCase();

            if (!confirm.equals("yes")) {
                System.out.println("Delete cancelled.");
                return;
            }

            // ── Step 5: Execute DELETE ─────────────────────────────────────
            String deleteQry = "DELETE FROM student WHERE roll=" + roll;
            int rows = smt.executeUpdate(deleteQry);

            if (rows == 1) {
                System.out.println("Student record deleted successfully.");
                System.out.println("  Deleted → Roll: " + roll + "  Name: " + name);
            } else {
                System.out.println("Delete failed. Please try again.");
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