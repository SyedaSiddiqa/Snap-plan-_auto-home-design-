package autohome;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        new LoginScreen().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}