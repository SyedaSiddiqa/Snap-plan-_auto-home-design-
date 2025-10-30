package autohome;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeDesignApp extends Application {
    private Stage primaryStage;
    private String userName = "User";
    private String userRole = "Designer";
    private int houseWidth = 100;
    private int houseHeight = 80;
    private int bedrooms = 2;
    private int bathrooms = 1;
    private int kitchens = 1;
    private boolean hasGarden = true;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginPage();
        primaryStage.setTitle("Home Design Suite");
        primaryStage.show();
    }

    private void showLoginPage() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome to Home Design Suite");
        scenetitle.setFont(Font.font(20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("User Name:");
        grid.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        btn.setOnAction(e -> {
            userName = userTextField.getText().isEmpty() ? "User" : userTextField.getText();
            showHouseConfigPage();
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 400, 275);
        primaryStage.setScene(scene);
    }

    private void showHouseConfigPage() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Configure Your Home");
        scenetitle.setFont(Font.font(20));
        grid.add(scenetitle, 0, 0, 2, 1);

        grid.add(new Label("Bedrooms:"), 0, 1);
        Spinner<Integer> bedSpinner = new Spinner<>(1, 5, bedrooms);
        grid.add(bedSpinner, 1, 1);

        grid.add(new Label("Bathrooms:"), 0, 2);
        Spinner<Integer> bathSpinner = new Spinner<>(1, 5, bathrooms);
        grid.add(bathSpinner, 1, 2);

        grid.add(new Label("Kitchens:"), 0, 3);
        Spinner<Integer> kitchenSpinner = new Spinner<>(1, 2, kitchens);
        grid.add(kitchenSpinner, 1, 3);

        grid.add(new Label("Garden:"), 0, 4);
        CheckBox gardenCheck = new CheckBox();
        gardenCheck.setSelected(hasGarden);
        grid.add(gardenCheck, 1, 4);

        grid.add(new Label("House Width:"), 0, 5);
        TextField widthField = new TextField(String.valueOf(houseWidth));
        grid.add(widthField, 1, 5);

        grid.add(new Label("House Height:"), 0, 6);
        TextField heightField = new TextField(String.valueOf(houseHeight));
        grid.add(heightField, 1, 6);

        Button btn = new Button("Start Designing");
        btn.setOnAction(e -> {
            bedrooms = bedSpinner.getValue();
            bathrooms = bathSpinner.getValue();
            kitchens = kitchenSpinner.getValue();
            hasGarden = gardenCheck.isSelected();
            houseWidth = Integer.parseInt(widthField.getText());
            houseHeight = Integer.parseInt(heightField.getText());

            LayoutDesigner designer = new LayoutDesigner(houseWidth, houseHeight);
            designer.start(primaryStage, bedrooms, bathrooms, kitchens, hasGarden, userName, userRole);
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 7);

        Scene scene = new Scene(grid, 450, 400);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}