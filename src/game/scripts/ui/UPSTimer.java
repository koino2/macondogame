package game.scripts.ui;

import lib.Script;

import java.awt.*;

public class UPSTimer extends Script {

    double ups;
    double upsTimer = 0;
    int upsFrames = 0;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

        upsTimer += deltaTime;
        upsFrames++;

        if (upsTimer >= 1.0) {
            ups = upsFrames;
            upsFrames = 0;
            upsTimer = 0;
        }

    }

    @Override
    public void renderUI(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        g.drawString("UPS: "+ ups, 20, 70);
    }
}