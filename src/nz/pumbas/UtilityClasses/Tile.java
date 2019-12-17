package nz.pumbas.UtilityClasses;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile
{
    private Vector pos;
    private Rectangle rectangle;
    public Tile(int size, Vector pos, Color colour) {
        this.pos = pos;
        this.rectangle = new Rectangle();
        rectangle.setWidth(size);
        rectangle.setHeight(size);
        rectangle.setFill(colour);
    }

    public int getX()
    {
        return pos.getX();
    }

    public int getY()
    {
        return pos.getY();
    }

    public Vector getPos()
    {
        return pos;
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public void setColour(Color colour) {
        rectangle.setFill(colour);
    }
}
