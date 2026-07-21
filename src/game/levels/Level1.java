package game.levels;

import game.prefabs.doors.Spawnpoint;
import game.prefabs.enemies.ShooterEnemy;
import game.prefabs.enemies.Turret;
import game.prefabs.units.CannonPlayer;
import game.prefabs.units.PistolPlayer;
import game.scripts.animations.AnimatedTexture;
import game.scripts.misc.Settings;
import game.scripts.weapons.cannon.Cannon;
import game.scripts.weapons.pistol.Pistol;
import lib.*;
import lib.postProcessEffects.Bloom;
import game.prefabs.enemies.Enemy;
import game.prefabs.Player;
import game.scripts.ui.DebugText;
import lib.postProcessEffects.Vignette;

import javax.imageio.ImageIO;
import java.awt.*;

public class Level1 extends Level {
    boolean won = false;
    double winTimestamp = 0;

    public Scene nextScene = new SampleScene();
    @Override
    public void onWin(){
        if(!won) {
            winTimestamp = time;
            for (int i = 0; i < objects.size(); i++) {
                for (int j = 0; j < objects.get(i).getDescendants().size(); j++) {
                    objects.get(i).getDescendants().get(j).addScript(new AnimatedTexture("src/assets/textures/objects/boom.png", 2));
                }
                objects.get(i).addScript(new AnimatedTexture("src/assets/textures/objects/boom.png", 2));
            }
            won = true;
            Sound sound = new Sound("src/assets/spawn.wav", 1, Settings.volume);
            player.sounds.add(sound);
            sound.play();
        }
        if(time > winTimestamp+2){
            engine.changeScene(nextScene);
        }
    }
    @Override
    public void onLose(){
        System.exit(1);
    }
    @Override
    public void buildObjects() {
        // WALLS
        ambientColor = new Color(74, 74, 76);
        Color wallColor = new Color(86, 156, 216);
        Color floorColor = new Color(65, 73, 73);
        int wallWidth = engine.getWidth();
        int wallHeight = engine.getHeight();
        int wallThickness = 50;

        /*Cannon cannon = new Cannon(0, 0, 10, "player");
        cannon.offsetX = 60;
        cannon.offsetY = 5;
        cannon.bulletColor = new Color(255, 179, 50);
        Player player1 = initPlayer();
        player1.playerControllerScript.weapon = cannon;
        player1.texture = StaticTextures.read("src/assets/textures/entities/cannon-robot-blue.png");*/
        Player player1 = new CannonPlayer();

        Player player2 = new PistolPlayer();

        playerOrder.add(player1);
        playerOrder.add(player2);

        Object2D wall1 = new Object2D(0, wallHeight / 2f, wallThickness, wallHeight + wallThickness, 0);
        wall1.setColor(wallColor);
        wall1.tags.add("wall");
        addObject(wall1);

        Object2D wall2 = new Object2D(wallWidth, wallHeight / 2f, wallThickness, wallHeight + wallThickness, 0);
        wall2.setColor(wallColor);
        wall2.tags.add("wall");
        addObject(wall2);

        Object2D wall3 = new Object2D(wallWidth / 2f, 0, wallWidth + wallThickness, wallThickness, 0);
        wall3.setColor(wallColor);
        wall3.tags.add("wall");
        addObject(wall3);

        Object2D wall4 = new Object2D(wallWidth / 2f, wallHeight, wallWidth + wallThickness, wallThickness, 0);
        wall4.setColor(wallColor);
        wall4.tags.add("wall");
        addObject(wall4);

        Object2D wall5 = new Object2D(400, wallHeight / 2f, wallThickness, (wallHeight + wallThickness) / 2f, 0);
        wall5.setColor(new Color(101, 255, 145, 255));
        wall5.tags.add("wall");
        addObject(wall5);

        Object2D floor = new Object2D(wallWidth / 2f, wallHeight / 2f, wallWidth, wallHeight, 0);
        floor.setColor(floorColor);
        floor.tags.add("noCollision");
        floor.zIndex = -100;
        addObject(floor);

        //Object2D block = new Object2D(300, 300, 100, 100, 0);
        //block.color = new Color(107, 255, 84);
        //block.tags.add("wall");
        //addObject(block);

        Object2D fallback = new Object2D((float) wallWidth / 2, (float) wallHeight / 2, 0, 0, 0);
        addObject(fallback);
        cameraFallbackObject = fallback;

        Bloom bloom = new Bloom();
        postProcessEffects.add(bloom);

        Vignette vignette = new Vignette();
        postProcessEffects.add(vignette);

        wall1.addScript(new DebugText());

        Spawnpoint spawnpoint = new Spawnpoint(120, wallHeight/2f);
        addObject(spawnpoint);
    }

    @Override
    public void initEnemies() {
        ShooterEnemy enemy = new ShooterEnemy(800, 200, 0);
        enemy.collisionScript.collidableObjects = objects;
        levelEnemies.add(enemy);

        Turret enemy2 = new Turret(800, 500, 0);
        enemy2.collisionScript.collidableObjects = objects;
        levelEnemies.add(enemy2);
    }

    @Override
    public Player initPlayer() {
        Player p = new Player(100, 300, 0) {
            @Override
            public void onDeath() {
                onPlayerDeath();
            }
        };
        p.tags.add("player");
        p.collisionScript.collidableObjects = objects;
        p.collisionScript.collidableTags.add("block2");
        p.collisionScript.collidableTags.add("wall");

        p.addScript(new Script() {
            @Override
            public void start() {
                Light light = new Light(0, 0, 200);
                light.color = (new Color(255, 255, 255, 107));
                p.addChild(light);
            }

            @Override
            public void update(double deltaTime) {
            }
        });

        return p;
    }
}