package lib;

import java.awt.*;

public abstract class Script {
    public Object2D object;

    public boolean started = false;

    public abstract void start();
    public abstract void update(double deltaTime);
    public abstract void renderUI(Graphics g);
}
