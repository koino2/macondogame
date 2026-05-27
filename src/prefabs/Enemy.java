package prefabs;

import lib.*;
import scripts.CollisionScript;
import scripts.EnemyScript;
import scripts.HealthScript;

import java.awt.*;
import java.util.Random;

public class Enemy extends Object2D {
    static int width = 100;
    static int height = 100;

    public CollisionScript collisionScript;
    public EnemyScript enemyScript;
    public HealthScript healthScript;

    public Enemy(int x, int y, int rotation){
        super(x, y, width, height, rotation);
        this.texture = StaticTextures.square();
        this.color = new Color(255, 39, 39);

        healthScript = new HealthScript() {
            @Override
            public void onDamage(float damageAmount) {
                if (health <= 0) {
                    destroy();
                }
            }
        };
        addScript(healthScript);

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(!other.tags.contains("noCollision")) {
                    resolveCollision(other);
                }
            }
        };
        collisionScript.collidableTags.add("bullet");
        collisionScript.collidableTags.add("wall");
        tags.add("enemy");
        addScript(collisionScript);

        enemyScript = new EnemyScript() {
            double attackTimer = 0;
            float attackInterval = 0.1f;
            float attackRadius = 200;
            float attackDamage = 20;

            Random rng = new Random();
            @Override
            public void behaviour(double deltaTime) {
                attackTimer += deltaTime;

                //if (attackTimer >= attackInterval) {
                //    for (int i = 0; i < object.scene.objects.size(); i++) {
                //        Object2D object2D = object.scene.objects.get(i);
                //        if(object2D.tags.contains("player")) {
                //            if (Math.hypot(object2D.xPos - object.xPos, object2D.yPos - object.yPos) < attackRadius) {
                //                ((Player)(object2D)).healthScript.damage(attackDamage);
                //                attackTimer = 0;
                //            }
                //        }
                //    }
                //}

                Object2D closest = null;
                float closestHypot = 0;
                for(int i = 0; i < object.scene.objects.size(); i++){
                    float distX = object.scene.objects.get(i).xPos - object.xPos;
                    float distY = object.scene.objects.get(i).yPos - object.yPos;
                    float hypot = (float) Math.hypot(distX, distY);
                    if(object.scene.objects.get(i) != object && object.scene.objects.get(i).tags.contains("player")) {
                        if(closest == null || hypot < closestHypot) {
                            closestHypot = hypot;
                            closest = object.scene.objects.get(i);
                            xVelocity = (distX/hypot)*100;
                            yVelocity = (distY/hypot)*100;
                        }
                    }
                }
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

                    float travelTime = distance/bulletSpeed;

                    int predictedX = (int) (closest.xPos + (closest.xVelocity * travelTime));
                    int predictedY = (int) (closest.yPos + (closest.yVelocity * travelTime));

                    int randomness = 200;
                    int targetX = predictedX + rng.nextInt(-randomness,randomness);
                    int targetY = predictedY + rng.nextInt(-randomness,randomness);

                    Bullet bullet = new Bullet(new Point(targetX, targetY), object, 0, 0, "enemy", bulletSpeed);
                    bullet.color = new Color(255, 99, 99);
                    bullet.zIndex = 100;
                    bullet.collisionScript.collidableObjects = object.scene.objects;
                    object.scene.addObject(bullet);
                    attackTimer = 0;
                }
            }
        };
        addScript(enemyScript);
    }
}
