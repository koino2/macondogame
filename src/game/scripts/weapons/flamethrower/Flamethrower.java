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

        emitter = new FlamethrowerParticleEmitter();
        object.addChild(emitter);

        object.tags.add("player");

        ((Player)(object)).healthScript.maxHealth = 200;
        ((Player)(object)).healthScript.health = 200;
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

            FlameDamageScript flameDamageScript = null;
            HealthScript healthScript = null;

            for (Script script : obj.scripts){
                if (script instanceof HealthScript){
                    ((HealthScript) script).damage(2);
                    healthScript = (HealthScript) (script);
                }
                if (script instanceof FlameDamageScript){
                    flameDamageScript = (FlameDamageScript) (script);
                }
            }

            if (flameDamageScript == null){
                if (healthScript != null) {
                    FlameDamageScript script = new FlameDamageScript(healthScript);
                    obj.addScript(script);
                }
            } else {
                flameDamageScript.flameTime = 5;
            }
        }
    }

    double timeSinceFiringStarted = 0;

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

        if (firing){
            timeSinceFiringStarted += deltaTime;
        } else {
            timeSinceFiringStarted = 0;
        }

        emitter.direction = object.rotation;
        emitter.enabled = timeSinceFiringStarted > 1;
        emitter.xPos = ParticleEmitter.getPointInDirection(object.rotation, 40).x;
        emitter.yPos = ParticleEmitter.getPointInDirection(object.rotation, 40).y;

        if (firing && timeSinceLastDamage > 0.1f && timeSinceFiringStarted > 1) {
            damage();
            timeSinceLastDamage = 0;
        }
    }

    @Override
    public void fire(Point target) {
        timeSinceFire = 0;
    }
}
