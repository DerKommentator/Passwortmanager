package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javafx.fxml.Initializable;
import model.datenstruktur.Account;
import model.datenstruktur.Website;
import model.sql.*;
import utils.BCrypt;

import javax.jws.soap.SOAPBinding;

public class Controller implements Initializable {

    @FXML
    private TabPane tabPane_Main;

    @FXML
    private TextField field_username;

    @FXML
    private PasswordField field_password;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_cancelCreateAccount;

    @FXML
    private Button btn_createAccountInTab;

    @FXML
    private Button btn_createAccount;

    @FXML
    private Button btn_createNewPassword;

    @FXML
    private Button btn_changeInfo;

    @FXML
    private Button btn_editInfo;

    @FXML
    private Button btn_addPassword;

    @FXML
    private ListView<String> listView_dashboardListEntries;

    @FXML
    private TextField txtfld_createAccountUsername;

    @FXML
    private PasswordField txtfld_createAccountPassword;

    @FXML
    private TextField txtfld_createAccountEmail;

    @FXML
    private TextField txtfld_informationPassword;

    @FXML
    private TextField txtfld_informationName;

    @FXML
    private TextField txtfld_informationWebsite;

    @FXML
    private TextField txtfld_informationEmail;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_createAccount.setOnAction(this::createAccountisClicked);
        btn_cancelCreateAccount.setOnAction(this::cancelCreateAccountIsClicked);
        btn_createAccountInTab.setOnAction(this::createAccountInTabIsClicked);
        btn_logout.setOnAction(this::logoutIsClicked);
        btn_login.setOnAction(this::loginIsClicked);
        btn_createNewPassword.setOnAction(this::createNewPasswordIsClicked);
        btn_addPassword.setOnAction(this::addPasswordIsClicked);
        btn_editInfo.setOnAction(this::editInfoIsClicked);
        btn_changeInfo.setOnAction(this::changeInfoIsClicked);
    }

    // Account-Erstellungs Button beim Login
    @FXML
    private void createAccountisClicked(javafx.event.ActionEvent actionEvent) {
        LinkedHashMap<String, Datatypes> columns = new LinkedHashMap<>();
        columns.put("username", Datatypes.text);
        columns.put("email", Datatypes.text);
        columns.put("password", Datatypes.text);
        columns.put("dbPath", Datatypes.text);

        Connection conn = Database.createDatabaseConnection("users.db");
        UsersTable.createNewTable(conn, "users", columns);
        Database.closeDatabase(conn);

        tabPane_Main.getSelectionModel().select(1);
        System.out.println("Account kann nun erstellt werden.");
    }

    // Abbruch beim Account erstellen
    @FXML
    private void cancelCreateAccountIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(0);
        System.out.println("Account-Erstellung abgebrochen.");
    }

    // Account-Erstellung
    @FXML
    private void createAccountInTabIsClicked(javafx.event.ActionEvent actionEvent) {
        // Daten aus Textfelder holen
        String username = txtfld_createAccountUsername.getText();
        String email = txtfld_createAccountEmail.getText();
        String password = txtfld_createAccountPassword.getText();

        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            String generatedSecurePasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            String usersPasswordDBPath = username + ".db";
            Account erstellterAccount = new Account(username, email , generatedSecurePasswordHash, usersPasswordDBPath);

            if (isRegistered(erstellterAccount)) {
                tabPane_Main.getSelectionModel().select(0);
            } else {
                System.out.println("Der Account konnte nicht erstellt werden.");
            }

        } else {
            System.out.println("Der Account konnte nicht erstellt werden, da die Textfelder ausgefüllt werden müssen.");
        }
    }

    public Boolean isRegistered(Account user) {
        Connection conn = Database.createDatabaseConnection("users.db");
        if (UsersTable.insertNewUser(conn, user)) {
            System.out.println("Der Account wurde erfolgreich erstellt.");
            Database.closeDatabase(conn);

            //TODO: check if username is users - conflicts with users.db
            //TODO: autoincrement in users.db for id -> FIXED: use rowid instead of id column

            try {
                Connection websiteConn = Database.createDatabaseConnection(user.getDBPath());
                LinkedHashMap<String, Datatypes> columns = new LinkedHashMap<>();
                columns.put("id", Datatypes.integer);
                columns.put("name", Datatypes.text);
                columns.put("email", Datatypes.text);
                columns.put("website", Datatypes.text);
                columns.put("username", Datatypes.text);
                columns.put("password", Datatypes.text);
                WebsiteTable.createNewTable(websiteConn, "data", columns);
            } catch (Exception e) {
                System.out.println("Registration - CreateWebsiteDB: " + e.getMessage());
                return false;
            }

            return true;
        }
        System.out.println("Der Account konnte nicht erstellt werden, da die Textfelder ausgefüllt werden müssen.");
        return false;
    }

    // Login
    @FXML
    private void loginIsClicked(javafx.event.ActionEvent actionEvent) {
        String username = field_username.getText();
        String password = field_password.getText();
        if (!username.isEmpty() && !password.isEmpty()) {
            if (isLogin(username, password)) {
                System.out.println("Login erfolgreich.");
                tabPane_Main.getSelectionModel().select(2);
                getDatabaseEntries(username + ".db");
            }
            else {
                System.out.println("Login fehlgeschlagen.");
            }
        } else {
            System.out.println("Username oder Password sind leer.");
        }
    }

    public boolean isLogin(String username, String password) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String generatedSecurePasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        System.out.println(generatedSecurePasswordHash);

        String query = "SELECT password FROM users WHERE username = ?";
        Connection conn = null;

        try {
            conn = Database.createDatabaseConnection("users.db");
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, userPassword);
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        } finally {
            assert conn != null;
            Database.closeDatabase(conn);
        }
        return false;
    }


    // Logut
    @FXML
    private void logoutIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(0);
        System.out.println("Logout.");
    }

    // neues Passwort hinzufügen
    @FXML
    private void createNewPasswordIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(2);

        // Daten aus Textfelder holen
        String email = txtfld_informationEmail.getText();
        String website = txtfld_informationWebsite.getText();
        String name = txtfld_informationName.getText();
        String password = txtfld_informationPassword.getText();

        if(email != null && website != null && name != null && password != null){
            Website erstellteWebsite = new Website(email, website , name, password);
            System.out.println("Die Website wurde erfolgreich hinzugefügt.");
            System.out.println("Email: " + erstellteWebsite.getEmail());
            System.out.println("Website: " + erstellteWebsite.getWebsite());
            System.out.println("Name: " + erstellteWebsite.getName());
            System.out.println("Passwort: " + erstellteWebsite.getPassword());

            Connection conn = Database.createDatabaseConnection("users.db");
            WebsiteTable.insert(conn, erstellteWebsite.getEmail(), erstellteWebsite.getWebsite(), erstellteWebsite.getName(), erstellteWebsite.getPassword());
            Database.closeDatabase(conn);

        } else {
            System.out.println("Die Website konnte nicht hinzugefügt werden, da die Textfelder ausgefüllt werden müssen.");
        }
    }

    // Passwort in Info-Tab hinzufügen (Ohne Hinzufügen)
    @FXML
    private void addPasswordIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(3);
        System.out.println("Neues Passwort kann hinzugefügt werden.");
    }

    // Passwort in Info-Tab bearbeiten
    @FXML
    private void editInfoIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(4);
        System.out.println("vorhandenes Passwort kann bearbeitet werden.");
    }

    // vorhandes Passwort bearbeiten
    @FXML
    private void changeInfoIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(2);
        System.out.println("Passwort wurde geändert und bearbeitet.");
    }

    // Wechsel auf den Informations Tab
    //TODO
    @FXML
    private void changeToInfoTab() {
        System.out.println("Lade Daten aus Datenbank");

    }

    public void getDatabaseEntries(String dbPath) {
        List<String> queryColumns = new ArrayList<String>();
        queryColumns.add("id");
        queryColumns.add("name");
        queryColumns.add("email");
        queryColumns.add("website");
        queryColumns.add("username");
        queryColumns.add("password");

        Connection conn = Database.createDatabaseConnection(dbPath);
        LinkedHashMap<String, String> queryResult = WebsiteTable.query(conn, queryColumns);
        for (Map.Entry<String, String> entry: queryResult.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            System.out.println(columnName);
            System.out.println(value);
            if (columnName.equals("name")) {
                listView_dashboardListEntries.getItems().add(value);
            }
        }

        Database.closeDatabase(conn);
    }
}
