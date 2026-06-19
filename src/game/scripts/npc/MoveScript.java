package game.scripts.npc;

import lib.Object2D;
import lib.Script;

public class MoveScript extends Script {
    public Object2D target = null;

    public float speed = 100;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

        if(target != null) {
            float distX = target.xPos - object.xPos;
            float distY = target.yPos - object.yPos;
            float hypot = (float) Math.hypot(distX, distY);

            object.xVelocity = (distX / hypot) * speed;
            object.yVelocity = (distY / hypot) * speed;
        }
        else{
            object.xVelocity *= (float) (0.2f * deltaTime);
            object.yVelocity *= (float) (0.2f * deltaTime);
        }

    }
}
