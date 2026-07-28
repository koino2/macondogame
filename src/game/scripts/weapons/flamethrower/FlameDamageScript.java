package game.scripts.weapons.flamethrower;

import game.scripts.misc.HealthScript;
import lib.Script;

public class FlameDamageScript extends Script {

    public float damage = 1;
    public float interval = 0.1f;

    public float flameTime = 5;

    public HealthScript healthScript;

    public FlameDamageScript(HealthScript healthScript){
        this.healthScript = healthScript;
    }

    @Override
    public void start() {

    }

    double time = 0;
    double timeSinceLastDamage = 0;

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        timeSinceLastDamage += deltaTime;
        flameTime -= (float) deltaTime;

        if (timeSinceLastDamage > interval && flameTime > 0){
            healthScript.damage(damage);
            timeSinceLastDamage = 0;
        }
    }
}
