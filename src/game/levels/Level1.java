package game.levels;

import game.prefabs.Ghost;
import game.scripts.player.recording.PlayerRecorder;
import game.scripts.player.recording.Recording;
import game.scripts.player.recording.RecordingFrame;
import game.scripts.player.recording.RecordingReader;
import game.scripts.weapons.Pistol;
import lib.*;
import lib.postProcessEffects.Bloom;
import game.prefabs.Enemy;
import game.prefabs.Player;
import game.scripts.player.CameraController;
import game.scripts.ui.DebugText;

import java.awt.*;

/*

public class Level1 extends Scene {

    List<Recording> recordings = new ArrayList<>();

    Player player;

    double respawnTime = 999;

    boolean enemiesSpawned = false;

    public void onDeathScene(){
        objects.remove(player);

        player.healthScript.health = 100;
        player.xPos = 100;
        player.yPos = 300;
        addObject(player);

        Ghost ghost = new Ghost(player.playerRecorder.recording);
        ghost.texture = player.texture;
        addObject(ghost);

        player.playerRecorder.stopRecording();

        respawnTime = 10;
    }

    @Override
    public void start() {
        player = new Player(100, 300, 0){
            @Override
            public void onDeath(){
                onDeathScene();
            }
        };
        player.tags.add("player");
        player.collisionScript.collidableObjects = objects;
        player.collisionScript.collidableTags.add("block2");
        player.collisionScript.collidableTags.add("wall");
        player.addScript(new DebugText());
        addObject(player);

        ambientColor = new Color(40, 53, 55);

        Camera camera = new Camera(0,0, 0);
        camera.scale = 2f;
        addObject(camera);
        this.camera = camera;
        camera.addScript(new CameraController(player));

        // WALLS
        Color wallColor = new Color(255, 255, 255);
        Color floorColor = new Color(65, 73, 73);
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

        Object2D floor = new Object2D(wallWidth/2f, wallHeight/2f, wallWidth, wallHeight, 0);
        floor.color = floorColor;
        floor.tags.add("noCollision");
        floor.zIndex = -100;
        objects.add(floor);

        Object2D block = new Object2D(300, 300, 100, 100, 0);
        block.color = new Color(107, 255, 84);
        block.tags.add("wall");
        objects.add(block);

        Light playerLight = new Light(0,0,300);
        playerLight.color = new Color(255,255,255,255);
        player.addChild(playerLight);
        Light playerLight2 = new Light(0,0,800);
        playerLight2.color = new Color(255, 255, 255, 50);
        player.addChild(playerLight2);

        Bloom bloom = new Bloom();
        postProcessEffects.add(bloom);
    }

    double time = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        respawnTime -= deltaTime;

        if(time > 10d && !enemiesSpawned) {
            Enemy enemy = new Enemy(500, 400, 0);
            enemy.collisionScript.collidableObjects = objects;
            addObject(enemy);
            enemiesSpawned = true;

            player.healthScript.damage(1000);
        }

        if(respawnTime <= 0){
            objects.remove(player);
            player.healthScript.health = 100;
            player.xPos = 100;
            player.yPos = 300;
            addObject(player);

            respawnTime = 99999;
        }
    }

    @Override
    public void renderUI(Graphics g) {

    }
}
*/

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

        Object2D floor = new Object2D(wallWidth/2f, wallHeight/2f, wallWidth, wallHeight, 0);
        floor.color = floorColor;
        floor.tags.add("noCollision");
        floor.zIndex = -100;
        objects.add(floor);

        Object2D block = new Object2D(300, 300, 100, 100, 0);
        block.color = new Color(107, 255, 84);
        block.tags.add("wall");
        objects.add(block);

        Bloom bloom = new Bloom();
        //postProcessEffects.add(bloom);
    }

    @Override
    public void spawnEnemies() {
        Enemy enemy = new Enemy(500, 400, 0);
        enemy.collisionScript.collidableObjects = objects;
        addObject(enemy);
    }

    @Override
    public Player createPlayer() {
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
        player.addScript(new DebugText());

        Camera camera = new Camera(0,0, 0);
        camera.scale = 2f;
        addObject(camera);
        this.camera = camera;
        camera.addScript(new CameraController(player));

        return player;
    }
}