package game.scripts.weapons.pistol;

import game.levels.Level;
import game.prefabs.Bullet;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;

import java.awt.*;
import java.util.Random;

public class Pistol extends WeaponScript {

    Random rng = new Random();
    int randomness = 200;

    float prediction = 1;

    public String excludeTag = "";

    public Color bulletColor = new Color(255, 99, 99);
    public int offsetX = 0;
    public int offsetY = 0;
    public float damage = 10;

    public boolean shake = false;

    public Pistol(){}

    public Pistol(int randomness, float prediction, float damage, String excludeTag){
        this.randomness = randomness;
        this.prediction = prediction;
        this.excludeTag = excludeTag;
        this.damage = damage;

        this.soundVolume = 0.2f;
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

            PistolBullet bullet = new PistolBullet(new Point(targetX, targetY), getRotatedPosition(), excludeTag);
            bullet.setColor(bulletColor);
            bullet.zIndex = 100;
            bullet.collisionScript.collidableObjects = object.scene.objects;
            object.scene.addObject(bullet);

            if (shake) {
                ((Level) (object.scene)).cameraController.shake(1, 0.2f);
            }

            resetTimer();
        }
    }
}
