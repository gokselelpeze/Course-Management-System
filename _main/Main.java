package _main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static final String appTitle = "CENGONLINE";
    public static Stage appStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        appStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/login.fxml"));
        primaryStage.setTitle(appTitle);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }
}
