package game.scripts.weapons;

import game.prefabs.Bullet;
import lib.Object2D;

import java.awt.*;
import java.util.Random;

public class Pistol extends WeaponScript{

    Random rng = new Random();
    int randomness = 200;

    float bulletSpeed = 500;

    float prediction = 1;

    public String excludeTag = "";

    public Color bulletColor = new Color(255, 99, 99);
    public int offsetX = 0;
    public int offsetY = 0;
    public float damage = 10;

    public Pistol(){}

    public Pistol(int randomness, float prediction, float damage, String excludeTag){
        this.randomness = randomness;
        this.prediction = prediction;
        this.excludeTag = excludeTag;
        this.damage = damage;
    }

    @Override
    public void fire(Point target) {
        if(canFire()){
            playSound();

            int targetX = target.x;
            int targetY = target.y;

            if(randomness != 0) {
                targetX += rng.nextInt(-randomness, randomness);
                targetY += rng.nextInt(-randomness, randomness);
            }

            Bullet bullet = new Bullet(new Point(targetX, targetY), object, offsetX, offsetY, excludeTag, bulletSpeed, damage);
            bullet.color = bulletColor;
            bullet.zIndex = 100;
            bullet.collisionScript.collidableObjects = object.scene.objects;
            object.scene.addObject(bullet);

            resetTimer();
        }
    }

    @Override
    public void fireAtObject(Object2D target) {
        if(canFire()){
            playSound();

            float distX = (target.xPos - object.xPos);
            float distY = (target.yPos - object.yPos);

            float distance = (float) Math.hypot(distX, distY);

            float travelTime = distance / bulletSpeed;

            int predictedX = (int) (target.xPos + (target.xVelocity * travelTime * prediction));
            int predictedY = (int) (target.yPos + (target.yVelocity * travelTime * prediction));

            int targetX = predictedX;
            int targetY = predictedY;

            if(randomness != 0) {
                targetX = predictedX + rng.nextInt(-randomness, randomness);
                targetY = predictedY + rng.nextInt(-randomness, randomness);
            }

            Bullet bullet = new Bullet(new Point(targetX, targetY), object, offsetX, offsetY, excludeTag, bulletSpeed, damage);
            bullet.color = new Color(255, 99, 99);
            bullet.zIndex = 100;
            bullet.collisionScript.collidableObjects = object.scene.objects;
            object.scene.addObject(bullet);

            resetTimer();
        }
    }
}
