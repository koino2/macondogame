package game.prefabs.units;

import game.levels.Level;
import game.prefabs.Player;
import game.scripts.weapons.chainsaw.ChainsawPullWeapon;
import game.scripts.weapons.chainsaw.ChainsawSlashWeapon;
import lib.Light;
import lib.Script;
import lib.StaticTextures;

import java.awt.*;

public class ChainsawPlayer extends Player {

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
    public void onDeath() {
        ((Level)(scene)).onPlayerDeath();
    }

    public ChainsawPlayer(float x, float y, float rot){
        super(x, y, rot);

        ChainsawPullWeapon chainsawPullWeapon = new ChainsawPullWeapon();
        addScript(chainsawPullWeapon);

        ChainsawSlashWeapon chainsawSlashWeapon = new ChainsawSlashWeapon();
        addScript(chainsawSlashWeapon);

        initPlayer();

        healthScript.maxHealth = 500;
        healthScript.health = 500;

        playerControllerScript.acceleration = 300;

        texture = StaticTextures.read("src/assets/textures/entities/chainsaw.png");
    }
}
