package game.scripts.enemies;

import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.pistol.Pistol;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;
import lib.Script;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TurretScript extends Script {
    public ClosestTargetScript cts;
    public WeaponScript weaponScript;

    public TurretScript(){
        cts = new ClosestTargetScript("player");
        weaponScript = new Pistol(10, 1, 50, "enemy");
        weaponScript.cooldown = 0.5f;
    }
    @Override
    public void start() {
        object.addScript(cts);
        object.addScript(weaponScript);
        cts.moveScript.speed = 0;
        try {
            object.texture = ImageIO.read(new File("src/assets/textures/entities/turret.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    double time = 0;

    float bulletSpeed = 500;
    float prediction = 1;
    Random rng = new Random();
    int randomness = 200;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));

            float hyp = (float) Math.sqrt(xDist * xDist + yDist * yDist);

            if (hyp < 1500) {


                Object2D target = closest;
                float distX = (target.xPos - object.xPos);
                float distY = (target.yPos - object.yPos);

                float distance = (float) Math.hypot(distX, distY);

                float travelTime = distance / bulletSpeed;

                int predictedX = (int) (target.xPos + (target.xVelocity * travelTime * prediction));
                int predictedY = (int) (target.yPos + (target.yVelocity * travelTime * prediction));

                int targetX = predictedX;
                int targetY = predictedY;

                if (randomness != 0) {
                    targetX = predictedX + rng.nextInt(-randomness, randomness);
                    targetY = predictedY + rng.nextInt(-randomness, randomness);
                }
                weaponScript.fire(new Point(targetX, targetY));
            }
        }
    }
}