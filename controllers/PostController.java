package controllers;

import animatefx.animation.Shake;
import classes.Comment;
import classes.Post;
import classes.Student;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class PostController implements Initializable {

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    @FXML
    private ImageView imgAuthor;
    @FXML
    private Text txtAuthor;
    @FXML
    private Text txtDate;
    @FXML
    private Text txtContent;
    @FXML
    private VBox boxComments;
    @FXML
    private ImageView imgUser;
    @FXML
    private TextField txtComment;
    @FXML
    private JFXButton btnPost;
    @FXML
    private JFXButton btnEditPost;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton btnDeletePost;
    Post currentPost;
    @FXML
    private AnchorPane post;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtComment.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                btnPost.fire();
            }
        });

    }
    @FXML
    void editPost(ActionEvent event) {
        stackPane.toFront();
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton JFXButton = new JFXButton("BACK");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> dialog.close());
        dialogLayout.setHeading(new Label("EDIT POST"));
        VBox vBox = new VBox();
        String type;
        TextArea textField = new TextArea();
        textField.setText(txtContent.getText());
        textField.setWrapText(true);

        vBox.getChildren().addAll(new Label("TYPE AND CLOSE"),
            textField);
        dialogLayout.setBody(vBox);
        dialogLayout.setActions(JFXButton);
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            editPost(currentPost, LoginController.currentUser.getName(), textField.getText());
            post.setEffect(null);
            stackPane.toBack();
        });
        post.setEffect(blur);
        dialog.show();
    }
    void editPost(Post post, String author, String content) {
        currentPost = post;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        txtAuthor.setText(author);
        txtDate.setText(formatter.format(date));
        txtContent.setText(content);
        txtContent.setWrappingWidth(860);
        try {
            File file = new File("D:\\DEV-Workspaces\\IntelliJ IDEA\\CENGOnline\\src\\img\\monkey.png");
            String url = file.toURI().toURL().toString();
            Image image = new Image(url);
            imgUser.setImage(image);

            File file2 = new File("D:\\DEV-Workspaces\\IntelliJ IDEA\\CENGOnline\\src\\img\\matruska.png");
            String url2 = file2.toURI().toURL().toString();
            Image image2 = new Image(url2);
            imgAuthor.setImage(image2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Comment comment : currentPost.getComments()) {
            addCommentToList(comment);
        }
    }


    @FXML
    void postComment(ActionEvent event) {

        String comment = txtComment.getText();
        if (comment.length() < 1) {
            new Shake(txtComment).play();
        } else {

            Comment comm = new Comment(LoginController.currentUser.getName(), comment, formatter.format(date));
            addCommentToList(comm);
            if (currentPost != null)
                currentPost.getComments().add(comm);
            txtComment.clear();
        }
        System.out.println("Comment posted");
    }

    @FXML
    void deletePost(ActionEvent event) {

    }


    private void addCommentToList(Comment comment) {
        FXMLLoader prefs = new FXMLLoader(getClass().getResource("../fxmls/comment.fxml"));
        try {
            AnchorPane pane = prefs.load();
            CommentController controller = prefs.getController();
            controller.editComment(comment);
            boxComments.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
