package autohome;

import autohome.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LayoutDesigner {
    private final BlueprintCanvas blueprintCanvas;
    private final List<Furniture> availableFurniture = new ArrayList<>();
    private final int houseWidth;
    private final int houseHeight;
    private int bedroomCount;
    private int bathroomCount;
    private int kitchenCount;
    private boolean hasGarden;
    private Color surfaceColor = Color.SANDYBROWN;
    private Color roofColor = Color.DARKRED;
    private Color wallsColor = Color.LIGHTGRAY;
    private ThreeDView threeDView;
    private boolean show3DView = false;
    private VBox blueprintArea;

    public LayoutDesigner(int houseWidth, int houseHeight) {
        this.houseWidth = houseWidth;
        this.houseHeight = houseHeight;
        this.blueprintCanvas = new BlueprintCanvas(600, 400, houseWidth, houseHeight);
        this.threeDView = new ThreeDView();
        initializeFurniture();
    }

    public void start(Stage stage, int beds, int baths, int kitchens, boolean garden,
                      String userName, String userRole) {
        this.bedroomCount = beds;
        this.bathroomCount = baths;
        this.kitchenCount = kitchens;
        this.hasGarden = garden;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #e0e0e0;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f0f0;");

        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(15));
        rightPanel.setStyle("-fx-background-color: #f0f0f0;");

        rightPanel.getChildren().addAll(
                createRoomPalette(),
                createFurniturePalette(),
                createStructurePalette()
        );

        scrollPane.setContent(rightPanel);

        blueprintArea = new VBox(15, new Label("House Blueprint"), blueprintCanvas);
        blueprintArea.setAlignment(Pos.CENTER);
        blueprintArea.setPadding(new Insets(15));
        blueprintArea.setStyle("-fx-background-color: white; -fx-border-color: #a0a0a0; -fx-border-width: 1;");

        HBox buttonBox = createButtonBox(stage, userName, userRole, root);

        root.setRight(scrollPane);
        root.setCenter(blueprintArea);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 1100, 750);
        stage.setTitle("House Layout Designer");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeFurniture() {
        availableFurniture.add(new Furniture("Bed", 60, 40, Color.LIGHTBLUE));
        availableFurniture.add(new Furniture("Sofa", 80, 40, Color.LIGHTCORAL));
        availableFurniture.add(new Furniture("Table", 60, 60, Color.LIGHTGREEN));
        availableFurniture.add(new Furniture("Chair", 30, 30, Color.LIGHTGOLDENRODYELLOW));
        availableFurniture.add(new Furniture("Cabinet", 40, 40, Color.LIGHTSALMON));
        availableFurniture.add(new Furniture("Desk", 70, 40, Color.LIGHTSEAGREEN));
    }

    private VBox createRoomPalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(15));
        palette.setStyle("-fx-background-color: #e0f0ff; -fx-border-color: #a0a0a0; -fx-border-width: 1; -fx-border-radius: 5;");
        palette.setPrefWidth(220);

        Label title = new Label("Rooms");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        palette.getChildren().add(title);

        for (int i = 1; i <= bedroomCount; i++) {
            Button btn = createCompactButton("Bedroom " + i, Color.LIGHTBLUE, 100, 80);
            palette.getChildren().add(btn);
        }

        for (int i = 1; i <= bathroomCount; i++) {
            Button btn = createCompactButton("Bathroom " + i, Color.LIGHTCORAL, 80, 60);
            palette.getChildren().add(btn);
        }

        for (int i = 1; i <= kitchenCount; i++) {
            Button btn = createCompactButton("Kitchen " + i, Color.LIGHTGREEN, 120, 80);
            palette.getChildren().add(btn);
        }

        if (hasGarden) {
            Button btn = createCompactButton("Garden", Color.PALEGREEN, houseWidth * 20, 40);
            palette.getChildren().add(btn);
        }

        return palette;
    }

    private Button createCompactButton(String text, Color color, double width, double height) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: " + toHex(color) + "; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px; " +
                "-fx-background-radius: 5;");
        btn.setTooltip(new Tooltip("Click to add " + text + " to blueprint"));
        btn.setOnAction(e -> {
            Room newRoom = new Room(text, width, height, color);
            newRoom.setPosition(
                    blueprintCanvas.getWidth() / 2 - newRoom.getWidth() / 2,
                    blueprintCanvas.getHeight() / 2 - newRoom.getHeight() / 2
            );
            blueprintCanvas.addRoom(newRoom);
        });
        return btn;
    }

    private VBox createFurniturePalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(15));
        palette.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #a0a0a0; -fx-border-width: 1; -fx-border-radius: 5;");
        palette.setPrefWidth(220);

        Label title = new Label("Furniture");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        palette.getChildren().add(title);

        for (Furniture furniture : availableFurniture) {
            Button btn = new Button(furniture.getType());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-font-size: 14px; " +
                    "-fx-padding: 8px; " +
                    "-fx-background-radius: 5;");
            btn.setTooltip(new Tooltip("Click to add " + furniture.getType() + " to blueprint"));

            btn.setOnAction(e -> {
                Furniture newFurniture = new Furniture(
                        furniture.getType(),
                        furniture.getWidth(),
                        furniture.getHeight(),
                        furniture.getColor()
                );
                newFurniture.setPosition(
                        blueprintCanvas.getWidth() / 2 - newFurniture.getWidth() / 2,
                        blueprintCanvas.getHeight() / 2 - newFurniture.getHeight() / 2
                );
                blueprintCanvas.addFurniture(newFurniture);
            });

            palette.getChildren().add(btn);
        }

        return palette;
    }

    private VBox createStructurePalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(15));
        palette.setStyle("-fx-background-color: #f0e0ff; -fx-border-color: #a0a0a0; -fx-border-width: 1; -fx-border-radius: 5;");
        palette.setPrefWidth(220);

        Label title = new Label("Structure");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        palette.getChildren().add(title);

        Button surfaceBtn = new Button("Surface");
        surfaceBtn.setMaxWidth(Double.MAX_VALUE);
        surfaceBtn.setStyle("-fx-background-color: " + toHex(surfaceColor) + "; " +
                "-fx-font-size: 14px; -fx-padding: 8px;");
        surfaceBtn.setTooltip(new Tooltip("Add surface to blueprint"));
        surfaceBtn.setOnAction(e -> {
            Structure surface = new Structure("Surface",
                    blueprintCanvas.getWidth() / 2 - 200,
                    blueprintCanvas.getHeight() / 2 - 150,
                    400, 300, surfaceColor);
            blueprintCanvas.addStructure(surface);
        });

        Button roofBtn = new Button("Roof");
        roofBtn.setMaxWidth(Double.MAX_VALUE);
        roofBtn.setStyle("-fx-background-color: " + toHex(roofColor) + "; " +
                "-fx-font-size: 14px; -fx-padding: 8px;");
        roofBtn.setTooltip(new Tooltip("Add roof to blueprint"));
        roofBtn.setOnAction(e -> {
            Structure roof = new Structure("Roof",
                    blueprintCanvas.getWidth() / 2 - 100,
                    20,
                    200, 40, roofColor);
            blueprintCanvas.addStructure(roof);
        });

        Button wallsBtn = new Button("Walls");
        wallsBtn.setMaxWidth(Double.MAX_VALUE);
        wallsBtn.setStyle("-fx-background-color: " + toHex(wallsColor) + "; " +
                "-fx-font-size: 14px; -fx-padding: 8px;");
        wallsBtn.setTooltip(new Tooltip("Add walls to blueprint"));
        wallsBtn.setOnAction(e -> {
            Structure walls = new Structure("Walls",
                    50,
                    50,
                    blueprintCanvas.getWidth() - 100,
                    blueprintCanvas.getHeight() - 100,
                    wallsColor);
            blueprintCanvas.addStructure(walls);
        });

        Button surfaceColorBtn = new Button("Surface Color");
        surfaceColorBtn.setMaxWidth(Double.MAX_VALUE);
        surfaceColorBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        surfaceColorBtn.setOnAction(e -> {
            Color newColor = showColorPicker("Select Surface Color", surfaceColor);
            if (newColor != null) {
                surfaceColor = newColor;
                surfaceBtn.setStyle("-fx-background-color: " + toHex(surfaceColor) + "; " +
                        "-fx-font-size: 14px; -fx-padding: 8px;");
            }
        });

        Button roofColorBtn = new Button("Roof Color");
        roofColorBtn.setMaxWidth(Double.MAX_VALUE);
        roofColorBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        roofColorBtn.setOnAction(e -> {
            Color newColor = showColorPicker("Select Roof Color", roofColor);
            if (newColor != null) {
                roofColor = newColor;
                roofBtn.setStyle("-fx-background-color: " + toHex(roofColor) + "; " +
                        "-fx-font-size: 14px; -fx-padding: 8px;");
            }
        });

        Button wallsColorBtn = new Button("Walls Color");
        wallsColorBtn.setMaxWidth(Double.MAX_VALUE);
        wallsColorBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        wallsColorBtn.setOnAction(e -> {
            Color newColor = showColorPicker("Select Walls Color", wallsColor);
            if (newColor != null) {
                wallsColor = newColor;
                wallsBtn.setStyle("-fx-background-color: " + toHex(wallsColor) + "; " +
                        "-fx-font-size: 14px; -fx-padding: 8px;");
            }
        });

        palette.getChildren().addAll(surfaceBtn, roofBtn, wallsBtn,
                new Separator(), surfaceColorBtn, roofColorBtn, wallsColorBtn);
        return palette;
    }

    private Color showColorPicker(String title, Color initialColor) {
        ColorPicker colorPicker = new ColorPicker(initialColor);
        colorPicker.setStyle("-fx-font-size: 14px;");

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(colorPicker);

        if (dialog.showAndWait().get() == ButtonType.OK) {
            return colorPicker.getValue();
        }
        return null;
    }

    private HBox createButtonBox(Stage stage, String userName, String userRole, BorderPane root) {
        Button saveBtn = new Button("Save Blueprint");
        saveBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        Button reportBtn = new Button("Generate Report");
        reportBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        Button clearBtn = new Button("Clear All");
        clearBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        Button toggle3DBtn = new Button("Show 3D View");
        toggle3DBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        toggle3DBtn.setOnAction(e -> {
            show3DView = !show3DView;
            if (show3DView) {
                threeDView.updateScene(
                        blueprintCanvas.getRooms(),
                        blueprintCanvas.getFurniture(),
                        blueprintCanvas.getStructures()
                );
                root.setCenter(threeDView.getContainer());
                toggle3DBtn.setText("Show 2D View");
            } else {
                root.setCenter(blueprintArea);
                toggle3DBtn.setText("Show 3D View");
            }
        });

        saveBtn.setOnAction(e -> saveBlueprint(stage, userName, userRole));
        reportBtn.setOnAction(e -> {
            DataManager.logUsage(userName, userRole, "Generated report");
            WritableImage snapshot = blueprintCanvas.snapshot();
            String blueprintPath = ReportGenerator.saveBlueprintImage(stage, snapshot);
            if (blueprintPath != null) {
                ReportGenerator.generateReport(stage, bedroomCount, bathroomCount, kitchenCount,
                        hasGarden, blueprintPath, userName, userRole);
            }
        });
        clearBtn.setOnAction(e -> {
            blueprintCanvas.clearAll();
            DataManager.logUsage(userName, userRole, "Cleared blueprint");
        });

        HBox buttonBox = new HBox(15, saveBtn, reportBtn, clearBtn, toggle3DBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setStyle("-fx-background-color: #e0e0e0;");

        return buttonBox;
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void saveBlueprint(Stage stage, String userName, String userRole) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Blueprint");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ReportGenerator.saveImage(blueprintCanvas.snapshot(), file);
                DataManager.logUsage(userName, userRole, "Saved blueprint to " + file.getName());
                new Alert(Alert.AlertType.INFORMATION, "Blueprint saved successfully!").show();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error saving blueprint: " + e.getMessage()).show();
            }
        }
    }
}