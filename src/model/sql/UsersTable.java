package model.sql;

import com.sun.org.apache.xpath.internal.operations.Bool;
import model.datenstruktur.Account;
import org.sqlite.mc.SQLiteMCChacha20Config;

import java.nio.file.Path;
import java.security.AccessControlContext;
import java.sql.*;
import java.util.*;

public class UsersTable {
    public static void createNewTable(Connection conn, String tableName, LinkedHashMap<String, Datatypes> columns) {
        StringBuilder sqlBody = new StringBuilder();

        for (Map.Entry<String, Datatypes> entry: columns.entrySet()) {
            String columnName = entry.getKey();
            Datatypes datatype = entry.getValue();

            if (columnName.equals("username")) {
                sqlBody.append(String.format("%s %s PRIMARY KEY,", columnName, datatype.toString()));
            }
            else {
                sqlBody.append(String.format("%s %s,", columnName, datatype.toString()));
            }
        }
        String sqlBodyCutted = sqlBody.substring(0, sqlBody.length() - 1);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableName, sqlBodyCutted);

        System.out.println(sql);

        /*String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	username text,\n"
                + "	password text\n"
                + ");";*/

        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);

            System.out.println("Neue Tabelle wurde erstellt");

        } catch (SQLException e) {
            System.out.println("ERROR - DB Create Table: " + e.getMessage());
        }
    }

    // TODO: password hashen / verschluesseln
    public static void insert(Connection conn, String username, String email, String password, String dbPath) {
        String sql = "INSERT INTO users (username, email, password, dbPath) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, dbPath);
            preparedStatement.executeUpdate();

            System.out.println("Ein neuer Eintrag wurde hinzugefügt: " + preparedStatement.toString());

        } catch (SQLException e) {
            System.out.println("ERROR - DB insert: " + e.getMessage());
        }
    }

    public static Boolean insertNewUser(Connection conn, Account user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String hashedPassword = user.getPassword();
        String dbPath = user.getDBPath();

        String sql = "INSERT INTO users (username, email, password, dbPath) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, dbPath);
            preparedStatement.executeUpdate();

            System.out.println("Ein neuer Eintrag wurde hinzugefügt: " + preparedStatement.toString());

            return true;
        } catch (SQLException e) {
            System.out.println("ERROR - DB insertNewUser: " + e.getMessage());
        }

        return false;
    }

    public static LinkedHashMap<String, String> query(Connection conn, List<String> columnNames) {
        StringBuilder sql = new StringBuilder("SELECT ");
        LinkedHashMap<String, String> queryResults = new LinkedHashMap<String, String>();

        for (String columnName : columnNames) {
            sql.append(columnName).append(",");
        }
        String sqlCutted = sql.substring(0, sql.length() - 1);
        String sqlQuery = sqlCutted + " FROM users;";

        System.out.println(sqlQuery);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                for (String columnName : columnNames) {
                    queryResults.put(columnName, rs.getString(columnName));
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR - DB query: " + e.getMessage());
        }

        return queryResults;
    }

    public static List<Object> querySqlStatement(Connection conn, String sql) {
        System.out.println(sql);
        List<Object> queryReturn = new ArrayList<Object>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            final int columnCount = resultSetMetaData.getColumnCount();

            while (rs.next()) {
                Object[] values = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    values[i - 1] = rs.getObject(i);
                }
                queryReturn.add(values);
                System.out.println(Arrays.toString(values));
            }

        } catch (SQLException e) {
            System.out.println("ERROR - DB querySqlStatement: " + e.getMessage());
        }

        return queryReturn;
    }


}