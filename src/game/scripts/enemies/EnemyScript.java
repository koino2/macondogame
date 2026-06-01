package game.scripts.enemies;

import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.Pistol;
import lib.Object2D;
import lib.Script;
import lib.Sound;
import game.prefabs.Bullet;

import java.awt.*;
import java.util.Random;

public class EnemyScript extends Script {

    ClosestTargetScript cts;
    Pistol pistol;

    @Override
    public void start() {

        cts = new ClosestTargetScript("player");
        object.addScript(cts);

        pistol = new Pistol(200, 1, "enemy");
        object.addScript(pistol);

    }

    @Override
    public void update(double deltaTime) {

        Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }
        if (closest != null) {
            pistol.fireAtObject(closest);
        }

    }
}