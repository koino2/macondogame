package game.prefabs.enemies;

import game.scripts.enemies.EnemyScript;
import game.scripts.enemies.ShooterScript;
import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.pistol.Pistol;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShooterEnemy extends Enemy{
    public ShooterEnemy(int x, int y, int rot){
        super(x, y, rot, 100, 100, new EnemyScript(new Pistol(), new ClosestTargetScript("player")));
        try {
            this.texture = ImageIO.read(new File("src/assets/textures/entities/robot1-red-pcb.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setColor(new Color(255, 255, 255));

        ShooterScript shooterScript = new ShooterScript();
        shooterScript.cts = new ClosestTargetScript("player");
        shooterScript.weaponScript = new Pistol(0, 1, 10, "enemy");
        shooterScript.weaponScript.cooldown = 0.1f;
        addScript(shooterScript);
    }
}
