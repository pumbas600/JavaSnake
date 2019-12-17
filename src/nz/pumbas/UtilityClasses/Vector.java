package nz.pumbas.UtilityClasses;

import com.sun.istack.internal.NotNull;

public class Vector
{
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public boolean equals(@NotNull Vector vector)
    {
        return vector.getX() == x && vector.getY() == y;
    }

    public void add(@NotNull Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public String toString() {
        return (this.x + ", " + this.y);
    }
}
