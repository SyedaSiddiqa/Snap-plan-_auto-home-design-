package autohome.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlueprintCanvas extends Canvas {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Furniture> furniture = new ArrayList<>();
    private final List<Structure> structures = new ArrayList<>();
    private DraggableItem selectedItem;
    private double lastX, lastY;
    private double dragStartX, dragStartY;

    public BlueprintCanvas(double width, double height, int houseWidth, int houseHeight) {
        super(width, height);
        setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");

        Structure outline = new Structure("House Outline", 10, 10, width - 20, height - 20, Color.TRANSPARENT);
        outline.setStroke(Color.BLACK);
        outline.setStrokeWidth(2);
        structures.add(outline);

        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);

        draw();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        draw();
    }

    public void addFurniture(Furniture item) {
        furniture.add(item);
        draw();
    }

    public void addStructure(Structure structure) {
        structures.add(structure);
        draw();
    }

    public void clearAll() {
        rooms.clear();
        furniture.clear();
        structures.clear();

        Structure outline = new Structure("House Outline", 10, 10, getWidth() - 20, getHeight() - 20, Color.TRANSPARENT);
        outline.setStroke(Color.BLACK);
        outline.setStrokeWidth(2);
        structures.add(outline);

        draw();
    }

    public WritableImage snapshot() {
        return snapshot(null, null);
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        for (Structure structure : structures) {
            if (structure.getColor() != Color.TRANSPARENT) {
                gc.setFill(structure.getColor());
                gc.fillRect(structure.getX(), structure.getY(), structure.getWidth(), structure.getHeight());
            }

            if (structure.getStroke() != null) {
                gc.setStroke(structure.getStroke());
                gc.setLineWidth(structure.getStrokeWidth());
                gc.strokeRect(structure.getX(), structure.getY(), structure.getWidth(), structure.getHeight());
            }

            gc.setFill(Color.BLACK);
            gc.fillText(structure.getName(), structure.getX() + 5, structure.getY() + 15);
        }

        for (Room room : rooms) {
            gc.setFill(room.getColor());
            gc.fillRect(room.getX(), room.getY(), room.getWidth(), room.getHeight());
            gc.setStroke(Color.BLACK);
            gc.strokeRect(room.getX(), room.getY(), room.getWidth(), room.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillText(room.getName(), room.getX() + 5, room.getY() + 15);
        }

        for (Furniture item : furniture) {
            gc.setFill(item.getColor());
            gc.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            gc.setStroke(Color.BLACK);
            gc.strokeRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            gc.setFill(Color.BLACK);
            gc.fillText(item.getType(), item.getX() + 5, item.getY() + 15);
        }
    }

    private void handleMousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        dragStartX = e.getX();
        dragStartY = e.getY();
        selectedItem = null;

        for (int i = furniture.size() - 1; i >= 0; i--) {
            Furniture item = furniture.get(i);
            if (containsPoint(item, e.getX(), e.getY())) {
                selectedItem = item;
                return;
            }
        }

        for (int i = rooms.size() - 1; i >= 0; i--) {
            Room room = rooms.get(i);
            if (containsPoint(room, e.getX(), e.getY())) {
                selectedItem = room;
                return;
            }
        }

        for (int i = structures.size() - 1; i >= 0; i--) {
            Structure structure = structures.get(i);
            if (containsPoint(structure, e.getX(), e.getY())) {
                selectedItem = structure;
                return;
            }
        }
    }

    private boolean containsPoint(DraggableItem item, double x, double y) {
        return x >= item.getX() && x <= item.getX() + item.getWidth() &&
                y >= item.getY() && y <= item.getY() + item.getHeight();
    }

    private void handleMouseDragged(MouseEvent e) {
        if (selectedItem == null) return;

        double deltaX = e.getX() - lastX;
        double deltaY = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();

        selectedItem.setPosition(
                selectedItem.getX() + deltaX,
                selectedItem.getY() + deltaY
        );

        draw();
    }

    private void handleMouseReleased(MouseEvent e) {
        if (selectedItem != null &&
                Math.abs(e.getX() - dragStartX) < 5 &&
                Math.abs(e.getY() - dragStartY) < 5) {

            if (selectedItem instanceof Structure) {
                structures.remove(selectedItem);
                structures.add((Structure) selectedItem);
            } else if (selectedItem instanceof Room) {
                rooms.remove(selectedItem);
                rooms.add((Room) selectedItem);
            } else if (selectedItem instanceof Furniture) {
                furniture.remove(selectedItem);
                furniture.add((Furniture) selectedItem);
            }
        }

        selectedItem = null;
        draw();
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public List<Furniture> getFurniture() {
        return Collections.unmodifiableList(furniture);
    }

    public List<Structure> getStructures() {
        return Collections.unmodifiableList(structures);
    }
}