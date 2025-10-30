package autohome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SizeInputScreen {
    private final String username;
    private final String role;

    public SizeInputScreen(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public void start(Stage stage) {
        stage.setTitle("House Dimensions and Room Setup");

        Label titleLabel = new Label("Enter House Details");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label widthLabel = new Label("House Width (m):");
        TextField widthField = new TextField();
        widthField.setPromptText("e.g., 20");

        Label heightLabel = new Label("House Height (m):");
        TextField heightField = new TextField();
        heightField.setPromptText("e.g., 15");

        Label bedroomLabel = new Label("Bedrooms:");
        Spinner<Integer> bedroomSpinner = new Spinner<>(1, 10, 3);
        bedroomSpinner.setEditable(true);

        Label bathroomLabel = new Label("Bathrooms:");
        Spinner<Integer> bathroomSpinner = new Spinner<>(1, 10, 2);
        bathroomSpinner.setEditable(true);

        Label kitchenLabel = new Label("Kitchens:");
        Spinner<Integer> kitchenSpinner = new Spinner<>(1, 10, 1);
        kitchenSpinner.setEditable(true);

        CheckBox gardenCheckBox = new CheckBox("Include Garden");
        gardenCheckBox.setSelected(true);

        Button designButton = new Button("Design House");
        designButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        designButton.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int bedrooms = bedroomSpinner.getValue();
                int bathrooms = bathroomSpinner.getValue();
                int kitchens = kitchenSpinner.getValue();
                boolean hasGarden = gardenCheckBox.isSelected();

                if (width <= 0 || height <= 0) {
                    showAlert("Invalid dimensions", "Width and height must be positive numbers.");
                    return;
                }

                LayoutDesigner layoutDesigner = new LayoutDesigner(width, height);
                layoutDesigner.start(stage, bedrooms, bathrooms, kitchens, hasGarden, username, role);

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for width and height.");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.add(widthLabel, 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(heightLabel, 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(bedroomLabel, 0, 2);
        grid.add(bedroomSpinner, 1, 2);
        grid.add(bathroomLabel, 0, 3);
        grid.add(bathroomSpinner, 1, 3);
        grid.add(kitchenLabel, 0, 4);
        grid.add(kitchenSpinner, 1, 4);
        grid.add(gardenCheckBox, 0, 5, 2, 1);

        VBox root = new VBox(20, titleLabel, grid, designButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(root, 400, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}