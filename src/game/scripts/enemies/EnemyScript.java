package game.scripts.enemies;

import game.scripts.weapons.WeaponScript;
import lib.Script;
import lib.Sound;

public class EnemyScript extends Script {

    Script behaviour;
    WeaponScript weapon;

    String spawnSoundPath = "src/assets/spawn.wav";

    public EnemyScript(WeaponScript weapon, Script behaviour){
        this.weapon = weapon;
        this.behaviour = behaviour;
    }

    @Override
    public void start() {

        Sound spawnSound = new Sound(spawnSoundPath, 1);
        object.sounds.add(spawnSound);
        spawnSound.setVolume(2f);
        spawnSound.play();

        object.addScript(behaviour);

        object.addScript(weapon);

    }

    @Override
    public void update(double deltaTime) {

        /*Object2D closest = cts.closest;
        if (closest != null) {
            int xDist = (int) (closest.globalX - object.globalX);
            int yDist = (int) (closest.globalY - object.globalY);
            object.rotation = (float) Math.toDegrees(Math.atan2(yDist, xDist));
        }
        if (closest != null) {
            weapon.fireAtObject(closest);
        }*/

    }
}