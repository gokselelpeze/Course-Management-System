package controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import classes.Database;
import classes.Post;
import classes.Student;
import classes.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CoursePageController implements Initializable {
    @FXML
    private AnchorPane pnlAnnouncement;

    @FXML
    private JFXButton btnBack;

    @FXML
    private TextArea txtAnnouncement;

    @FXML
    private JFXButton btnPostAnnouncement;

    @FXML
    private AnchorPane pnlEditCourse;

    @FXML
    private JFXTextField txtEditCourseID;

    @FXML
    private JFXTextField txtEditCourseName;

    @FXML
    private JFXTimePicker txtEditLessonTime;

    @FXML
    private JFXButton btnEditCourseToSystem;

    @FXML
    private AnchorPane pnlCourse;

    @FXML
    private Text courseName;

    @FXML
    private ScrollPane scrPane;

    @FXML
    private VBox boxTimeline;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXButton btnAddAnnouncement;

    @FXML
    private JFXButton btnEditCourse;

    @FXML
    private JFXButton btnDeleteCourse;

    private ArrayList<Post> postList;

    private String[] courseID_Name;

    private User user;
    private Database database;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postList = new ArrayList<>();
        // Getting courseName
        courseID_Name = CourseController.currentCourse.split(" - ");
        // Getting user
        user = LoginController.currentUser;
        // Getting database obj
        database = LoginController.database;
        courseName.setText(CourseController.currentCourse);
        if (user instanceof Student) {
            btnAddAnnouncement.setVisible(false);
            btnEditCourse.setVisible(false);
            btnDeleteCourse.setVisible(false);
        }
        readPosts();
        txtAnnouncement.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                btnPostAnnouncement.fire();
            }
        });

    }

    public void readPosts() {
        if (database.getPostHashMap().containsKey(courseID_Name[1]))
            postList = database.getPostHashMap().get(courseID_Name[1]);
        if (postList.isEmpty()) {
            System.out.println("No post available");

        } else {
            for (int i = 0; i < postList.size(); i++) {
                System.out.println(postList.get(i).getAuthor() + " " + postList.get(i).getContent());
                addPostToCourse(postList.get(i));
                System.out.println(postList.size());
            }
        }
        // TODO null pointer
        /*Post post = new Post("Göksel", "Selam bu ilk post");
        addPostToCourse(post);
        Post post2 = new Post("Göksen", "Selam bu ikinci post");
        addPostToCourse(post2);
    */
    }

    private void addPostToCourse(Post post) {
        FXMLLoader prefs = new FXMLLoader(getClass().getResource("../fxmls/post.fxml"));
        try {
            AnchorPane pane = prefs.load();
            PostController controller = prefs.getController();
            controller.editPost(post, post.getAuthor(), post.getContent());
            boxTimeline.getChildren().add(0, pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addAnnouncement() {
        new FadeIn(pnlAnnouncement).play();
        pnlAnnouncement.toFront();
        txtAnnouncement.clear();
    }

    @FXML
    void closeCourse() {
        CourseController.window.close();
    }

    @FXML
    void postAnnouncement() {
        String announcement = txtAnnouncement.getText();
        if (announcement.length() < 1) {
            new Shake(txtAnnouncement).play();
        } else {
            Post post = new Post(user.getName(), announcement);
            if (database.getPostHashMap().containsKey(courseID_Name[1])) {
                database.getPostHashMap().get(courseID_Name[1]).add(0, post);
            } else {
                ArrayList<Post> postList = new ArrayList<>();
                postList.add(0, post);
                database.getPostHashMap().put(courseID_Name[1], postList);
            }
            addPostToCourse(post);
            new FadeIn(pnlCourse).play();
            pnlCourse.toFront();
        }
    }

    @FXML
    void backToCourses() {
        new FadeIn(pnlCourse).play();
        pnlCourse.toFront();
    }

    @FXML
    void editCourseToSystem(ActionEvent event) {
        database.editCourseName(DashboardController.currentCourse.getName(), txtEditCourseID.getText(), txtEditCourseName.getText());
        new FadeIn(pnlCourse).play();
        pnlCourse.toFront();
    }

    @FXML
    void deleteCourse(ActionEvent event) {
        database.deleteCourse(courseID_Name[1]);
        System.out.println(CourseController.currentCourse + "  deleted.");
        Stage stage = (Stage) btnDeleteCourse.getScene().getWindow();
        stage.close();
    }

    @FXML
    void editCourse(ActionEvent event) {
        new FadeIn(pnlEditCourse).play();
        pnlEditCourse.toFront();
    }
}
