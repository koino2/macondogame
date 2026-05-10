package scripts;

import lib.Script;

import java.awt.*;

public class FPSTimer extends Script {

    double fps;
    double fpsTimer = 0;
    int fpsFrames = 0;

    @Override
    public void start() {

    }

    long lastRender = System.nanoTime();
    double frameTime = 0;

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void renderUI(Graphics g) {
        fpsTimer += frameTime;
        fpsFrames++;

        if (fpsTimer >= 1.0) {
            fps = fpsFrames;
            fpsFrames = 0;
            fpsTimer = 0;
        }

        g.setColor(Color.black);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        g.drawString("FPS: "+fps, 20, 35);

        frameTime = (System.nanoTime() - lastRender)/1_000_000_000.0;
        lastRender = System.nanoTime();
    }
}