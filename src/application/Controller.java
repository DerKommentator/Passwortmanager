package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import java.net.URL;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.datenstruktur.Account;
import model.datenstruktur.Website;
import model.sql.Database;
import model.sql.TestController;
import model.sql.UsersTable;
import model.sql.WebsiteTable;

import javax.jws.soap.SOAPBinding;

public class Controller implements Initializable {

    @FXML
    private TabPane tabPane_Main;

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
        LinkedHashMap<String, UsersTable.Datatypes> columns = new LinkedHashMap<>();
        columns.put("id", UsersTable.Datatypes.integer);
        columns.put("username", UsersTable.Datatypes.text);
        columns.put("email", UsersTable.Datatypes.text);
        columns.put("password", UsersTable.Datatypes.text);

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
        tabPane_Main.getSelectionModel().select(0);

        // Daten aus Textfelder holen
        String username = txtfld_createAccountUsername.getText();
        String email = txtfld_createAccountEmail.getText();
        String password = txtfld_createAccountPassword.getText();

        if(username != null && email != null && password != null){
            Account erstellterAccount = new Account(username, email , password);
            System.out.println("Der Account wurde erfolgreich erstellt.");
            System.out.println("Username: " + erstellterAccount.getUsername());
            System.out.println("Email: " + erstellterAccount.getEmail());
            System.out.println("Passwort: " + erstellterAccount.getPassword());

            Connection conn = Database.createDatabaseConnection("users.db");
            UsersTable.insert(conn, erstellterAccount.getUsername(), erstellterAccount.getEmail(), erstellterAccount.getPassword());
            Database.closeDatabase(conn);

        } else {
            System.out.println("Der Account konnte nicht erstellt werden, da die Textfelder ausgefüllt werden müssen.");
        }
    }

    // Login
    @FXML
    private void loginIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(2);


        System.out.println("Login erfolgreich.");
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
        LinkedHashMap<String, WebsiteTable.Datatypes> columns = new LinkedHashMap<>();
        columns.put("id", WebsiteTable.Datatypes.integer);
        columns.put("email", WebsiteTable.Datatypes.text);
        columns.put("website", WebsiteTable.Datatypes.text);
        columns.put("username", WebsiteTable.Datatypes.text);
        columns.put("password", WebsiteTable.Datatypes.text);

        Connection conn = Database.createDatabaseConnection("users.db");
        WebsiteTable.createNewTable(conn, "websites", columns);
        Database.closeDatabase(conn);

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
}
