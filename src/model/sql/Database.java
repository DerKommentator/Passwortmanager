package model.sql;

import utils.CryptoException;
import utils.CryptoUtils;

import java.io.File;
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
                System.out.println("DB: Database URL: " + url);
            }
        } catch (SQLException e) {
            System.out.println("DB: " + e.getMessage());
        }
        return conn;
    }

    public static Connection createPasswordDatabaseConnection(String filePath, String username, String password) {
        //File decryptedFile = decryptDatabase(password, new File(filePath));

        String url = String.format("jdbc:sqlite:%s", filePath);
        Connection conn = null;
        try {
            //Connection conn = DriverManager.getConnection(url, SQLiteMCChacha20Config.getDefault().withKey("Key").toProperties());
            //conn = DriverManager.getConnection(url, username, password);
            conn = DriverManager.getConnection(url);

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

    private static File decryptDatabase(String key, File encryptedFile) {
        //File encryptedFile = new File("document.encrypted");
        File decryptedFile = new File("document.decrypted");

        try {
            //CryptoUtils.encrypt(key, filePath, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return decryptedFile;
    }
}