package nz.pumbas.UtilityClasses;

import java.util.HashMap;

public enum Direction {
    //The origin for the co-ordinate system is in the top left corner with the positive y-axis going downwards
    UP("W", 0, new Vector(0, -1)),
    LEFT("A", 1, new Vector(-1, 0)),
    DOWN("S", 2, new Vector(0, 1)),
    RIGHT("D", 3, new Vector(1, 0));

    //the 1 prevents it from resizing itself from being 'full'
    private static final HashMap<String, Direction> map = new HashMap<>(values().length -1, 1);

    static {
        for (Direction direction : values()) map.put(direction.key, direction);
    }

    private String key;
    private int id;
    private Vector change;

    Direction(String key, int id, Vector change) {
        this.key = key;
        this.id = id;
        this.change = change;
    }

    public int getId()
    {
        return id;
    }

    public Vector getChange() {
        return change;
    }

    public static Direction of(String key) {
        return map.get(key.toUpperCase());
    }
}
