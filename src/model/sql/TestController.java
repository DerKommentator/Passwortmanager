package model.sql;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestController {

    public void test(ActionEvent actionEvent) {
        LinkedHashMap<String, Datatypes> columns = new LinkedHashMap<>();
        columns.put("id", Datatypes.integer);
        columns.put("username", Datatypes.text);
        columns.put("password", Datatypes.text);

        List<String> queryColumns = new ArrayList<String>();
        queryColumns.add("id");
        queryColumns.add("username");
        queryColumns.add("password");

        Connection conn = Database.createDatabaseConnection("users.db");
        UsersTable.createNewTable(conn, "users", columns);
        //UsersTable.insert(conn, "testuser", "test@gmx.de", "123456");
        /*LinkedHashMap<String, String> queryResult = UsersTable.query(conn, queryColumns);
        for (Map.Entry<String, String> entry: queryResult.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            System.out.println(columnName);
            System.out.println(value);
        }*/
    }
}