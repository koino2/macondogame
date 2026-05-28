package game.scripts.enemies;

import game.scripts.npc.ClosestTargetScript;
import lib.Object2D;
import lib.Script;
import lib.Sound;
import game.prefabs.Bullet;

import java.awt.*;
import java.util.Random;

public class EnemyScript extends Script {

    double attackTimer = 0;
    float attackInterval = 0.1f;

    Random rng = new Random();

    ClosestTargetScript cts;

    @Override
    public void start() {
        cts = new ClosestTargetScript("player");
        object.addScript(cts);
    }

    @Override
    public void update(double deltaTime) {
        attackTimer += deltaTime;

        Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }
        if (closest != null && attackTimer > attackInterval) {
            Sound sound = new Sound("src/assets/shoot.wav");
            sound.setVolume(0.5f);
            sound.play();

            float bulletSpeed = 500;

            float distX = (closest.xPos - object.xPos);
            float distY = (closest.yPos - object.yPos);

            float distance = (float) Math.hypot(distX, distY);

            float travelTime = distance / bulletSpeed;

            int predictedX = (int) (closest.xPos + (closest.xVelocity * travelTime));
            int predictedY = (int) (closest.yPos + (closest.yVelocity * travelTime));

            int randomness = 200;
            int targetX = predictedX + rng.nextInt(-randomness, randomness);
            int targetY = predictedY + rng.nextInt(-randomness, randomness);

            Bullet bullet = new Bullet(new Point(targetX, targetY), object, 0, 0, "enemy", bulletSpeed);
            bullet.color = new Color(255, 99, 99);
            bullet.zIndex = 100;
            bullet.collisionScript.collidableObjects = object.scene.objects;
            object.scene.addObject(bullet);
            attackTimer = 0;
        }
    }
}