package levels;

import lib.Camera;
import lib.Object2D;
import lib.Scene;
import prefabs.Enemy;
import prefabs.Player;
import scripts.CameraController;
import scripts.DebugText;

import java.awt.*;

public class Level1 extends Scene {
    @Override
    public void start() {
        Player player = new Player(100, 300, 0);
        player.tags.add("player");
        player.collisionScript.collidableObjects = objects;
        player.collisionScript.collidableTags.add("block2");
        player.collisionScript.collidableTags.add("wall");
        player.addScript(new DebugText());
        addObject(player);

        //ambientColor = Color.black;

        Camera camera = new Camera(0,0, 0);
        camera.scale = 2f;
        addObject(camera);
        this.camera = camera;
        camera.addScript(new CameraController(player));

        // WALLS
        Color wallColor = new Color(176, 255, 225);
        int wallWidth = engine.getWidth();
        int wallHeight = engine.getHeight();
        int wallThickness = 50;

        Object2D wall1 = new Object2D(0, wallHeight/2f, wallThickness, wallHeight+wallThickness, 0);
        wall1.color = wallColor;
        wall1.tags.add("wall");
        objects.add(wall1);

        Object2D wall2 = new Object2D(wallWidth, wallHeight/2f, wallThickness, wallHeight+wallThickness, 0);
        wall2.color = wallColor;
        wall2.tags.add("wall");
        objects.add(wall2);

        Object2D wall3 = new Object2D(wallWidth/2f, 0, wallWidth+wallThickness, wallThickness, 0);
        wall3.color = wallColor;
        wall3.tags.add("wall");
        objects.add(wall3);

        Object2D wall4 = new Object2D(wallWidth/2f, wallHeight, wallWidth+wallThickness, wallThickness, 0);
        wall4.color = wallColor;
        wall4.tags.add("wall");
        objects.add(wall4);

        Object2D block = new Object2D(300, 300, 100, 100, 0);
        block.color = new Color(107, 255, 84);
        block.tags.add("wall");
        objects.add(block);

        Enemy enemy = new Enemy(500, 400, 0);
        enemy.collisionScript.collidableObjects = objects;
        addObject(enemy);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void renderUI(Graphics g) {

    }
}
