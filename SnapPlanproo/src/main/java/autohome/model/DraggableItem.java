package autohome.model;

public interface DraggableItem {
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    void setPosition(double x, double y);
}
