package game.levels;

import figma.parser.LevelParser;
import game.prefabs.misc.PressurePlate;
import game.prefabs.unitorderselection.UnitOrderSelector;
import game.prefabs.units.CannonPlayer;
import game.prefabs.units.ChainsawPlayer;
import game.prefabs.units.FlamethrowerPlayer;
import game.prefabs.units.PistolPlayer;
import game.scripts.animations.AnimatedTexture;
import game.scripts.misc.Settings;
import game.scripts.misc.SkyScript;
import lib.*;
import lib.postProcessEffects.Bloom;
import game.prefabs.Player;
import game.scripts.ui.DebugText;
import lib.postProcessEffects.Vignette;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainLevel extends Level {

    @Override
    public void onWin(){
        plate.enabled = true;
    }

    public void next(){
        engine.changeScene(new WinScreen());
    }

    @Override
    public void onLose(){
        engine.changeScene(new LoseScreen());
    }

    void wall(float x, float y, float width, float height, Color color) {
        Object2D wall = new Object2D(x, y, width, height, 0);
        wall.tags.add("wall");
        wall.setColor(color);
        addObject(wall);
    }

    LevelParser levelParser;
    PressurePlate plate;

    @Override
    public void buildObjects() {

        ambientColor = new Color(74, 74, 76);

        plate = new PressurePlate(200, 600, 0) {
            @Override
            public void onTrigger() {
                next();
            }
        };
        addObject(plate);

        levelParser = new LevelParser("src/figma/levels/mainLevel-v6.level", plate);
        levelParser.parse();
        for (int i = 0; i < levelParser.objects.size(); i++) {
            addObject(levelParser.objects.get(i));
        }

        plate.xPos = levelParser.pressurePlate.xPos;
        plate.yPos = levelParser.pressurePlate.yPos;
        plate.color = levelParser.pressurePlate.color;
        plate.rotation = levelParser.pressurePlate.rotation;
        plate.xSize = levelParser.pressurePlate.xSize;
        plate.yPos = levelParser.pressurePlate.ySize;
        plate.zIndex = 100;

        String path = "src/assets/textures/objects/background/1.png";
        Random random = new Random();
        int num = random.nextInt(0, 4);
        if (num == 0){path = "src/assets/textures/objects/background/1.png";}
        if (num == 1){path = "src/assets/textures/objects/background/2.png";}
        if (num == 2){path = "src/assets/textures/objects/background/2.png";}
        if (num == 3){path = "src/assets/textures/objects/background/3.png";}
        Object2D sky = new Object2D(0, 0, 1, 1, 0);
        sky.texture = StaticTextures.read(path);
        sky.xSize = sky.texture.getWidth();
        sky.ySize = sky.texture.getHeight();
        sky.zIndex = -1000;
        sky.tags.add("noCollision");
        sky.color = new Color(150, 150, 150);
        sky.addScript(new SkyScript());
        addObject(sky);

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
        cardPlayers.add(new FlamethrowerPlayer(levelParser.spawnPoint.xPos, levelParser.spawnPoint.yPos, 0));
        cardPlayers.add(new ChainsawPlayer(levelParser.spawnPoint.xPos, levelParser.spawnPoint.yPos, 0));
        cardPlayers.add(new PistolPlayer(levelParser.spawnPoint.xPos, levelParser.spawnPoint.yPos, 0));
        cardPlayers.add(new CannonPlayer(levelParser.spawnPoint.xPos, levelParser.spawnPoint.yPos, 0));
        UnitOrderSelector selector = new UnitOrderSelector(0, 0, cardPlayers){
            @Override
            public void onSelected(List<Player> playerList) {
                bloom.enabled = true;
                fallback.xPos = levelParser.cameraFallback.xPos;
                fallback.yPos = levelParser.cameraFallback.yPos;
            }
        };
        addObject(selector);
    }

    @Override
    public void initEnemies() {
        levelParser.parse();
        for (int i = 0; i < levelParser.enemies.size(); i++) {
            levelParser.enemies.get(i).collisionScript.collidableObjects = objects;
            levelEnemies.add(levelParser.enemies.get(i));
        }
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