package controllers;

import _main.Main;
import classes.Database;
import classes.Stack;
import classes.Student;
import classes.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {

    User user;
    @FXML
    private Button btnDashboard;
    @FXML
    private JFXButton btnCloseImg;
    @FXML
    private JFXButton btnRestoreImg;
    @FXML
    private JFXButton btnMinimizeImg;
    @FXML
    private Button btnSchedule;
    @FXML
    private Button btnStatistics;
    @FXML
    private Button btnMessages;
    @FXML
    private MenuButton menuBtnProfilePopUp;
    @FXML
    private BorderPane rootBorderPane;
    private AnchorPane dashboard;
    private AnchorPane messageBox;
    @FXML
    private Label labelNotificationCount;
    @FXML
    private JFXButton btnNotification;
    @FXML
    private Circle circleNotification;
    @FXML
    private StackPane stackPane;

    private final int maxNotification = 10;

    private Stack notifications;

    private int notificationCount = 0;

    Database database;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = LoginController.currentUser;
        database = LoginController.database;
        notifications = new Stack(maxNotification);
        loadDashboard();
        loadMessages();
        rootBorderPane.setCenter(dashboard);
        // TODO
        menuBtnProfilePopUp.setText(user.getName());
        notificationCount = 0;
        reloadNotifications();
        addNotification("Update available. New version!");
        Notifications.create().owner(Main.appStage).title("Login successful").text("Login successful").hideCloseButton().hideAfter(Duration.seconds(2)).show();
        addNotification("Welcome again, " + user.getName());
        menuBtnProfilePopUp.getItems().get(0).setOnAction(e -> {
            loadProfile();
        });
        menuBtnProfilePopUp.getItems().get(1).setOnAction(e -> {
            try {
                database.saveData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Notifications.create().owner(Main.appStage).title("Logout successful").text("Logout successful").hideCloseButton().hideAfter(Duration.seconds(2)).show();
            changeScene("../fxmls/login.fxml");

        });
    }

    private void addNotification(String notification) {
        notifications.push(notification);
        notificationCount += 1;
        reloadNotifications();
    }

    private void reloadNotifications() {
        if (notificationCount == 0) {
            labelNotificationCount.setText("");
            btnNotification.setDisable(true);
            circleNotification.setVisible(false);
        } else {
            labelNotificationCount.setText(String.valueOf(notificationCount));
            btnNotification.setDisable(false);
            circleNotification.setVisible(true);
        }
    }

    private void loadDashboard() {
        FXMLLoader pref;
        pref = new FXMLLoader(getClass().getResource("../fxmls/dashboard.fxml"));
        try {
            dashboard = pref.load();
            rootBorderPane.setCenter(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {
        FXMLLoader pref;
        pref = new FXMLLoader(getClass().getResource("../fxmls/messageBox.fxml"));
        try {
            messageBox = pref.load();
            rootBorderPane.setCenter(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
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

    private void loadProfile() {
        stackPane.toFront();
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton JFXButton = new JFXButton("BACK");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> dialog.close());
        dialogLayout.setHeading(new Label(user.getName()));
        VBox vBox = new VBox();
        String type;
        if (user instanceof Student) {
            type = "Student";
        } else
            type = "Teacher";
        vBox.getChildren().addAll(new Label("Name: " + user.getName()),
            new Label("User Type: " + type));
        dialogLayout.setBody(vBox);
        dialogLayout.setActions(JFXButton);
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
//            removeNotification(notification);
            dashboard.setEffect(null);
            stackPane.toBack();
        });
        dashboard.setEffect(blur);
        dialog.show();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Object eventSource = event.getSource();
        if (eventSource == btnDashboard) {
            rootBorderPane.setCenter(dashboard);

        } else if (eventSource == btnCloseImg) {
            database.saveData();
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.close();
        } else if (eventSource == btnRestoreImg) {
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.setFullScreen(!s.isFullScreen());
        } else if (eventSource == btnMinimizeImg) {
            Stage s = (Stage) ((Node) eventSource).getScene().getWindow();
            s.setIconified(true);
        } else if (eventSource == btnNotification && notificationCount > 0) {
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < notificationCount; i++) {
                temp.append(i + 1).append(". ").append(notifications.pop()).append(System.lineSeparator());
            }
            loadDialog(temp.toString());
            notificationCount = 0;
            reloadNotifications();
        } else if (eventSource == btnMessages) {
            rootBorderPane.setCenter(messageBox);

        }/* else if (eventSource == btnSchedule) {
            rootBorderPane.setCenter(dashboard);

        } else if (eventSource == btnStatistics) {
            rootBorderPane.setCenter(dashboard);
        }*/
    }

    // Notification Dialog
    private void loadDialog(String notification) {
        stackPane.toFront();
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton JFXButton = new JFXButton("OKAY");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> dialog.close());
        dialogLayout.setHeading(new Label("NOTIFICATIONS"));
        dialogLayout.setBody(new Label(notification));
        dialogLayout.setActions(JFXButton);
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
//            removeNotification(notification);
            dashboard.setEffect(null);
            stackPane.toBack();
        });
        dashboard.setEffect(blur);
        dialog.show();
    }
}
