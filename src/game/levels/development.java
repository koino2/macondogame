package game.levels;

import game.prefabs.doors.Door;
import game.prefabs.doors.LevelDoor;
import game.prefabs.doors.Spawnpoint;
import game.scripts.animations.AnimatedTexture;
import game.scripts.player.CameraController;
import game.scripts.ui.DebugText;
import lib.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
    Door door;
    @Override
    public void start() {
        //LevelDoor ld = new LevelDoor(0, 0);
        //addObject(ld);

        Object2D object = new Object2D(0, 0, 100, 100, 0);
        object.setColor(new Color(255, 255, 255));
        object.addScript(new AnimatedTexture("src/assets/boom.png", 2));
        addObject(object);

        Camera camera1 = new Camera(0, 0, 0);
        addObject(camera1);
        camera1.addScript(new CameraController(object));
        camera = camera1;

        Object2D scripts = new Object2D(0, 0, 0, 0, 0);
        addObject(scripts);
        DebugText debugText = new DebugText();
        debugText.textColor = Color.black;
        scripts.addScript(debugText);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void renderUI(Graphics g) {

    }
}
