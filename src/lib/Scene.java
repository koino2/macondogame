package lib;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public abstract class Scene extends JPanel {
    public abstract void update(double dt);
    public abstract void start();

    public List<Object2D> objects = new ArrayList<>();

    public boolean running = true;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        render((Graphics2D) g);
    }

    public Scene() {
        start();
        new Thread(() -> {
            long lastTime = System.nanoTime();
            while (true) {
                long now = System.nanoTime();
                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(dt);
                repaint();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void render(Graphics g) {
        objects.sort(Comparator.comparingInt(o -> o.zIndex));
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).render((Graphics2D) g);
        }
    }
}