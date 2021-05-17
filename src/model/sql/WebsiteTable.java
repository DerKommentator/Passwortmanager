package model.sql;

import java.sql.*;
import java.util.*;

public class WebsiteTable {
    public static void createNewTable(Connection conn, String tableName, LinkedHashMap<String, Datatypes> columns) {
        StringBuilder sqlBody = new StringBuilder();

        for (Map.Entry<String, Datatypes> entry: columns.entrySet()) {
            String columnName = entry.getKey();
            Datatypes datatype = entry.getValue();

            if (columnName.equals("id")) {
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
    public static void insert(Connection conn, String email, String website, String username, String password) {
        String sql = "INSERT INTO websites (email, website, username, password) VALUES (?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, website);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();

            System.out.println("Ein neuer Eintrag wurde hinzugefügt: " + preparedStatement.toString());

        } catch (SQLException e) {
            System.out.println("ERROR - DB insert: " + e.getMessage());
        }
    }

    public static LinkedHashMap<String, String> query(Connection conn, List<String> columnNames) {
        StringBuilder sql = new StringBuilder("SELECT ");
        LinkedHashMap<String, String> queryResults = new LinkedHashMap<String, String>();

        for (String columnName : columnNames) {
            sql.append(columnName).append(",");
        }
        String sqlCutted = sql.substring(0, sql.length() - 1);
        String sqlQuery = sqlCutted + " FROM data;";

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
}