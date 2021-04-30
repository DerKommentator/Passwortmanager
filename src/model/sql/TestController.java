package model.sql;

import model.sql.Database;
import model.sql.UsersTable;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestController {

    public void test(ActionEvent actionEvent) {
        LinkedHashMap<String, UsersTable.Datatypes> columns = new LinkedHashMap<>();
        columns.put("id", UsersTable.Datatypes.integer);
        columns.put("username", UsersTable.Datatypes.text);
        columns.put("password", UsersTable.Datatypes.text);

        List<String> queryColumns = new ArrayList<String>();
        queryColumns.add("id");
        queryColumns.add("username");
        queryColumns.add("password");

        Connection conn = Database.createNewDatabase("I:\\IntelliJ_IDEA\\Projects\\Passwortmanager\\src\\application\\test.db");
        UsersTable.createNewTable(conn, "users", columns);
        UsersTable.insert(conn, "testuser", "123456");
        LinkedHashMap<String, String> queryResult = UsersTable.query(conn, queryColumns);
        for (Map.Entry<String, String> entry: queryResult.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            System.out.println(columnName);
            System.out.println(value);
        }
    }
}