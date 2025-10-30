package autohome;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    public void start(Stage primaryStage) {
        Label titleLabel = new Label("AutoHome Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        loginButton.setOnAction(e -> handleLogin(primaryStage));

        VBox root = new VBox(15,
                titleLabel,
                usernameField,
                passwordField,
                errorLabel,
                loginButton
        );
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30px; -fx-background-color: #f0f8ff;");
        Scene scene = new Scene(root, 350, 300);

        primaryStage.setTitle("AutoHome Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        if (DataManager.validateUser(username, password)) {
            String role = DataManager.getUserRole(username);
            DataManager.logUsage(username, role, "Logged in");

            SizeInputScreen sizeInput = new SizeInputScreen(username, role);
            sizeInput.start(primaryStage);
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }
}