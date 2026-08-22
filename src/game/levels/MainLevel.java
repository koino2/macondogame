package game.levels;

import game.prefabs.doors.Spawnpoint;
import game.prefabs.enemies.ShooterEnemy;
import game.prefabs.enemies.Turret;
import game.prefabs.unitorderselection.UnitOrderSelector;
import game.prefabs.units.CannonPlayer;
import game.prefabs.units.ChainsawPlayer;
import game.prefabs.units.FlamethrowerPlayer;
import game.prefabs.units.PistolPlayer;
import game.scripts.animations.AnimatedTexture;
import game.scripts.misc.Settings;
import lib.*;
import lib.postProcessEffects.Bloom;
import game.prefabs.Player;
import game.scripts.ui.DebugText;
import lib.postProcessEffects.Vignette;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainLevel extends Level {
    boolean won = false;
    double winTimestamp = 0;

    public Scene nextScene = new Level2();

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

    void wall(float x, float y, float width, float height, Color color) {
        Object2D wall = new Object2D(x, y, width, height, 0);
        wall.tags.add("wall");
        wall.setColor(color);
        addObject(wall);
    }

    @Override
    public void buildObjects() {

        ambientColor = new Color(74, 74, 76);
        Color wallColor = new Color(86, 156, 216);
        Color floorColor = new Color(65, 73, 73);
        int wallWidth = engine.getWidth();
        int wallHeight = engine.getHeight();
        int wallThickness = 50;

        wall(100, 300, 200, 200, wallColor);

        Object2D fallback = new Object2D(0, 0, 0, 0, 0);
        addObject(fallback);
        cameraFallbackObject = fallback;

        Bloom bloom = new Bloom();
        postProcessEffects.add(bloom);
        bloom.enabled = false;

        Vignette vignette = new Vignette();
        postProcessEffects.add(vignette);

        fallback.addScript(new DebugText());

        List<Player> cardPlayers = new ArrayList<>();
        cardPlayers.add(new FlamethrowerPlayer(100, 300, 0));
        cardPlayers.add(new ChainsawPlayer(100, 300, 0));
        cardPlayers.add(new PistolPlayer(100, 300, 0));
        cardPlayers.add(new CannonPlayer(100, 300, 0));
        UnitOrderSelector selector = new UnitOrderSelector(0, 0, cardPlayers){
            @Override
            public void onSelected(List<Player> playerList) {
                bloom.enabled = true;
                fallback.xPos = (float) wallWidth / 2;
                fallback.yPos = (float) wallHeight / 2;
            }
        };
        addObject(selector);
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