package game.scripts.weapons.cannon;

import game.levels.Level;
import game.prefabs.Bullet;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;

import java.awt.*;
import java.util.Random;

public class Cannon extends WeaponScript {

    Random rng = new Random();
    int randomness = 200;

    float prediction = 1;

    public String excludeTag = "";

    public Color bulletColor = new Color(255, 99, 99);
    public int offsetX = 0;
    public int offsetY = 0;
    public float damage = 10;

    public Cannon(){}

    public Cannon(int randomness, float prediction, float damage, String excludeTag){
        this.randomness = randomness;
        this.prediction = prediction;
        this.excludeTag = excludeTag;
        this.damage = damage;

        cooldown = 0.5f;
    }

    public Point getRotatedPosition(){
        double rad = Math.toRadians(object.globalRotation);

        float rotatedX = (float) (offsetX * Math.cos(rad) - offsetY * Math.sin(rad));
        float rotatedY = (float) (offsetX * Math.sin(rad) + offsetY * Math.cos(rad));

        return new Point(
                Math.round(object.xPos + rotatedX),
                Math.round(object.yPos + rotatedY)
        );
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

            CannonBullet bullet = new CannonBullet(new Point(targetX, targetY), getRotatedPosition(), excludeTag);
            bullet.setColor(bulletColor);
            bullet.zIndex = 100;
            bullet.collisionScript.collidableObjects = object.scene.objects;
            ((Level)(object.scene)).trash.addChild(bullet);

            resetTimer();
        }
    }

    @Override
    public void fireAtObject(Object2D target) {
        fire(new Point((int) target.xPos, (int) target.yPos));
    }
}
