package game.prefabs.enemies;

import game.scripts.enemies.ShooterScript;
import game.scripts.npc.ClosestTargetScript;
import game.scripts.weapons.pistol.Pistol;
import lib.StaticTextures;

import java.awt.*;

public class Nanobot extends Enemy{
    public Nanobot(int x, int y, int rot){
        super(x, y, rot, 50, 50);

        this.texture = StaticTextures.read("src/assets/textures/entities/nanobot.png");

        setColor(new Color(255, 255, 255));

        healthScript.health = 25;
        healthScript.maxHealth = 25;

        ShooterScript shooterScript = new ShooterScript();
        shooterScript.cts = new ClosestTargetScript("player");
        shooterScript.weaponScript = new Pistol(0, 0, 1, "enemy");
        shooterScript.weaponScript.cooldown = 0.5f;
        shooterScript.weaponScript.soundVolume = 0.2f;
        shooterScript.weaponScript.soundPath = "src/assets/audio/enemies/nanobot/nanobot-shoot.wav";
        addScript(shooterScript);

        tags.add("noCollision");
    }
}
