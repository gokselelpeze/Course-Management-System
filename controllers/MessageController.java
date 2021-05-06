package controllers;

import classes.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MessageController implements Initializable {
    @FXML
    private Text txtName;

    @FXML
    private Text txtDate;

    @FXML
    private Text txtMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void editMessage(Message message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        txtName.setText(message.getSender());
        txtDate.setText(formatter.format(date));
        txtMessage.setText(message.getText());
        txtMessage.setWrappingWidth(400);

    }
}