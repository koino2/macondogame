package prefabs;

import lib.Light;
import lib.Object2D;
import lib.Script;
import scripts.CollisionScript;
import scripts.HealthScript;

import java.awt.*;

public class Bullet extends Object2D {

    Light light;
    public float damage = 10;

    public CollisionScript collisionScript;

    public Bullet(Point target, Object2D player, int offsetX, int offsetY, String exclude, float speed) {

        super(player.xPos + ((float) (offsetX * Math.cos(((float) Math.toRadians(player.rotation))) - offsetY * Math.sin(((float) Math.toRadians(player.rotation))))), player.yPos + ((float) (offsetX * Math.sin(((float) Math.toRadians(player.rotation))) + offsetY * Math.cos(((float) Math.toRadians(player.rotation))))), 10, 10, (float) Math.toDegrees(Math.atan2(((target.x - player.xPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos)), ((target.y - player.yPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos))))); // every time i look at this line of code, it feels like it's longer than before
        // we have obtained peak java

        float distanceX = (target.x - player.xPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos);
        float distanceY = (target.y - player.yPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos);

        float hypotenuse = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        distanceX /= hypotenuse;
        distanceY /= hypotenuse;

        color = new Color(255, 179, 50);

        collisionScript = new CollisionScript() {
            @Override
            public void onCollide(Object2D other) {
                if(!other.tags.contains("noCollision") && !other.tags.contains(exclude)){
                    resolveCollision(other);
                    for (Script script : other.scripts) {
                        if(script instanceof HealthScript) {
                            ((HealthScript)(script)).damage(damage);
                        }
                    }
                    destroy();
                }
            }
        };
        addScript(collisionScript);

        addScript(new Script() {
            @Override
            public void start() {
                light = new Light(0, 0, 10);
                light.color = new Color(255, 232, 126, 255);
                object.addChild(light);
                //scene.addObject(light);
            }

            @Override
            public void update(double deltaTime) {

            }

            @Override
            public void renderUI(Graphics g) {

            }
        });

        tags.add("bullet");
        tags.add("noCollision");

        xVelocity = distanceX * speed;
        yVelocity = distanceY * speed;
    }
}