package game.scripts.npc;

import lib.Script;

import java.awt.*;

public class LookAtScript extends Script {

    Point target;

    @Override
    public void start() {

    }

    public void behaviour(double deltaTime){}

    @Override
    public void update(double deltaTime) {
        behaviour(deltaTime);
        if (target != null){
            int xDist = (int) (target.x - object.globalX);
            int yDist = (int) (target.y - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }
    }
}
