package game.scripts.enemies;

import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.pistol.Pistol;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;
import lib.Script;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TurretScript extends Script {
    public ClosestTargetScript cts;
    public WeaponScript weaponScript;

    public TurretScript(){
        cts = new ClosestTargetScript("player");
        weaponScript = new Pistol(10, 1, 50, "enemy");
        weaponScript.cooldown = 0.5f;
    }
    @Override
    public void start() {
        object.addScript(cts);
        object.addScript(weaponScript);
        cts.moveScript.speed = 0;
        try {
            object.texture = ImageIO.read(new File("src/assets/textures/entities/turret.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    double time = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }
        if (closest != null) {
            /*if(weaponScript.canFire()){
                System.out.println("FIRE!"+time);
            }*/
            weaponScript.fireAtObject(closest);
        }
    }
}