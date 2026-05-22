package lib;

import java.awt.*;

public class Light extends Object2D{
    public float radius;
    public Color color;

    public Light(float x, float y, float radius) {
        super(x, y, radius, radius, 0);
        this.radius = radius;
    }
}
