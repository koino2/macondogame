package prefabs;

import lib.Light;
import lib.Object2D;
import lib.Script;

import java.awt.*;

public class Bullet extends Object2D {

    Light light;

    public Bullet(float xPos, float yPos, Point target, Object2D player, int offsetX, int offsetY) {

        super(player.xPos + ((float) (offsetX * Math.cos(((float) Math.toRadians(player.rotation))) - offsetY * Math.sin(((float) Math.toRadians(player.rotation))))), player.yPos + ((float) (offsetX * Math.sin(((float) Math.toRadians(player.rotation))) + offsetY * Math.cos(((float) Math.toRadians(player.rotation))))), 10, 10, (float) Math.toDegrees(Math.atan2(((target.x - player.xPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos)), ((target.y - player.yPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos))))); // every time i look at this line of code, it feels like it's longer than before
        // we have obtained peak java

        float distanceX = (target.x - player.xPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos);
        float distanceY = (target.y - player.yPos) / (float)Math.hypot(target.x - player.xPos, target.y - player.yPos);

        float hypotenuse = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        distanceX /= hypotenuse;
        distanceY /= hypotenuse;

        color = new Color(255, 179, 50);

        addScript(new BulletLightScript());

        tags.add("bullet");

        xVelocity = distanceX * 500;
        yVelocity = distanceY * 500;
    }
}
class BulletLightScript extends Script{
    Light light;
    @Override
    public void start() {
        light = new Light(0, 0, 5);
        light.color = new Color(255, 232, 126, 255);
        object.children.add(light);
        object.scene.lights.add(light);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void renderUI(Graphics g) {

    }
}