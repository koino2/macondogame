package game.levels;

import game.prefabs.doors.Door;
import game.prefabs.doors.LevelDoor;
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
        float xPos = 200;
        float yPos = 200;

        float xSize = 100;
        float ySize = 100;

        float xSizeAfter = 10;
        float ySizeAfter = 10;

        float directionX = 1f;
        float directionY = 0f;

        float time = 1f;

        /*door = new Door(new Color(188, 188, 200), xPos, yPos, xSize, ySize, xSizeAfter, ySizeAfter, directionX, directionY, time);
        objects.add(door);*/

        LevelDoor ld = new LevelDoor();
        addObject(ld);

        Camera camera1 = new Camera(200, 200, 0);
        addObject(camera1);
        camera = camera1;
    }

    @Override
    public void update(double deltaTime) {
        /*if(Input.isKeyDown(KeyEvent.VK_E)){
            door.open();
        }
        if(Input.isKeyDown(KeyEvent.VK_Q)){
            door.close();
        }*/
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
