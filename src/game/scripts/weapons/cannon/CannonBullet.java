package game.scripts.weapons.cannon;

import game.levels.Level;
import game.prefabs.Bullet;
import lib.Light;
import lib.Script;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CannonBullet extends Bullet {

    public CannonBullet(Point target, Point spawn, String exclude){
        super(target, spawn, 20, 20,  0, 10, 5, 0);
        setColor(Color.ORANGE);

        List<String> excludeTags = new ArrayList<>();
        excludeTags.add(exclude);

        addScript(new Script() {
            @Override
            public void start() {
                CannonFlyAnimation fly = new CannonFlyAnimation(excludeTags, ((Level)(scene)).trash);
                addScript(fly);
            }
            @Override public void update(double deltaTime) {}}
        );

        Light light = new Light(0, 0, xSize);
        light.color = new Color(255, 232, 126, 255);
        addScript(new Script() {@Override public void start() {
            addChild(light);
        }@Override public void update(double deltaTime) {}});
    }
}
