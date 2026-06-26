package callimus.firstbankk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_PATH = "C:/Data/FirstBankUganda.accdb"; // Configure this path to your file location [cite: 54]
    private static final String CONNECTION_URL = "jdbc:ucanaccess://" + DB_PATH;

    public static void saveAccountRecord(String generatedAccNum, Account acc) throws SQLException {
        String sql = "INSERT INTO AccountsTable (AccountNumber, FirstName, LastName, NIN, Email, PhoneNumber, AccountType, Branch, Deposit, DOB) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Try-with-resources handles resource recycling safely
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, generatedAccNum);
            pstmt.setString(2, acc.firstName);
            pstmt.setString(3, acc.lastName);
            pstmt.setString(4, acc.nin);
            pstmt.setString(5, acc.email);
            pstmt.setString(6, acc.phoneNumber);
            pstmt.setString(7, acc.getAccountTypeName());
            pstmt.setString(8, acc.branch);
            pstmt.setDouble(9, acc.openingDeposit);
            pstmt.setDate(10, java.sql.Date.valueOf(acc.dob)); // Map LocalDate objects to relational timestamps

            pstmt.executeUpdate();
        }
    }
}