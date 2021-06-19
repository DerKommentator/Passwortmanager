package application;

import com.sun.istack.internal.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
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
import model.datenstruktur.Account;
import model.datenstruktur.AccountInfo;
import model.sql.*;
import utils.BCrypt;

import javax.activation.CommandObject;

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

    @FXML
    private PasswordField txtfld_dashboardPassword;

    @FXML
    private TextField txtfld_dashboardName;

    @FXML
    private TextField txtfld_dashboardWebsite;

    @FXML
    private TextField txtfld_dashboardEmail;

    @FXML
    private Button btn_showLoginPassword;
    @FXML
    private Button btn_showCreateAccPassword;
    @FXML
    private Button btn_showDashbordPassword;
    @FXML
    private Button btn_showAddInfoPassword;
    @FXML
    private Button btn_showEditInfoPassword;


    @FXML
    private TextField filterField;

    @FXML
    private TextField txtfld_editPassword;

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
    private PasswordField txtfld_settingsNewPw;
    @FXML
    private PasswordField txtfld_settingsValNewPw;


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

        btn_showDashbordPassword.setOnAction(e -> showPassword(txtfld_dashboardPassword));
        btn_showLoginPassword.setOnAction(e -> showPassword(field_password));
        btn_showCreateAccPassword.setOnAction(e -> showPassword(txtfld_createAccountPassword));
        btn_showAddInfoPassword.setOnAction(e -> showPassword(txtfld_informationPassword));
        btn_showEditInfoPassword.setOnAction(e -> showPassword(txtfld_editPassword));

        btn_dashboardDelete.setOnAction(e -> deleteAccountInfoEntry(accountInfo));

        btn_changeToSettingsTab.setOnAction(e -> changeToSettingsTab());
        btn_changeSettings.setOnAction(e -> updateSettings());


        tableView_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableView_username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        tableView_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tableView_website.setCellValueFactory(new PropertyValueFactory<>("Website"));

        /*Website web1 = new Website("rte@ge.e", "www.ge.de", "Test1", "1234", "lul");
        Website web2 = new Website("rte@ge2.e", "www.ge2.de", "Test2", "12345", "lul2");
        Website web3 = new Website("rte@ge3.e", "www.ge3.de", "Test3", "123456", "lul3");

        dataList.addAll(web1, web2, web3);*/

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
                AccountInfoTable.createNewTable(websiteConn, "data", columns);
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
                System.out.println("Login fehlgeschlagen.");
            }
        } else {
            System.out.println("Username oder Password sind leer.");
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
            /*while (resultSet.next()) {
                userPassword = resultSet.getString("password");
                //System.out.println(".........."+userPassword);
            }*/
            return BCrypt.checkpw(password, user.getPassword());

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
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

            Connection conn = Database.createDatabaseConnection(user.getDBPath());
            AccountInfoTable.insert(conn, erstellteAccountInfo.getName(), erstellteAccountInfo.getEmail(), erstellteAccountInfo.getWebsite(), erstellteAccountInfo.getUsername(), erstellteAccountInfo.getPassword());
            Database.closeDatabase(conn);

            //TODO: verbessern, weil uneffizient
            List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath(), null);
            dataList.remove(0, dataList.size());
            dataList.addAll(queryResults);

        } else {
            System.out.println("Die Website konnte nicht hinzugefügt werden, da alle Textfelder ausgefüllt werden müssen.");
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
        if (!accountInfo.isEmpty()) {
            txtfld_editName.setText(accountInfo.getName());
            txtfld_editEmail.setText(accountInfo.getEmail());
            txtfld_editWebsite.setText(accountInfo.getWebsite());
            txtfld_editUsername.setText(accountInfo.getUsername());
            txtfld_editPassword.setText(accountInfo.getPassword());

            tabPane_Main.getSelectionModel().select(4);
            System.out.println("Ändern des Eintrags kann bearbeitet werden.");
        } else {
            System.out.println("Error: Kein Eintrag ausgewählt");
        }
    }

    @FXML
    private void deleteAccountInfoEntry(AccountInfo accountInfo) {
        System.out.println("Lösche den Eintrag: " + accountInfo.getName());

        Connection conn = Database.createDatabaseConnection(user.getDBPath());
        boolean status = AccountInfoTable.delete(conn, accountInfo.getId());

        //TODO: verbessern, weil uneffizient
        List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath(), conn);
        dataList.remove(0, dataList.size());
        dataList.addAll(queryResults);
        Database.closeDatabase(conn);

        if (status) {
            System.out.println("Löschen erfolgreich");
        } else {
            System.out.println("Löschen fehlgeschlagen");
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

        Connection conn = Database.createDatabaseConnection(user.getDBPath());
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
            System.out.println("Update fehlgeschlagen");
        }

        tabPane_Main.getSelectionModel().select(2);
    }

    @FXML
    private void showPassword(TextField txtfld) {
        System.out.println("test: " + txtfld.getAccessibleRole());
        if (txtfld.getAccessibleRole() == AccessibleRole.PASSWORD_FIELD) {
            txtfld.setAccessibleRole(AccessibleRole.TEXT_FIELD);
        } else {
            txtfld.setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
        }
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
            conn = Database.createDatabaseConnection(dbPath);
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

                    user.setPassword(generatedSecurePasswordHash);
                } else {
                    System.out.println("Passwörter stimmen nicht überein");
                }
            }

            Connection conn = Database.createDatabaseConnection("users.db");
            boolean status = UsersTable.update(conn, user.getUsername(), user.getEmail(), user.getPassword(), user.getDBPath(), user.getId());
            Database.closeDatabase(conn);

            if (status) {
                System.out.println("Update erfolgreich");
                tabPane_Main.getSelectionModel().select(2);
            } else {
                System.out.println("Update fehlgeschlagen");
            }
        } else {
            System.out.println("Passwort stimmt nicht");
        }
    }
}
