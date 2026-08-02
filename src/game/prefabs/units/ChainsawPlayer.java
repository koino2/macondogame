package game.prefabs.units;

import game.levels.Level;
import game.prefabs.Player;
import game.scripts.weapons.cannon.Cannon;
import game.scripts.weapons.chainsaw.Chainsaw;
import game.scripts.weapons.chainsaw.ChainsawPullWeapon;
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
        initPlayer();
        addScript(chainsawPullWeapon);
        texture = StaticTextures.read("src/assets/textures/entities/cannon-robot-blue.png");
    }
}
