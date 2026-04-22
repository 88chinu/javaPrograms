package jdbcex;
import java.sql.*;
import java.util.Scanner;

public class Q2_InsertStudentData {

    static final String DRIVER   = "org.postgresql.Driver";
    static final String DB_URL   = "jdbc:postgresql://192.168.1.17/mca_25_db";
    static final String USER     = "25mmca01";
    static final String PASSWORD = "25mmca01";

    public static void main(String[] args) {

        Connection con = null;
        Statement  smt = null;
        Scanner    sc  = new Scanner(System.in);

        try {
            // ── Collect user input ──────────────────────────────────────────
            System.out.print("Enter Roll    : ");
            String rollStr = sc.nextLine().trim();

            System.out.print("Enter Name    : ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Mobile  : ");
            String mobileStr = sc.nextLine().trim();

            System.out.print("Enter Email   : ");
            String email = sc.nextLine().trim();

            System.out.print("Enter Address : ");
            String address = sc.nextLine().trim();

            System.out.print("Enter Gender (Male/Female/Other): ");
            String gender = sc.nextLine().trim();

            System.out.print("Enter CGPA    : ");
            String cgpaStr = sc.nextLine().trim();

            // ── Validate and parse numeric inputs ──────────────────────────
            int roll;
            try {
                roll = Integer.parseInt(rollStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid roll number. Please enter a valid integer.");
                return;
            }

            long mobile;
            try {
                mobile = Long.parseLong(mobileStr);
                if (mobileStr.length() != 10) {
                    System.out.println("Warning: Mobile number should be 10 digits.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid mobile number. Please enter digits only.");
                return;
            }

            double cgpa;
            try {
                cgpa = Double.parseDouble(cgpaStr);
                if (cgpa < 0.0 || cgpa > 10.0) {
                    System.out.println("Invalid CGPA. Must be between 0.0 and 10.0.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid CGPA. Please enter a valid decimal number.");
                return;
            }

            // ── Validate text fields ───────────────────────────────────────
            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || gender.isEmpty()) {
                System.out.println("All fields are required. Please fill in every field.");
                return;
            }

            // ── Step 2 & 3: Load driver and connect ────────────────────────
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // ── Step 4: Build query safely ─────────────────────────────────
            // NOTE: In production always use PreparedStatement to avoid SQL Injection.
            // Using Statement here as per assignment requirement.
            String qry = "INSERT INTO student(roll, name, mobile, email, address, gender, cgpa) "
                    + "VALUES("
                    + roll        + ", '"
                    + name        + "', "
                    + mobile      + ", '"
                    + email       + "', '"
                    + address     + "', '"
                    + gender      + "', "
                    + cgpa
                    + ")";

            smt = con.createStatement();

            // ── Step 5: Execute ────────────────────────────────────────────
            int rows = smt.executeUpdate(qry);

            if (rows == 1) {
                System.out.println("Student record inserted successfully!");
                System.out.println("  Roll: " + roll + "  Name: " + name + "  CGPA: " + cgpa);
            } else {
                System.out.println("Insert failed. No rows affected.");
            }

        } catch (ClassNotFoundException ce) {
            System.out.println("ERROR: PostgreSQL Driver not found.");
            ce.printStackTrace();

        } catch (SQLException se) {
            if (se.getSQLState().equals("23505")) {
                System.out.println("ERROR: A student with this roll number already exists.");
            } else if (se.getSQLState().equals("23502")) {
                System.out.println("ERROR: A required field is missing (NOT NULL violation).");
            } else {
                System.out.println("Database error: " + se.getMessage());
                System.out.println("SQL State : " + se.getSQLState());
                se.printStackTrace();
            }

        } finally {
            try { if (smt != null) smt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
            sc.close();
        }
    }
}