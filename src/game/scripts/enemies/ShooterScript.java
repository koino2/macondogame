package game.scripts.enemies;

import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;
import lib.Script;

import java.awt.*;

public class ShooterScript extends Script {
    public ClosestTargetScript cts;
    public WeaponScript weaponScript;
    @Override
    public void start() {
        object.addScript(cts);
        object.addScript(weaponScript);
    }

    @Override
    public void update(double deltaTime) {
        Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));

            float hyp = (float) Math.sqrt(xDist*xDist + yDist*yDist);
            if (hyp < 500){
                weaponScript.fire(new Point((int) closest.xPos, (int) closest.yPos));
                cts.moveScript.speed = 100;
            } else {
                cts.moveScript.speed = 0;
            }
        }
    }
}