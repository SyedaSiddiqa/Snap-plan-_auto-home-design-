package autohome.model;

import javafx.scene.paint.Color;

public class Structure implements DraggableItem {
    private final String name;
    private double x;
    private double y;
    private final double width;
    private final double height;
    private Color color = Color.TRANSPARENT;
    private Color stroke;
    private double strokeWidth = 1;

    public Structure(String name, double x, double y, double width, double height, Color color) {
        this.name = name;
        this.x = x;
        this.y = y;
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
    public Color getStroke() { return stroke; }
    public double getStrokeWidth() { return strokeWidth; }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(Color stroke) {
        this.stroke = stroke;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}