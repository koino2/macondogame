package game.levels;

import game.scripts.animations.Animation;
import lib.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
        animation.addKeyframe(1f, 200, 200, 0);
        animation.addKeyframe(1f, 300, 200, 90);
        animation.addKeyframe(1f, 200, 300, 180);
        animation.addKeyframe(1f, 300, 300, 0);

        Object2D object = new Object2D(200, 200, 100, 100, 0);
        try {
            object.texture = ImageIO.read(new File("src/assets/robot1-blue.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        object.color = new Color(255, 255, 255);
        object.addScript(animation);
        addObject(object);

        Camera camera1 = new Camera(200, 200, 0);
        addObject(camera1);
        camera = camera1;

        animation.play();
    }

    @Override
    public void update(double deltaTime) {
        if(Input.isKeyDown(KeyEvent.VK_E)){
            animation.play();
            animation.time = 1f;
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
