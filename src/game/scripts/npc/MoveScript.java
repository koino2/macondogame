package game.scripts.npc;

import lib.Object2D;
import lib.Script;

public class MoveScript extends Script {
    public Object2D target = null;

    @Override
    public void start() {

    }

    @Override
    public void update(double deltaTime) {

        if(target != null) {
            float distX = target.xPos - object.xPos;
            float distY = target.yPos - object.yPos;
            float hypot = (float) Math.hypot(distX, distY);

            object.xVelocity = (distX / hypot) * 100;
            object.yVelocity = (distY / hypot) * 100;
        }
        else{
            object.xVelocity *= (float) (0.2f * deltaTime);
            object.yVelocity *= (float) (0.2f * deltaTime);
        }

    }
}
