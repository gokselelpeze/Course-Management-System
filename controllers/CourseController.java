package controllers;

import _main.Main;
import classes.Course;
import classes.Database;
import classes.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    public static String currentCourse;

    public static Stage window;
    @FXML
    private Text courseName;
    @FXML
    private Text lecturer;
    @FXML
    private JFXButton btnViewCourse;

    private User user;
    private Database database;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = LoginController.currentUser;
        database = LoginController.database;
        String username = user.getUsername();

        Course course = DashboardController.currentCourse;
        courseName.setText(course.getId() + " - " + course.getName());
        lecturer.setText(course.getTeacher().getName());
    }

    @FXML
    void viewCourse(ActionEvent event) throws IOException {
        currentCourse = courseName.getText();
        window = new Stage();
        window.initStyle(StageStyle.UNDECORATED);
        window.setAlwaysOnTop(true);
        window.initOwner(Main.appStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxmls/coursePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        window.setScene(scene);
        window.setX(Main.appStage.getX() + 200);
        window.setY(Main.appStage.getY() + 80);
        window.showAndWait();
    }
}