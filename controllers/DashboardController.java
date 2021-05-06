package controllers;

import animatefx.animation.FadeIn;
import classes.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public static Course currentCourse;
    ArrayList<String> courses;
    User user;
    Database database;
    @FXML
    private AnchorPane pnlCourses;
    @FXML
    private VBox boxCourses;
    @FXML
    private JFXButton btnAddCourse;
    @FXML
    private AnchorPane pnlAddCourse;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXTextField txtCourseID;
    @FXML
    private JFXTextField txtCourseName;
    @FXML
    private JFXTimePicker txtLessonTime;
    @FXML
    private JFXButton btnAddCourseToSystem;
    @FXML
    private AnchorPane pnlEditCourse;
    @FXML
    private JFXButton btnBack1;
    @FXML
    private JFXTextField txtEditCourseID;
    @FXML
    private JFXTextField txtEditCourseName;
    @FXML
    private JFXTimePicker txtEditLessonTime;
    @FXML
    private JFXButton btnEditCourseToSystem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = LoginController.currentUser;
        database = LoginController.database;
        String username = user.getUsername();
        if (user instanceof Student) {
            btnAddCourse.setVisible(false);
            courses = database.getStudentHashMap().get(username).getCourseNames();
        } else {
            courses = database.getTeacherHashMap().get(username).getCourseNames();
        }
        refreshList();
    }

    public void refreshList() {
        boxCourses.getChildren().clear();
        for (String course : courses) {
            currentCourse = LoginController.database.getCourseHashMap().get(course);
            addCourseToList();
        }
    }

    // Swapping to New Course Page
    @FXML
    void addCourse(ActionEvent event) {
        new FadeIn(pnlAddCourse).play();
        pnlAddCourse.toFront();
    }

    @FXML
    void addCourseToSystem(ActionEvent event) {
        database.addCourse(txtCourseID.getText(), txtCourseName.getText(), (Teacher) LoginController.currentUser);
        new FadeIn(pnlCourses).play();
        pnlCourses.toFront();
        refreshList();
    }

    @FXML
    void backToCourses(ActionEvent event) {
        new FadeIn(pnlCourses).play();
        pnlCourses.toFront();
    }

    private void addCourseToList() {
        FXMLLoader prefs = new FXMLLoader(getClass().getResource("../fxmls/course.fxml"));
        try {
            boxCourses.getChildren().add(prefs.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
