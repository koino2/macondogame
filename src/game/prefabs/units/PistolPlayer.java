package game.prefabs.units;

import game.levels.Level;
import game.prefabs.Player;
import game.scripts.weapons.pistol.Pistol;
import lib.Light;
import lib.Script;
import lib.StaticTextures;

import java.awt.*;

public class PistolPlayer extends Player {

    public void initPlayer() {
        this.tags.add("player");

        addScript(new Script() {
            @Override
            public void start() {
                Light light = new Light(0, 0, 200);
                light.color = (new Color(255, 255, 255, 107));
                addChild(light);
                collisionScript.collidableObjects = scene.objects;
                collisionScript.collidableTags.add("block2");
                collisionScript.collidableTags.add("wall");
            }

            @Override
            public void update(double deltaTime) {
            }
        });
    }

    @Override
    public void onDeath(){
        ((Level)(scene)).onPlayerDeath();
    }

    public PistolPlayer() {
        super(100, 300, 0);

        Pistol pistol = new Pistol(0, 0, 10, "player");
        pistol.offsetX = 60;
        pistol.offsetY = 5;
        pistol.bulletColor = new Color(255, 179, 50);
        initPlayer();
        playerControllerScript.weapon = pistol;
        this.texture = StaticTextures.read("src/assets/textures/entities/robot1-blue.png");
    }
}
