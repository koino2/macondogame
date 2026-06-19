
package game.prefabs.enemies;

import game.scripts.enemies.EnemyScript;
import game.scripts.enemies.TurretScript;

public class Turret extends Enemy{
    public static int width = 200;
    public static int height = 200;
    public Turret(int xPos, int yPos, int rot){
        super(xPos, yPos, rot, width, height ,new TurretScript());
    }
}

