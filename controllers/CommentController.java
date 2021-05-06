package controllers;

import classes.Comment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CommentController implements Initializable {

    @FXML
    private Text txtAuthor;

    @FXML
    private Text txtDate;

    @FXML
    private Text txtComment;

    @FXML
    private ImageView imgUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void editComment(Comment comment) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        txtAuthor.setText(comment.getAuthor());
        txtDate.setText(formatter.format(date));
        txtComment.setText(comment.getComment());
        txtComment.setWrappingWidth(636);
        try {
            File file = new File("D:\\DEV-Workspaces\\IntelliJ IDEA\\CENGOnline\\src\\img\\monkey.png");
            String url = file.toURI().toURL().toString();
            Image image = new Image(url);
            imgUser.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}