package controllers;


import _main.Main;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import classes.Database;
import classes.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {


    public static User currentUser;
    public static Database database;
    @FXML
    private AnchorPane pnlSignUp;
    @FXML
    private AnchorPane pnlSignIn;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnSignIn;
    @FXML
    private Hyperlink linkForgotPassword;
    @FXML
    private Button btnSignUp;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnCloseImg;
    @FXML
    private JFXButton btnRestoreImg;
    @FXML
    private JFXButton btnMinimizeImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            database = new Database();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtPassword.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                btnSignIn.fire();
            }
        });
        txtUsername.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                btnSignIn.fire();
            }
        });
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Object eventSource = event.getSource();
        if (eventSource == btnSignIn) {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            if (username.isEmpty())
                new Shake(txtUsername).play();
            else if (password.isEmpty()) {
                new Shake(txtPassword).play();
            } else {

               /* System.out.println(username.length());
                System.out.println(database.getUser(username).getUsername());
                System.out.println(password);
                System.out.println(database.getAccounts().get(username).getPassword());*/

                if (database.getUserHashMap().get(username) != null && database.getUserHashMap().get(username).getPassword().equals(password)) {
                    System.out.println("Giriş başarılı");
                    currentUser = database.getUserHashMap().get(username);
                    changeScene("../fxmls/main.fxml");
                } else
                    System.out.println("şifre yanlış");
            }
        } else if (eventSource == btnSignUp) {
            System.out.println("kayit ol");
            new FadeIn(pnlSignUp).play();
            pnlSignUp.toFront();
        } else if (eventSource == linkForgotPassword) {
            System.out.println("sifremi unuttum");

        } else if (eventSource == btnBack) {
            new FadeIn(pnlSignIn).play();
            pnlSignIn.toFront();
        } else if (eventSource == btnCloseImg) {
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.close();
        } else if (eventSource == btnRestoreImg) {
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.setFullScreen(!s.isFullScreen());
        } else if (eventSource == btnMinimizeImg) {
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.setIconified(true);
        }

    }

    private void changeScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, 1280, 720);
            Main.appStage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
