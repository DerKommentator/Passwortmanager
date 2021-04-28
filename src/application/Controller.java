package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_createAccount.setOnAction(this::createAccountisClicked);
        btn_cancelCreateAccount.setOnAction(this::cancelCreateAccountIsClicked);
        btn_createAccountInTab.setOnAction(this::createAccountInTabIsClicked);
        btn_logout.setOnAction(this::logoutIsClicked);
        btn_login.setOnAction(this::loginIsClicked);
    }

    // Account-Erstellungs Button beim Login
    @FXML
    private void createAccountisClicked(javafx.event.ActionEvent actionEvent) {
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
        System.out.println("Account wurde erstellt...");
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
}
