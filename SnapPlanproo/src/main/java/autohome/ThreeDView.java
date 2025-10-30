package autohome;

import autohome.model.*;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeDView {
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraRotateX = new Rotate(-30, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraPosition = new Translate(0, -100, -1000);
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Group sceneRoot = new Group();
    private final SubScene subScene;
    private final Pane container = new Pane();

    private Node selectedNode;
    private final double roomHeight = 100;
    private final double floorLevel = 300;
    private final double wallThickness = 10;

    // Furniture materials
    private final Map<String, PhongMaterial> materials = new HashMap<>();

    public ThreeDView() {
        // Setup subscene
        subScene = new SubScene(sceneRoot, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTBLUE);

        // Bind subscene size to container
        subScene.widthProperty().bind(container.widthProperty());
        subScene.heightProperty().bind(container.heightProperty());

        // Add subScene to our container
        container.getChildren().add(subScene);
        container.setStyle("-fx-background-color: #2b2b2b;");

        // Configure camera
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(55);
        camera.getTransforms().addAll(
                cameraPosition,
                cameraRotateY,
                cameraRotateX
        );
        subScene.setCamera(camera);

        // Initialize materials
        initMaterials();

        // Add lighting
        PointLight mainLight = new PointLight(Color.WHITE);
        mainLight.setTranslateX(500);
        mainLight.setTranslateY(-200);
        mainLight.setTranslateZ(-500);
        sceneRoot.getChildren().add(mainLight);

        PointLight ambientLight = new PointLight(Color.WHITE.deriveColor(0, 1, 0.7, 1));
        ambientLight.setTranslateX(-500);
        ambientLight.setTranslateY(300);
        ambientLight.setTranslateZ(500);
        sceneRoot.getChildren().add(ambientLight);

        // Add floor
        Box floor = new Box(2000, 10, 2000);
        floor.setMaterial(materials.get("floor"));
        floor.setTranslateY(floorLevel);
        sceneRoot.getChildren().add(floor);

        // Add grid for better perspective
        addGrid();

        // Setup mouse controls
        setupMouseControls();
    }

    private void initMaterials() {
        // Wood material
        PhongMaterial woodMaterial = new PhongMaterial();
        woodMaterial.setDiffuseColor(Color.BURLYWOOD);
        woodMaterial.setSpecularColor(Color.WHITE);
        woodMaterial.setSpecularPower(64);

        // Fabric material
        PhongMaterial fabricMaterial = new PhongMaterial();
        fabricMaterial.setDiffuseColor(Color.LIGHTCORAL);
        fabricMaterial.setSpecularColor(Color.WHITE);
        fabricMaterial.setSpecularPower(32);

        // Metal material
        PhongMaterial metalMaterial = new PhongMaterial();
        metalMaterial.setDiffuseColor(Color.SILVER);
        metalMaterial.setSpecularColor(Color.WHITE);
        metalMaterial.setSpecularPower(128);

        // Floor material
        PhongMaterial floorMaterial = new PhongMaterial();
        floorMaterial.setDiffuseColor(Color.LIGHTGRAY);
        floorMaterial.setSpecularColor(Color.WHITE);
        floorMaterial.setSpecularPower(32);

        // Wall material
        PhongMaterial wallMaterial = new PhongMaterial();
        wallMaterial.setDiffuseColor(Color.LIGHTGRAY.deriveColor(0, 1, 0.8, 1));
        wallMaterial.setSpecularColor(Color.WHITE);
        wallMaterial.setSpecularPower(32);

        materials.put("wood", woodMaterial);
        materials.put("fabric", fabricMaterial);
        materials.put("metal", metalMaterial);
        materials.put("floor", floorMaterial);
        materials.put("wall", wallMaterial);
    }

    private void addGrid() {
        // Create a grid for better depth perception
        Group gridGroup = new Group();
        int gridSize = 2000;
        int gridStep = 100;

        for (int i = -gridSize/2; i <= gridSize/2; i += gridStep) {
            // X-axis lines
            Box lineX = new Box(gridSize, 1, 1);
            lineX.setMaterial(new PhongMaterial(Color.GRAY.deriveColor(0, 1, 1, 0.4)));
            lineX.setTranslateY(floorLevel + 1);
            lineX.setTranslateZ(i);
            gridGroup.getChildren().add(lineX);

            // Z-axis lines
            Box lineZ = new Box(1, 1, gridSize);
            lineZ.setMaterial(new PhongMaterial(Color.GRAY.deriveColor(0, 1, 1, 0.4)));
            lineZ.setTranslateY(floorLevel + 1);
            lineZ.setTranslateX(i);
            gridGroup.getChildren().add(lineZ);
        }

        sceneRoot.getChildren().add(gridGroup);
    }

    public ObservableList<Node> getChildren() {
        return container.getChildren();
    }

    public Pane getContainer() {
        return container;
    }

    private void setupMouseControls() {
        sceneRoot.setOnMousePressed(this::handleMousePressed);
        sceneRoot.setOnMouseDragged(this::handleMouseDragged);
        sceneRoot.setOnMouseReleased(e -> selectedNode = null);

        sceneRoot.setOnScroll(event -> {
            double zoomFactor = 1.05;
            double delta = event.getDeltaY();

            if (delta < 0) {
                zoomFactor = 0.95;
            }

            cameraPosition.setZ(cameraPosition.getZ() * zoomFactor);
        });
    }

    private void handleMousePressed(MouseEvent me) {
        mouseOldX = me.getSceneX();
        mouseOldY = me.getSceneY();

        if (me.getPickResult().getIntersectedNode() != null) {
            Node picked = me.getPickResult().getIntersectedNode();
            if (picked.getUserData() != null && "furniture".equals(picked.getUserData())) {
                selectedNode = picked;
                me.consume();
            }
        }
    }

    private void handleMouseDragged(MouseEvent me) {
        mousePosX = me.getSceneX();
        mousePosY = me.getSceneY();

        double deltaX = mousePosX - mouseOldX;
        double deltaY = mousePosY - mouseOldY;

        if (selectedNode != null) {
            double moveSpeed = 0.5;
            selectedNode.setTranslateX(selectedNode.getTranslateX() + deltaX * moveSpeed);
            selectedNode.setTranslateZ(selectedNode.getTranslateZ() + deltaY * moveSpeed);
        } else {
            cameraRotateY.setAngle(cameraRotateY.getAngle() + deltaX * 0.2);
            double newAngle = cameraRotateX.getAngle() - deltaY * 0.2;
            if (newAngle > -90 && newAngle < 0) {
                cameraRotateX.setAngle(newAngle);
            }
        }

        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        me.consume();
    }

    public void updateScene(List<Room> rooms, List<Furniture> furniture, List<Structure> structures) {
        sceneRoot.getChildren().removeIf(node ->
                node.getUserData() != null &&
                        ("room".equals(node.getUserData()) ||
                                "furniture".equals(node.getUserData()) ||
                                "structure".equals(node.getUserData()))
        );

        for (Room room : rooms) {
            addRoomWithWalls(room);
        }

        for (Furniture item : furniture) {
            addFurniture(item);
        }

        for (Structure structure : structures) {
            addStructure(structure);
        }
    }

    private void addRoomWithWalls(Room room) {
        double roomY = floorLevel - roomHeight / 2;
        double centerX = 300;
        double centerZ = 200;

        Box floor = new Box(room.getWidth(), 5, room.getHeight());
        floor.setTranslateX(room.getX() - centerX);
        floor.setTranslateY(roomY + roomHeight / 2 - 2.5);
        floor.setTranslateZ(room.getY() - centerZ);
        floor.setMaterial(materials.get("floor"));
        floor.setUserData("room");
        sceneRoot.getChildren().add(floor);

        addWall(room, room.getX() - centerX, roomY, room.getY() - centerZ - room.getHeight()/2,
                room.getWidth(), roomHeight, wallThickness);

        addWall(room, room.getX() - centerX, roomY, room.getY() - centerZ + room.getHeight()/2,
                room.getWidth(), roomHeight, wallThickness);

        addWall(room, room.getX() - centerX - room.getWidth()/2, roomY, room.getY() - centerZ,
                wallThickness, roomHeight, room.getHeight());

        addWall(room, room.getX() - centerX + room.getWidth()/2, roomY, room.getY() - centerZ,
                wallThickness, roomHeight, room.getHeight());
    }

    private void addWall(Room room, double x, double y, double z,
                         double width, double height, double depth) {
        Box wall = new Box(width, height, depth);
        wall.setTranslateX(x);
        wall.setTranslateY(y);
        wall.setTranslateZ(z);
        wall.setMaterial(materials.get("wall"));
        wall.setUserData("room");
        sceneRoot.getChildren().add(wall);
    }

    private void addFurniture(Furniture item) {
        double centerX = 300;
        double centerZ = 200;

        Group furnitureGroup = new Group();
        furnitureGroup.setTranslateX(item.getX() - centerX);
        furnitureGroup.setTranslateZ(item.getY() - centerZ);
        furnitureGroup.setUserData("furniture");

        switch (item.getType()) {
            case "Bed":
                createBed(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 20);
                break;
            case "Sofa":
                createSofa(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 30);
                break;
            case "Table":
                createTable(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 35);
                break;
            case "Chair":
                createChair(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 40);
                break;
            case "Cabinet":
                createCabinet(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 60);
                break;
            case "Desk":
                createDesk(furnitureGroup, item);
                furnitureGroup.setTranslateY(floorLevel - 37.5);
                break;
            default:
                Box defaultBox = new Box(item.getWidth(), 30, item.getHeight());
                defaultBox.setMaterial(materials.get("wood"));
                furnitureGroup.getChildren().add(defaultBox);
                furnitureGroup.setTranslateY(floorLevel - 15);
        }

        sceneRoot.getChildren().add(furnitureGroup);
    }

    private void createBed(Group group, Furniture bed) {
        Box mattress = new Box(bed.getWidth() - 10, 15, bed.getHeight() - 10);
        mattress.setMaterial(materials.get("fabric"));
        mattress.setTranslateY(-7.5);
        group.getChildren().add(mattress);

        Box frameBase = new Box(bed.getWidth(), 10, bed.getHeight());
        frameBase.setMaterial(materials.get("wood"));
        frameBase.setTranslateY(5);
        group.getChildren().add(frameBase);

        Box headboard = new Box(10, 40, bed.getHeight());
        headboard.setTranslateX(-bed.getWidth()/2 + 5);
        headboard.setTranslateY(-30);
        headboard.setMaterial(materials.get("wood"));
        group.getChildren().add(headboard);

        Box pillow1 = new Box(30, 8, 40);
        pillow1.setTranslateX(-bed.getWidth()/4);
        pillow1.setTranslateY(-25);
        pillow1.setTranslateZ(15);
        pillow1.setMaterial(materials.get("fabric"));
        group.getChildren().add(pillow1);

        Box pillow2 = new Box(30, 8, 40);
        pillow2.setTranslateX(bed.getWidth()/4);
        pillow2.setTranslateY(-25);
        pillow2.setTranslateZ(15);
        pillow2.setMaterial(materials.get("fabric"));
        group.getChildren().add(pillow2);
    }

    private void createSofa(Group group, Furniture sofa) {
        Box seat = new Box(sofa.getWidth(), 25, sofa.getHeight() - 10);
        seat.setMaterial(materials.get("fabric"));
        seat.setTranslateY(-12.5);
        group.getChildren().add(seat);

        Box back = new Box(sofa.getWidth(), 35, 10);
        back.setMaterial(materials.get("fabric"));
        back.setTranslateY(-35);
        back.setTranslateZ(sofa.getHeight()/2 - 5);
        group.getChildren().add(back);

        Box armLeft = new Box(15, 35, sofa.getHeight());
        armLeft.setTranslateX(-sofa.getWidth()/2 + 7.5);
        armLeft.setTranslateY(-27.5);
        armLeft.setMaterial(materials.get("fabric"));
        group.getChildren().add(armLeft);

        Box armRight = new Box(15, 35, sofa.getHeight());
        armRight.setTranslateX(sofa.getWidth()/2 - 7.5);
        armRight.setTranslateY(-27.5);
        armRight.setMaterial(materials.get("fabric"));
        group.getChildren().add(armRight);

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                Cylinder leg = new Cylinder(2.5, 10);
                leg.setTranslateX(i * (sofa.getWidth()/2 - 10));
                leg.setTranslateY(10);
                leg.setTranslateZ(j * (sofa.getHeight()/2 - 10));
                leg.setMaterial(materials.get("wood"));
                group.getChildren().add(leg);
            }
        }
    }

    private void createTable(Group group, Furniture table) {
        Cylinder top = new Cylinder(table.getWidth()/2 - 5, 5);
        top.setMaterial(materials.get("wood"));
        top.setTranslateY(-table.getHeight()/2);
        group.getChildren().add(top);

        int legCount = 4;
        for (int i = 0; i < legCount; i++) {
            double angle = 2 * Math.PI * i / legCount;
            double x = Math.cos(angle) * (table.getWidth()/3);
            double z = Math.sin(angle) * (table.getWidth()/3);

            Cylinder leg = new Cylinder(3, table.getHeight() - 10);
            leg.setMaterial(materials.get("wood"));
            leg.setTranslateX(x);
            leg.setTranslateY(5);
            leg.setTranslateZ(z);
            group.getChildren().add(leg);
        }
    }

    private void createChair(Group group, Furniture chair) {
        Box seat = new Box(chair.getWidth(), 5, chair.getHeight());
        seat.setMaterial(materials.get("wood"));
        seat.setTranslateY(-chair.getHeight()/2);
        group.getChildren().add(seat);

        Box back = new Box(chair.getWidth(), chair.getHeight(), 5);
        back.setMaterial(materials.get("wood"));
        back.setTranslateY(-chair.getHeight());
        back.setTranslateZ(chair.getHeight()/2 - 2.5);
        group.getChildren().add(back);

        double legOffset = chair.getWidth()/3;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                Cylinder leg = new Cylinder(1.5, chair.getHeight() - 5);
                leg.setMaterial(materials.get("wood"));
                leg.setTranslateX(i * legOffset);
                leg.setTranslateY(5);
                leg.setTranslateZ(j * (chair.getHeight()/2 - 5));
                group.getChildren().add(leg);
            }
        }
    }

    private void createCabinet(Group group, Furniture cabinet) {
        Box body = new Box(cabinet.getWidth(), cabinet.getHeight(), cabinet.getHeight());
        body.setMaterial(materials.get("wood"));
        body.setTranslateY(-cabinet.getHeight()/2);
        group.getChildren().add(body);

        int drawerCount = 3;
        double drawerHeight = cabinet.getHeight() / (drawerCount + 1);
        for (int i = 0; i < drawerCount; i++) {
            Box drawer = new Box(cabinet.getWidth() - 10, drawerHeight - 5, cabinet.getHeight() - 5);
            drawer.setMaterial(materials.get("wood"));
            drawer.setTranslateY(-cabinet.getHeight() + (i + 0.5) * drawerHeight);
            drawer.setTranslateZ(2.5);
            group.getChildren().add(drawer);

            Sphere handle = new Sphere(2.5);
            handle.setMaterial(materials.get("metal"));
            handle.setTranslateX(cabinet.getWidth()/2 - 8);
            handle.setTranslateY(-cabinet.getHeight() + (i + 0.5) * drawerHeight);
            handle.setTranslateZ(cabinet.getHeight()/2 - 2);
            group.getChildren().add(handle);
        }
    }

    private void createDesk(Group group, Furniture desk) {
        Box top = new Box(desk.getWidth(), 5, desk.getHeight());
        top.setMaterial(materials.get("wood"));
        top.setTranslateY(-desk.getHeight()/2);
        group.getChildren().add(top);

        for (int i = -1; i <= 1; i += 2) {
            Cylinder leg = new Cylinder(3, desk.getHeight() - 10);
            leg.setMaterial(materials.get("wood"));
            leg.setTranslateX(i * (desk.getWidth()/2 - 10));
            leg.setTranslateY(5);
            leg.setTranslateZ(desk.getHeight()/2 - 15);
            group.getChildren().add(leg);
        }

        Box panel = new Box(10, desk.getHeight() - 20, desk.getHeight() - 30);
        panel.setMaterial(materials.get("wood"));
        panel.setTranslateX(-desk.getWidth()/2 + 5);
        panel.setTranslateY(-desk.getHeight()/2 + 10);
        group.getChildren().add(panel);
    }

    private void addStructure(Structure structure) {
        double centerX = 300;
        double centerZ = 200;

        if ("Roof".equals(structure.getName())) {
            Cylinder pyramid = new Cylinder(structure.getWidth()/2, 80, 4);
            pyramid.setRotationAxis(Rotate.X_AXIS);
            pyramid.setRotate(90);
            pyramid.setMaterial(materials.get("wood"));
            pyramid.setTranslateX(structure.getX() + structure.getWidth()/2 - centerX);
            pyramid.setTranslateY(floorLevel - roomHeight - 40);
            pyramid.setTranslateZ(structure.getY() + structure.getHeight()/2 - centerZ);
            pyramid.setUserData("structure");
            sceneRoot.getChildren().add(pyramid);
        } else {
            Box structure3D = new Box(structure.getWidth(), 20, structure.getHeight());
            structure3D.setMaterial(materials.get("wood"));
            structure3D.setTranslateX(structure.getX() - centerX);
            structure3D.setTranslateY(floorLevel - 10);
            structure3D.setTranslateZ(structure.getY() - centerZ);
            structure3D.setUserData("structure");
            sceneRoot.getChildren().add(structure3D);
        }
    }
}