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
            conn = DriverManager.getConnection(url, "", "");

            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("DB: The driver name is: " + meta.getDriverName());
                System.out.println("DB: Database URL: " + url);
            }
        } catch (SQLException e) {
            System.out.println("DB: " + e.getMessage());
        }
        return conn;
    }

    public static Connection createPasswordDatabaseConnection(String filePath, String username, String password) {

        String url = String.format("jdbc:sqlite:%s", filePath);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);

            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("DB: The driver name is: " + meta.getDriverName());
                System.out.println("DB: Database URL: " + url);
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