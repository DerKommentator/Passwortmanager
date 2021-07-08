package model.sql;

import javafx.scene.control.Alert;
import model.datenstruktur.AccountInfo;
import utils.Popup;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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

        File database = new File(filePath);
        if (!database.exists()) {
            Popup.showAlert("User", "Datenbank von " + username + " konnte nicht geöffnet werden!", Alert.AlertType.ERROR);
            return null;
        }

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
        if (conn == null) {
            System.out.println("conn is null. Exit");
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR - DB close: " + e.getMessage());
        }
    }
}