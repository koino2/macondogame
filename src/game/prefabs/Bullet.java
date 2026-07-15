package game.prefabs;

import game.scripts.weapons.BulletScript;
import lib.Light;
import lib.Object2D;
import lib.Script;
import lib.CollisionScript;
import game.scripts.misc.HealthScript;

import java.awt.*;

public class Bullet extends Object2D {

    public CollisionScript collisionScript;
    public String excludeTag;

    public float damage;
    public float lifetime_ = 5f;
    public float speed;

    public Point target;

    public Bullet(Point target, Point spawn, float sizeX, float sizeY, float rotation, float damage, float lifetime, float speed) {

        /*
        unfortunately while refactoring the weapon system we lost this gem:
        super(player.xPos + ((float) (offsetX * Math.cos(((float) Math.toRadians(player.globalRotation))) - offsetY * Math.sin(((float) Math.toRadians(player.globalRotation))))), player.yPos + ((float) (offsetX * Math.sin(((float) Math.toRadians(player.globalRotation))) + offsetY * Math.cos(((float) Math.toRadians(player.globalRotation))))), 10, 10, (float) Math.toDegrees(Math.atan2(((target.x - player.xPos) / (float) Math.hypot(target.x - player.xPos, target.y - player.yPos)), ((target.y - player.yPos) / (float) Math.hypot(target.x - player.xPos, target.y - player.yPos))))); // every time i look at this line of code, it feels like it's longer than before
        // we have obtained peak java
         */

        super(spawn.x, spawn.y, sizeX, sizeY, rotation);

        this.damage = damage;
        this.lifetime_ = lifetime;
        this.speed = speed;
        this.target = target;

        float distanceX = (target.x - spawn.x) / (float) Math.hypot(target.x - spawn.x, target.y - spawn.y);
        float distanceY = (target.y - spawn.y) / (float) Math.hypot(target.x - spawn.x, target.y - spawn.y);

        float hypotenuse = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        distanceX /= hypotenuse;
        distanceY /= hypotenuse;

        color = new Color(255, 179, 50);

        tags.add("bullet");
        tags.add("noCollision");

        xVelocity = distanceX * speed;
        yVelocity = distanceY * speed;

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if (destroyed) return;
                if (!shouldCollideWith(other)) return;
                //resolveCollision(other);

                for (Script script : object.scripts) {
                    if (script instanceof BulletScript bulletScript) {
                        bulletScript.onBulletCollide(other);
                        if (destroyed) return;
                    }
                }
            }
        };
        addScript(collisionScript);
        addScript(new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {
                lifetime_ -= deltaTime;
                if (lifetime_ <= 0) destroy();
            }
        });
    }

    public boolean shouldCollideWith(Object2D other) {
        if (other.tags.contains("noCollision")) return false;
        if (excludeTag != null && other.tags.contains(excludeTag)) return false;
        return true;
    }
}