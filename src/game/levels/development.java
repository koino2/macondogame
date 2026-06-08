package game.levels;

import game.scripts.animations.Animation;
import lib.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class development extends Scene {
    public static void main(String[] args) {
        JFrame window = new JFrame("Untitled Macondo Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);

        development scene = new development();
        Engine engine = new Engine(scene);
        window.setContentPane(engine);
        window.setVisible(true);
    }
    Animation animation;
    @Override
    public void start() {
        animation = new Animation();

        float xPos = 200;
        float yPos = 200;

        float xSize = 100;
        float ySize = 100;

        float xSizeAfter = 10;
        float ySizeAfter = 100;

        float directionX = -1f;
        float directionY = 0f;

        float time = 1f;

        Object2D object = new Object2D(xPos, yPos, xSize, ySize, 0);
        object.color = new Color(255, 82, 82);
        object.addScript(animation);
        addObject(object);

        animation.addKeyframe(0f, xPos, yPos, xSize, ySize, 0);
        animation.addKeyframe(
                time,
                xPos + ((xSizeAfter-xSize)/2)*directionX,
                yPos + ((ySizeAfter-ySize)/2)*directionY,
                xSizeAfter,
                ySizeAfter,
                0
        );

        Camera camera1 = new Camera(200, 200, 0);
        addObject(camera1);
        camera = camera1;
    }

    @Override
    public void update(double deltaTime) {
        if(Input.isKeyDown(KeyEvent.VK_E)){
            animation.play();
            //animation.time = 1f;
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
