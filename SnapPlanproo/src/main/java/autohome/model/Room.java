package autohome.model;

import javafx.scene.paint.Color;

public class Room implements DraggableItem {
    private final String name;
    private final double width;
    private final double height;
    private final Color color;
    private double x;
    private double y;

    public Room(String name, double width, double height, Color color) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public String getName() { return name; }
    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }
    public Color getColor() { return color; }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}