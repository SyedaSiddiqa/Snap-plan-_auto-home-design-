package autohome.model;

import javafx.scene.paint.Color;

public class Furniture implements DraggableItem {
    private final String type;
    private final double width;
    private final double height;
    private final Color color;
    private double x;
    private double y;

    public Furniture(String type, double width, double height, Color color) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public String getType() { return type; }
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