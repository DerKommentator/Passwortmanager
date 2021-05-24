package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
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
    private TextField filterField;

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

    private final ObservableList<AccountInfo> dataList = FXCollections.observableArrayList();
    private final Account user = new Account();

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
                List<AccountInfo> queryResults = getDatabaseEntries(username + ".db");
                user.setDBPath(username + ".db");
                user.setUsername(username);
                dataList.addAll(queryResults);
                searchFilter();
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

        if(email != null && website != null && name != null && password != null && username != null){
            AccountInfo erstellteAccountInfo = new AccountInfo(email, website , name, password, username);
            System.out.println("Die Website wurde erfolgreich hinzugefügt.");
            System.out.println("Email: " + erstellteAccountInfo.getEmail());
            System.out.println("Website: " + erstellteAccountInfo.getWebsite());
            System.out.println("Name: " + erstellteAccountInfo.getName());
            System.out.println("Passwort: " + erstellteAccountInfo.getPassword());

            Connection conn = Database.createDatabaseConnection(user.getDBPath());
            AccountInfoTable.insert(conn, erstellteAccountInfo.getName(), erstellteAccountInfo.getEmail(), erstellteAccountInfo.getWebsite(), erstellteAccountInfo.getUsername(), erstellteAccountInfo.getPassword());
            Database.closeDatabase(conn);

            //TODO: verbessern, weil uneffizient
            List<AccountInfo> queryResults = getDatabaseEntries(user.getDBPath());
            dataList.remove(0, queryResults.size() - 1);
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

        tableView.setRowFactory(tv -> {
            TableRow<AccountInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    AccountInfo clickedRow = row.getItem();
                    System.out.println(clickedRow.getId());
                }
            });
            return row;
        });
    }



    public List<AccountInfo> getDatabaseEntries(String dbPath) {
        List<String> queryColumns = new ArrayList<String>();
        queryColumns.add("id");
        queryColumns.add("name");
        queryColumns.add("email");
        queryColumns.add("website");
        queryColumns.add("username");
        queryColumns.add("password");

        Connection conn = Database.createDatabaseConnection(dbPath);
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

                if (accountInfo.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (accountInfo.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (accountInfo.getWebsite().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (accountInfo.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
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
}
