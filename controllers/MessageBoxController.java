package controllers;

import animatefx.animation.Shake;
import classes.Chat;
import classes.Database;
import classes.Message;
import classes.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MessageBoxController implements Initializable {

    @FXML
    private VBox boxMessages;

    @FXML
    private TextField txtComment;

    @FXML
    private JFXButton btnPost;
    @FXML
    private ListView<String> userListView;

    @FXML
    private TextField searchUser;
    @FXML
    private Label lblName;

    Database database;

    ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        database = LoginController.database;
        setListViewItems();
        setItemActions();
    }

    private void refreshChat(String receiver) {
        boxMessages.getChildren().clear();
        boxMessages.getChildren().removeAll();

        Chat chat = database.getMessageHashMap().get(LoginController.currentUser.getUsername()).get(receiver);
        for (Message message : chat.getChat()) {
            addMessage(message);
        }
    }
    private void loadChat(String receiver) {
        boxMessages.getChildren().clear();
        boxMessages.getChildren().removeAll();

        Chat chat = database.getMessageHashMap().get(LoginController.currentUser.getUsername()).get(receiver);
        for (Message message : chat.getChat()) {
            addMessage(message.getReceiver(), message);
        }
    }
    // METHOD OVERLOADING
    private void addMessage(Message message) {
        FXMLLoader prefs = new FXMLLoader(getClass().getResource("../fxmls/message.fxml"));
        try {
            AnchorPane pane = prefs.load();
            MessageController controller = prefs.getController();
            controller.editMessage(message);
            boxMessages.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addMessage(String receiver, Message message) {
        FXMLLoader prefs = new FXMLLoader(getClass().getResource("../fxmls/message.fxml"));
        try {
            AnchorPane pane = prefs.load();
            MessageController controller = prefs.getController();
            controller.editMessage(message);
            boxMessages.getChildren().add(pane);
            //
            database.addMessage(LoginController.currentUser.getUsername(), receiver, message);
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void sendMessage(ActionEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Message message = new Message(LoginController.currentUser.getName(), database.getUserHashMap().get(getUserWithName(lblName.getText()).getUsername()).getUsername(), txtComment.getText(), formatter.format(date));
        addMessage(getUserWithName(lblName.getText()).getUsername(), message);
    }

    public void setListViewItems() {
        for (User user : database.getUserArrayList()) {
            System.out.println(user.getName());
            if (user != LoginController.currentUser)
                userArrayList.add(user);
        }
    }

    public void refreshListView(ListView<String> userListView, ArrayList<User> userArrayList) {
        userListView.getItems().clear();
        userListView.getSelectionModel().clearSelection();
        for (User user : userArrayList) {
            System.out.println(user.getName());
            userListView.getItems().add(user.getName());
        }
    }

    public User getUserWithName(String name) {
        for (User variable : userArrayList) {
            if (variable.getName() == name) {
                return variable;
            }
        }
        return null;
    }

    public static ArrayList<String> getUserNames(ArrayList<User> userArrayList) {
        ArrayList<String> userNames = new ArrayList<>();
        for (User user : userArrayList) {
            userNames.add(user.getName());
        }
        return userNames;
    }

    public void setItemActions() {
        refreshListView(userListView, userArrayList);

        searchUser.setOnKeyReleased(e -> {
            userListView.getItems().clear();
            if (!searchUser.getText().isEmpty()) {
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (userArrayList.get(i).getName().toLowerCase().contains(searchUser.getText().toLowerCase())) {
                        userListView.getItems().add(userArrayList.get(i).getName());
                    }
                }
            } else {
                userListView.getItems().addAll(getUserNames(userArrayList));
            }
        });

        userListView.setOnMouseClicked(click -> {

            if (click.getClickCount() == 2) {
                User user = getUserWithName(userListView.getSelectionModel().getSelectedItem());
                lblName.setText(user.getName());
                System.out.println(user.getName());
                if (database.getMessageHashMap().get(LoginController.currentUser.getUsername()) != null){
                    refreshChat(user.getUsername());
                    System.out.println("1");
                }
                else
                    System.out.println("2");
            }
        });
        searchUser.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                if (!userListView.getItems().isEmpty()) {
                    userListView.getSelectionModel().select(0);
                    User user = getUserWithName(userListView.getSelectionModel().getSelectedItem());
                    lblName.setText(user.getName());
                    boxMessages.getChildren().removeAll();
                    if (database.getMessageHashMap().get(LoginController.currentUser.getUsername()) != null){
                        refreshChat(user.getUsername());
                        System.out.println("1");
                    }
                    else
                        System.out.println("2");


                } else
                    new Shake(searchUser).play();
            }
        });

    }
}
