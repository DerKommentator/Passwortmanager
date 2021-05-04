package model.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection createDatabaseConnection(String filePath) {
        String url = "jdbc:sqlite:" + filePath;
        Connection conn = null;
        try {
            //Connection conn = DriverManager.getConnection(url, SQLiteMCChacha20Config.getDefault().withKey("Key").toProperties());
            conn = DriverManager.getConnection(url);

            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("DB: The driver name is: " + meta.getDriverName());
                System.out.println("DB: A new database has been created. URL: " + url);
            }
        } catch (SQLException e) {
            System.out.println("DB: " + e.getMessage());
        }
        return conn;
    }

    public static void closeDatabase(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR - DB close: " + e.getMessage());
        }
    }
}