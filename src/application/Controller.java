package application;

import com.sun.istack.internal.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.UnaryOperator;

import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.datenstruktur.Account;
import model.datenstruktur.AccountInfo;
import model.sql.*;
import utils.BCrypt;
import utils.Popup;

import javax.activation.CommandObject;

public class Controller implements Initializable {

    @FXML
    private TabPane tabPane_Main;

    @FXML
    private TextField field_username;

    @FXML
    private PasswordField field_password;
    @FXML
    private TextField txtfld_loginPasswordText;

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
    private TextField txtfld_createAccountPasswordText;

    @FXML
    private TextField txtfld_createAccountEmail;

    @FXML
    private PasswordField txtfld_informationPassword;
    @FXML
    private TextField txtfld_informationPasswordText;

    @FXML
    private TextField txtfld_informationName;

    @FXML
    private TextField txtfld_informationWebsite;

    @FXML
    private TextField txtfld_informationEmail;

    @FXML
    private PasswordField txtfld_dashboardPassword;
    @FXML
    private TextField txtfld_dashboardPasswordText;

    @FXML
    private TextField txtfld_dashboardName;

    @FXML
    private TextField txtfld_dashboardWebsite;

    @FXML
    private TextField txtfld_dashboardEmail;

    //@FXML
    //private Button btn_showLoginPassword;
    @FXML
    private CheckBox chkbx_showLoginPassword;
    //@FXML
    //private Button btn_showCreateAccPassword;
    @FXML
    private CheckBox chkbx_showCreateAccPassword;
    //@FXML
    //private Button btn_showDashbordPassword;
    @FXML
    private CheckBox chkbx_showDashbordPassword;
    //@FXML
    //private Button btn_showAddInfoPassword;
    @FXML
    private CheckBox chkbx_showAddInfoPassword;
    //@FXML
    //private Button btn_showEditInfoPassword;
    @FXML
    private CheckBox chkbx_showEditInfoPassword;
    @FXML
    private CheckBox chkbx_showSettingsOldPassword;
    @FXML
    private CheckBox chkbx_showSettingsNewPassword;


    @FXML
    private TextField filterField;

    @FXML
    private PasswordField txtfld_editPassword;
    @FXML
    private TextField txtfld_editPasswordText;

    @FXML
    private TableView<AccountInfo> tableView;

    @FXML
    private TableColumn<AccountInfo, String> tableView_name;

    @FXML
    private TableColumn<AccountInfo, String> tableView_username;

    @FXML
    private TableColumn<AccountInfo, String> tableView_email;

    @FXML
    private TableColumn<AccountInfo, String> tableView_website;

    @FXML
    private TextField txtfld_informationUsername;


    @FXML
    private TextField txtfld_editName;
    @FXML
    private TextField txtfld_editEmail;
    @FXML
    private TextField txtfld_editWebsite;
    @FXML
    private TextField txtfld_editUsername;
    //@FXML
    //private TextField txtfld_editPassword;

    @FXML
    private Button btn_changeToSettingsTab;
    @FXML
    private Button btn_changeSettings;
    @FXML
    private TextField txtfld_settingsEmail;
    @FXML
    private PasswordField txtfld_settingsOldPw;
    @FXML
    private TextField txtfld_settingsOldPwText;
    @FXML
    private PasswordField txtfld_settingsNewPw;
    @FXML
    private TextField txtfld_settingsNewPwText;
    @FXML
    private PasswordField txtfld_settingsValNewPw;
    @FXML
    private TextField txtfld_settingsValNewPwText;


    @FXML
    private Button btn_dashboardDelete;

    private final ObservableList<AccountInfo> dataList = FXCollections.observableArrayList();
    private Account user = new Account();

    private AccountInfo accountInfo = new AccountInfo();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_createAccount.setOnAction(this::createAccountisClicked);
        btn_cancelCreateAccount.setOnAction(this::cancelCreateAccountIsClicked);
        btn_createAccountInTab.setOnAction(this::createAccountInTabIsClicked);
        btn_logout.setOnAction(this::logoutIsClicked);
        btn_login.setOnAction(this::loginIsClicked);
        btn_createNewPassword.setOnAction(this::createNewPasswordIsClicked);
        btn_addPassword.setOnAction(this::addPasswordIsClicked);
        btn_editInfo.setOnAction(e -> editInfoIsClicked(accountInfo));
        btn_changeInfo.setOnAction(e -> updateEntry());

        //btn_showDashbordPassword.setOnAction(e -> showPassword(txtfld_dashboardPassword));
        this.toggleVisiblePassword(chkbx_showDashbordPassword, txtfld_dashboardPasswordText, txtfld_dashboardPassword);
        this.toggleVisiblePassword(chkbx_showDashbordPassword, txtfld_dashboardPasswordText, txtfld_dashboardPassword);
        this.toggleVisiblePassword(chkbx_showLoginPassword, txtfld_loginPasswordText, field_password);
        this.toggleVisiblePassword(chkbx_showCreateAccPassword, txtfld_createAccountPasswordText, txtfld_createAccountPassword);
        this.toggleVisiblePassword(chkbx_showAddInfoPassword, txtfld_informationPasswordText, txtfld_informationPassword);
        this.toggleVisiblePassword(chkbx_showEditInfoPassword, txtfld_editPasswordText, txtfld_editPassword);

        this.toggleVisiblePassword(chkbx_showSettingsOldPassword, txtfld_settingsOldPwText, txtfld_settingsOldPw);
        this.toggleTwoVisiblePassword(chkbx_showSettingsNewPassword, txtfld_settingsNewPwText, txtfld_settingsNewPw, txtfld_settingsValNewPwText, txtfld_settingsValNewPw);

        chkbx_showDashbordPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showDashbordPassword, txtfld_dashboardPasswordText, txtfld_dashboardPassword));
        chkbx_showLoginPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showLoginPassword, txtfld_loginPasswordText, field_password));
        chkbx_showCreateAccPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showCreateAccPassword, txtfld_createAccountPasswordText, txtfld_createAccountPassword));
        chkbx_showAddInfoPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showAddInfoPassword, txtfld_informationPasswordText, txtfld_informationPassword));
        chkbx_showEditInfoPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showEditInfoPassword, txtfld_editPasswordText, txtfld_editPassword));

        chkbx_showSettingsOldPassword.setOnAction(e -> toggleVisiblePassword(chkbx_showSettingsOldPassword, txtfld_settingsOldPwText, txtfld_settingsOldPw));
        chkbx_showSettingsNewPassword.setOnAction(e -> toggleTwoVisiblePassword(chkbx_showSettingsNewPassword, txtfld_settingsNewPwText, txtfld_settingsNewPw, txtfld_settingsValNewPwText, txtfld_settingsValNewPw));


        btn_dashboardDelete.setOnAction(e -> deleteAccountInfoEntry(accountInfo));

        btn_changeToSettingsTab.setOnAction(e -> changeToSettingsTab());
        btn_changeSettings.setOnAction(e -> updateSettings());


        tableView_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableView_username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        tableView_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tableView_website.setCellValueFactory(new PropertyValueFactory<>("Website"));

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

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            String generatedSecurePasswordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            //System.out.println("=============="+generatedSecurePasswordHash);
            String usersPasswordDBPath = username + ".db";
            Account erstellterAccount = new Account(username, email, generatedSecurePasswordHash, usersPasswordDBPath);
            erstellterAccount.setDecryptedPassword(password);
            if (isRegistered(erstellterAccount)) {
                tabPane_Main.getSelectionModel().select(0);
            } else {
                String errorHeader = "Registrierung - Datenbank";
                String errorContent = "Der Account konnte nicht erstellt werden.";
                Popup.showAlert(errorHeader, errorContent, Alert.AlertType.ERROR);
                System.out.println(errorContent);
            }

        } else {
            String errorHeader = "Registrierung";
            String errorContent = "Der Account konnte nicht erstellt werden, da die Textfelder ausgefüllt werden müssen.";
            Popup.showAlert(errorHeader, errorContent, Alert.AlertType.ERROR);
            System.out.println(errorContent);
        }
    }

    public Boolean isRegistered(Account user) {
        Connection conn = Database.createDatabaseConnection("users.db");
        if (UsersTable.insertNewUser(conn, user)) {
            String alertContent = "Der Account wurde erfolgreich erstellt.";
            System.out.println(alertContent);
            Database.closeDatabase(conn);

            //TODO: check if username is users - conflicts with users.db
            //TODO: autoincrement in users.db for id -> FIXED: use rowid instead of id column

            try {
                Connection websiteConn = Database.createPasswordDatabaseConnection(user.getDBPath(), user.getUsername(), user.getDecryptedPassword());
                LinkedHashMap<String, Datatypes> columns = new LinkedHashMap<>();
                columns.put("id", Datatypes.integer);
                columns.put("name", Datatypes.text);
                columns.put("email", Datatypes.text);
                columns.put("website", Datatypes.text);
                columns.put("username", Datatypes.text);
                columns.put("password", Datatypes.text);
                AccountInfoTable.createNewTable(websiteConn, "data", columns);
            } catch (Exception e) {
                String errorHeader = "Registrierung";
                String errorContent = "Der Account konnte nicht erstellt werden, da nicht alle Textfelder ausgefüllt wurden.";
                System.out.println(errorContent);
                Popup.showAlert(errorHeader, errorContent, Alert.AlertType.ERROR);

                Popup.showException(e, "Registration", "Registration - CreatePasswordDB");
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
                user.setDBPath(username + ".db");
                user.setUsername(username);
                List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath(), null);

                if (!dataList.isEmpty()) {
                    //TODO: verbessern, weil uneffizient
                    dataList.remove(0, dataList.size());
                }

                dataList.addAll(queryResults);
                searchFilter();
            } else {
                String alertHeader = "Login";
                String alertContent = "Login fehlgeschlagen";
                System.out.println(alertContent);
                Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
            }
        } else {
            String alertHeader = "Login";
            String alertContent = "Username oder Password sind leer.";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
        }
    }

    public boolean isLogin(String username, String password) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String query = "SELECT rowid, username, email, password, dbPath FROM users WHERE username = ?";
        Connection conn = null;

        try {
            conn = Database.createDatabaseConnection("users.db");
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            user = UsersTable.parseDBResultSetEntries(resultSet);
            user.setDecryptedPassword(password);
            System.out.println(user.getDBPath());
            /*while (resultSet.next()) {
                userPassword = resultSet.getString("password");
                //System.out.println(".........."+userPassword);
            }*/
            return BCrypt.checkpw(password, user.getPassword());

        } catch (Exception e) {
            Popup.showException(e, "Login", "Login Error");
            //System.out.println("Login Error: " + e.getMessage());
            return false;
        } finally {
            assert conn != null;
            Database.closeDatabase(conn);
        }
    }

    // Zurueck zum Dashboard
    @FXML
    private void backToDashboardIsClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(2);
        System.out.println("Zurück zum Dashboard.");
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
        String username = txtfld_informationUsername.getText();

        if (email != null && website != null && name != null && password != null && username != null) {
            AccountInfo erstellteAccountInfo = new AccountInfo(email, website, name, password, username);
            System.out.println("Die Website wurde erfolgreich hinzugefügt.");
            System.out.println("Email: " + erstellteAccountInfo.getEmail());
            System.out.println("Website: " + erstellteAccountInfo.getWebsite());
            System.out.println("Name: " + erstellteAccountInfo.getName());
            System.out.println("Passwort: " + erstellteAccountInfo.getPassword());

            Connection conn = Database.createPasswordDatabaseConnection(user.getDBPath(), user.getUsername(), user.getDecryptedPassword());
            AccountInfoTable.insert(conn, erstellteAccountInfo.getName(), erstellteAccountInfo.getEmail(), erstellteAccountInfo.getWebsite(), erstellteAccountInfo.getUsername(), erstellteAccountInfo.getPassword());
            Database.closeDatabase(conn);

            //TODO: verbessern, weil uneffizient
            List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath(), null);
            dataList.remove(0, dataList.size());
            dataList.addAll(queryResults);

        } else {
            String alertHeader = "Daten hinzufügen";
            String alertContent = "Die Website konnte nicht hinzugefügt werden, da alle Textfelder ausgefüllt werden müssen.";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
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
    private void editInfoIsClicked(AccountInfo accountInfo) {
        if (accountInfo.getName() != null) {

            txtfld_editName.setText(accountInfo.getName());
            txtfld_editEmail.setText(accountInfo.getEmail());
            txtfld_editWebsite.setText(accountInfo.getWebsite());
            txtfld_editUsername.setText(accountInfo.getUsername());
            txtfld_editPassword.setText(accountInfo.getPassword());

            tabPane_Main.getSelectionModel().select(4);
            System.out.println("Der Eintrag kann bearbeitet werden.");
        } else {
            String alertHeader = "Edit";
            String alertContent = "Kein Eintrag ausgewählt";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteAccountInfoEntry(AccountInfo accountInfo) {
        if (accountInfo.getName() != null) {

            System.out.println("Lösche den Eintrag: " + accountInfo.getName());

            Connection conn = Database.createPasswordDatabaseConnection(user.getDBPath(), user.getUsername(), user.getDecryptedPassword());
            boolean status = AccountInfoTable.delete(conn, accountInfo.getId());

            //TODO: verbessern, weil uneffizient
            List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath(), conn);
            dataList.remove(0, dataList.size());
            dataList.addAll(queryResults);
            Database.closeDatabase(conn);
            System.out.println(accountInfo.getId());
            if (status) {
                System.out.println("Löschen erfolgreich");
            } else {
                String alertHeader = "Delete";
                String alertContent = "Löschen fehlgeschlagen";
                System.out.println(alertContent);
                Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
            }
        } else {
            String alertHeader = "Edit";
            String alertContent = "Bitte wähle einen Eintrag aus";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.WARNING);
        }
    }


    // vorhandes Passwort bearbeiten
    @FXML
    private void updateEntry() {
        accountInfo.setName(txtfld_editName.getText());
        accountInfo.setEmail(txtfld_editEmail.getText());
        accountInfo.setWebsite(txtfld_editWebsite.getText());
        accountInfo.setUsername(txtfld_editUsername.getText());
        accountInfo.setPassword(txtfld_editPassword.getText());

        Connection conn = Database.createPasswordDatabaseConnection(user.getDBPath(), user.getUsername(), user.getDecryptedPassword());
        boolean status = AccountInfoTable.update(
                conn,
                accountInfo.getName(),
                accountInfo.getEmail(),
                accountInfo.getWebsite(),
                accountInfo.getUsername(),
                accountInfo.getPassword(),
                accountInfo.getId()
        );

        if (status) {
            System.out.println("Update erfolgreich");
        } else {
            String alertHeader = "Update";
            String alertContent = "Update fehlgeschalgen";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
        }

        tabPane_Main.getSelectionModel().select(2);
    }

    // Wechsel auf den Informations Tab
    //TODO
    @FXML
    private void changeToInfoTab() {
        System.out.println("Lade Daten aus Datenbank");

        tableView.setRowFactory(tv -> {
            TableRow<AccountInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    AccountInfo clickedRow = row.getItem();
                    accountInfo = clickedRow;
                    txtfld_dashboardPassword.setText(clickedRow.getPassword());
                    //txtfld_dashboardPasswordText.setText(clickedRow.getPassword());
                    txtfld_dashboardName.setText(clickedRow.getEmail());
                    txtfld_dashboardWebsite.setText(clickedRow.getWebsite());
                    txtfld_dashboardEmail.setText(clickedRow.getEmail());
                    //System.out.println(clickedRow.getId());
                }
            });
            return row;
        });
    }


    public List<AccountInfo> getDatabaseEntries(String dbPath, @Nullable Connection conn) {
        if (!dbPath.isEmpty()) {
            conn = Database.createPasswordDatabaseConnection(dbPath, user.getUsername(), user.getDecryptedPassword());
            //conn = Database.createDatabaseConnection(dbPath);
        }

        List<String> queryColumns = new ArrayList<String>();
        queryColumns.add("id");
        queryColumns.add("name");
        queryColumns.add("email");
        queryColumns.add("website");
        queryColumns.add("username");
        queryColumns.add("password");

        List<AccountInfo> accountsFromDB = AccountInfoTable.query(conn, queryColumns);

        /*for (Map.Entry<String, String> entry: queryResult.entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            System.out.println(columnName);
            System.out.println(value);
            if (columnName.equals("name")) {
                //listView_dashboardListEntries.getItems().add(value);

            }
        }*/

        Database.closeDatabase(conn);

        return accountsFromDB;
    }

    public void searchFilter() {
        FilteredList<AccountInfo> filteredData = new FilteredList<>(dataList, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(accountInfo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (accountInfo.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (accountInfo.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (accountInfo.getWebsite().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (accountInfo.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<AccountInfo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }


    // Settings
    @FXML
    private void changeToSettingsTab() {
        txtfld_settingsEmail.setText(user.getEmail());

        tabPane_Main.getSelectionModel().select(5);
        System.out.println("Einstellungen");
    }

    private void updateSettings() {
        String oldPw = txtfld_settingsOldPw.getText();
        String newPw = txtfld_settingsNewPw.getText();
        String newValPw = txtfld_settingsValNewPw.getText();
        String newEmail = txtfld_settingsEmail.getText();

        if (isLogin(user.getUsername(), oldPw)) {
            System.out.println("Passwort stimmt");

            user.setEmail(newEmail);

            if(!newPw.isEmpty()) {
                if (newPw.equals(newValPw)) {
                    String generatedSecurePasswordHash = BCrypt.hashpw(newPw, BCrypt.gensalt(12));

                    //System.out.println("Alt: " + user.getPassword());
                    user.setPassword(generatedSecurePasswordHash);
                    //System.out.println("Neu: " + user.getPassword());
                } else {
                    String alertHeader = "Settings";
                    String alertContent = "Passwörter stimmen nicht überein";
                    System.out.println(alertContent);
                    Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
                }
            }

            Connection conn = Database.createDatabaseConnection("users.db");
            boolean status = UsersTable.update(conn, user.getUsername(), user.getEmail(), user.getPassword(), user.getDBPath(), user.getId());
            Database.closeDatabase(conn);

            if (status) {
                System.out.println("Update erfolgreich");
                tabPane_Main.getSelectionModel().select(2);
            } else {
                String alertHeader = "Settings";
                String alertContent = "Update fehlgeschlagen";
                System.out.println(alertContent);
                Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
            }
        } else {
            String alertHeader = "Settings";
            String alertContent = "Passwort stimmt nicht";
            System.out.println(alertContent);
            Popup.showAlert(alertHeader, alertContent, Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void toggleVisiblePassword(CheckBox checkBox, TextField textField, PasswordField passwordField) {
        if (checkBox.isSelected()) {
            textField.setText(passwordField.getText());
            textField.setVisible(true);
            passwordField.setVisible(false);
            return;
        }
        passwordField.setText(textField.getText());
        passwordField.setVisible(true);
        textField.setVisible(false);
    }

    @FXML
    public void toggleTwoVisiblePassword(CheckBox checkBox, TextField textField, PasswordField passwordField, TextField textField2, PasswordField passwordField2) {
        if (checkBox.isSelected()) {
            textField.setText(passwordField.getText());
            textField.setVisible(true);
            passwordField.setVisible(false);

            textField2.setText(passwordField2.getText());
            textField2.setVisible(true);
            passwordField2.setVisible(false);
            return;
        }
        passwordField.setText(textField.getText());
        passwordField.setVisible(true);
        textField.setVisible(false);

        passwordField2.setText(textField2.getText());
        passwordField2.setVisible(true);
        textField2.setVisible(false);
    }

}
