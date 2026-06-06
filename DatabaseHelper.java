import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    // This creates a file named "hospital.db" in your folder automatically
    private static final String URL = "jdbc:sqlite:hospital.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // This creates the table for you automatically
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS patients ("
                + "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "patient_name TEXT NOT NULL, "
                + "age INTEGER NOT NULL, "
                + "ailment TEXT NOT NULL, "
                + "arrival_time DATETIME DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database ready.");
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }
}
