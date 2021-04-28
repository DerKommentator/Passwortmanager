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
    private Button btn_createAccount;

    @FXML
    void createAccountisClicked(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_createAccount.setOnAction(this::createAccountisClicked);
    }

    @FXML
    private void createAccountisClicked(javafx.event.ActionEvent actionEvent) {
        tabPane_Main.getSelectionModel().select(1);
    }
}
