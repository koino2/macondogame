package lib;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public abstract class Scene {
    public abstract void start();
    public abstract void update(double deltaTime);

    public List<Object2D> objects = new ArrayList<>();

    public boolean started = false;

    public void defaultUpdate(double dt){
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(dt);
        }
    }

    public void render(Graphics g) {
        objects.sort(Comparator.comparingInt(o -> o.zIndex));
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).render((Graphics2D) g);
        }
    }
}