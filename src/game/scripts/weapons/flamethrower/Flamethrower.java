package game.scripts.weapons.flamethrower;

import game.prefabs.Player;
import game.scripts.misc.HealthScript;
import game.scripts.weapons.WeaponScript;
import lib.Object2D;
import lib.Script;
import lib.particles.ParticleEmitter;

import java.awt.*;
import java.util.List;

public class Flamethrower extends WeaponScript {

    public double time = 0;

    public boolean firing = false;

    public double timeSinceFire = 0;
    public double timeSinceLastSpawn = 0;
    public double timeSinceLastDamage = 0;

    public String exclude;

    public ParticleEmitter emitter;

    public List<Object2D> damageable;

    public Flamethrower(){

    }

    @Override
    public void start(){
        if (damageable == null) damageable = object.scene.objects;

        emitter = new ParticleEmitter(0, 0);
        emitter.spawnTime = 0.1f;
        object.addChild(emitter);

        object.tags.add("player");

        ((Player)(object)).healthScript.maxHealth = 99999;
        ((Player)(object)).healthScript.health = 99999;
    }

    private float angleDifference(float a, float b) {
        float d = (b - a + 180) % 360 - 180;
        return d < -180 ? d + 360 : d;
    }

    public void damage(){
        for (Object2D obj : damageable){
            if (obj == object) continue;

            float dx = obj.xPos - object.xPos;
            float dy = obj.yPos - object.yPos;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > 150 + (obj.xSize+obj.ySize)/2 + (object.xSize+object.ySize)/2 ) continue;

            float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

            float diff = angleDifference(object.rotation, angle);
            if (Math.abs(diff) > 25) continue;

            for (Script script : obj.scripts){
                if (script instanceof HealthScript){
                    ((HealthScript) script).damage(10);
                    System.out.println("DAMAGE!");
                }
            }
        }
    }

    @Override
    public void behaviour(double deltaTime){
        time += deltaTime;
        timeSinceFire += deltaTime;
        timeSinceLastSpawn += deltaTime;
        timeSinceLastDamage += deltaTime;

        if (timeSinceFire < 0.1f){
            firing = true;
        } else {
            firing = false;
        }

        emitter.enabled = firing;

        if (firing && timeSinceLastDamage > 0.1f) {
            damage();
            timeSinceLastDamage = 0;
        }
    }

    @Override
    public void fireAtObject(Object2D target) {
        fire(new Point((int) target.xPos, (int) target.yPos));
    }

    @Override
    public void fire(Point target) {
        timeSinceFire = 0;
    }
}
