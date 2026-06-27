package game.scripts.weapons.cannon;

import game.prefabs.Bullet;
import lib.Light;
import lib.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CannonBullet extends Bullet {
    public CannonBullet(Point target, Point spawn, String exclude){
        super(target, spawn, 20, 20,  0, 10, 5, 500);
        setColor(Color.ORANGE);

        List<String> excludeTags = new ArrayList<>();
        excludeTags.add(exclude);

        ExplodeOnCollision explode = new ExplodeOnCollision();
        explode.excludeTags = excludeTags;
        addScript(explode);

        Light light = new Light(0, 0, xSize);
        light.color = new Color(255, 232, 126, 255);
        addScript(new Script() {@Override public void start() {
            addChild(light);
        }@Override public void update(double deltaTime) {}});
    }
}
