package game.prefabs.enemies;

import game.scripts.enemies.ShooterScript;
import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.pistol.Pistol;
import lib.StaticTextures;

import java.awt.*;

public class ShooterEnemy extends Enemy{
    public ShooterEnemy(int x, int y, int rot){
        super(x, y, rot, 100, 100);

        this.texture = StaticTextures.read("src/assets/textures/entities/robot1-red-pcb.png");

        setColor(new Color(255, 255, 255));

        ShooterScript shooterScript = new ShooterScript();
        shooterScript.cts = new ClosestTargetScript("player");
        shooterScript.weaponScript = new Pistol(0, 1, 10, "enemy");
        shooterScript.weaponScript.cooldown = 0.1f;
        addScript(shooterScript);
    }
}
