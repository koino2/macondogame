package game.levels;

import lib.*;
import lib.postProcessEffects.Bloom;
import game.prefabs.Enemy;
import game.prefabs.Player;
import game.scripts.ui.DebugText;

import java.awt.*;

public class Level1 extends Level {
    @Override
    public void buildObjects() {
        // WALLS
        Color wallColor = new Color(255, 255, 255);
        Color floorColor = new Color(65, 73, 73);
        int wallWidth = engine.getWidth();
        int wallHeight = engine.getHeight();
        int wallThickness = 50;

        Object2D wall1 = new Object2D(0, wallHeight/2f, wallThickness, wallHeight+wallThickness, 0);
        wall1.color = wallColor;
        wall1.tags.add("wall");
        addObject(wall1);

        Object2D wall2 = new Object2D(wallWidth, wallHeight/2f, wallThickness, wallHeight+wallThickness, 0);
        wall2.color = wallColor;
        wall2.tags.add("wall");
        addObject(wall2);

        Object2D wall3 = new Object2D(wallWidth/2f, 0, wallWidth+wallThickness, wallThickness, 0);
        wall3.color = wallColor;
        wall3.tags.add("wall");
        addObject(wall3);

        Object2D wall4 = new Object2D(wallWidth/2f, wallHeight, wallWidth+wallThickness, wallThickness, 0);
        wall4.color = wallColor;
        wall4.tags.add("wall");
        addObject(wall4);

        Object2D floor = new Object2D(wallWidth/2f, wallHeight/2f, wallWidth, wallHeight, 0);
        floor.color = floorColor;
        floor.tags.add("noCollision");
        floor.zIndex = -100;
        addObject(floor);

        Object2D block = new Object2D(300, 300, 100, 100, 0);
        block.color = new Color(107, 255, 84);
        block.tags.add("wall");
        addObject(block);

        Object2D fallback = new Object2D((float) wallWidth /2, (float) wallHeight /2, 0, 0, 0);
        addObject(fallback);
        cameraFallbackObject = fallback;

        Bloom bloom = new Bloom();
        postProcessEffects.add(bloom);

        block.addScript(new DebugText());
    }

    @Override
    public void initEnemies() {
        Enemy enemy = new Enemy(800, 200, 0);
        enemy.collisionScript.collidableObjects = objects;
        levelEnemies.add(enemy);

        Enemy enemy2 = new Enemy(800, 500, 0);
        enemy2.collisionScript.collidableObjects = objects;
        levelEnemies.add(enemy2);
    }

    @Override
    public Player initPlayer() {
        player = new Player(100, 300, 0){
            @Override
            public void onDeath(){
                onPlayerDeath();
            }
        };
        player.tags.add("player");
        player.collisionScript.collidableObjects = objects;
        player.collisionScript.collidableTags.add("block2");
        player.collisionScript.collidableTags.add("wall");

        /*Camera camera = new Camera(0,0, 0);
        camera.scale = 2f;
        addObject(camera);
        this.camera = camera;
        camera.addScript(new CameraController(player));*/

        return player;
    }
}