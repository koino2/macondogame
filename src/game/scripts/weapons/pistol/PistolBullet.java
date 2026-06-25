package game.scripts.weapons.pistol;

import game.prefabs.Bullet;
import game.scripts.weapons.bulletScripts.DamageOnCollision;
import game.scripts.weapons.bulletScripts.DestroyOnCollision;
import lib.Light;
import lib.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PistolBullet extends Bullet {
    public PistolBullet(Point target, Point spawn, String exclude){
        super(target, spawn, 10, 10,  0, 10, 5, 500);
        setColor(Color.ORANGE);

        List<String> excludeTags = new ArrayList<>();
        excludeTags.add(exclude);

        DamageOnCollision damage = new DamageOnCollision();
        damage.excludeTags = excludeTags;

        DestroyOnCollision destroy = new DestroyOnCollision();
        destroy.excludeTags = excludeTags;

        addScript(damage);
        addScript(destroy);

        Light light = new Light(0, 0, 10);
        light.color = new Color(255, 232, 126, 255);
        addScript(new Script() {@Override public void start() {
                addChild(light);
            }@Override public void update(double deltaTime) {}});
    }
}
