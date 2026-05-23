package lib;

import java.awt.*;

public class Light extends Object2D{
    public float radius;
    public Color color = Color.white;

    public Light(float x, float y, float radius) {
        super(x, y, 0, 0, 0);
        this.radius = radius;
    }
}
