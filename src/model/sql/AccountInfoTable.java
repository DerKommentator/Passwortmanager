package model.sql;

import model.datenstruktur.AccountInfo;

import java.sql.*;
import java.util.*;

public class AccountInfoTable {
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
    public static void insert(Connection conn, String name, String email, String website, String username, String password) {
        String sql = "INSERT INTO data (name, email, website, username, password) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, website);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.executeUpdate();

            System.out.println("Ein neuer Eintrag wurde hinzugefügt: " + preparedStatement.toString());

        } catch (SQLException e) {
            System.out.println("ERROR - DB insert: " + e.getMessage());
        }
    }

    public static List<AccountInfo> query(Connection conn, List<String> columnNames) {
        StringBuilder sql = new StringBuilder("SELECT ");
        List<AccountInfo> returnAccountInfos = new ArrayList<>();

        for (String columnName : columnNames) {
            sql.append(columnName).append(",");
        }
        String sqlCutted = sql.substring(0, sql.length() - 1);
        String sqlQuery = sqlCutted + " FROM data;";

        System.out.println(sqlQuery);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            int row = 1;
            while (rs.next()) {
                //System.out.println("row: " + row);
                LinkedHashMap<String, String> queryResults = new LinkedHashMap<String, String>();
                for (String columnName : columnNames) {
                    //System.out.println("---columnname: " + columnName);
                    //System.out.println("---value: " + rs.getString(columnName));
                    queryResults.put(columnName, rs.getString(columnName));
                }
                returnAccountInfos.add(parseDBEntries(queryResults));
                //System.out.println("===============");
                row++;
            }

        } catch (SQLException e) {
            System.out.println("ERROR - DB query: " + e.getMessage());
        }

        return returnAccountInfos;
    }

    public static AccountInfo parseDBEntries(LinkedHashMap<String, String> queryResult) {
        AccountInfo accountInfo = new AccountInfo();
        for (Map.Entry<String, String> entry: queryResult.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            //System.out.println(columnName);
            //System.out.println(value);
            switch (columnName) {
                case "name":
                    accountInfo.setName(value);
                    break;
                case "email":
                    accountInfo.setEmail(value);
                    break;
                case "website":
                    accountInfo.setWebsite(value);
                    break;
                case "username":
                    accountInfo.setUsername(value);
                    break;
                case "password":
                    accountInfo.setPassword(value);
                    break;
                case "id":
                    accountInfo.setId(Long.parseLong(value));
                    break;
            }
        }
        return accountInfo;
    }

}