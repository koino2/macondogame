package lib;

import javax.swing.*;
import java.awt.*;

public class Engine extends JPanel {

    public Scene currentScene;
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

            currentScene.engine = this;

            if(currentScene.started == false){
                while (getWidth() == 0){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                currentScene.start();
                currentScene.started = true;

                currentScene.startObjects();
            }

            currentScene.update(dt);
            currentScene.updateObjects(dt);
            Input.endFrame();
            repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changeScene (Scene newScene){
        currentScene.destroy();
        currentScene = newScene;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentScene.render(g);
        currentScene.renderUI(g);
        currentScene.renderUIObjects(g);
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        return super.getMousePosition();
    }
}
