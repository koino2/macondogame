package lib;

import javax.swing.*;
import java.awt.*;

public class Engine extends JPanel {

    Scene currentScene;
    long lastTime;

    public Engine(Scene scene){
        this.currentScene = scene;
        lastTime = System.nanoTime();

        Input.attach(this);
        new Thread(this::gameLoop).start();
    }

    public void gameLoop(){
        while (true) {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            if(currentScene.started == false){
                currentScene.start();
                currentScene.started = true;
            }

            currentScene.update(dt);
            repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentScene.render((Graphics2D)g);
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        return super.getMousePosition();
    }
}
