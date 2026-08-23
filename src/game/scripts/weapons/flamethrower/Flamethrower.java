package game.scripts.weapons.flamethrower;

import game.levels.Level;
import game.scripts.misc.HealthScript;
import game.scripts.misc.Settings;
import game.scripts.weapons.WeaponScript;
import lib.Input;
import lib.Object2D;
import lib.Script;
import lib.Sound;
import lib.particles.ParticleEmitter;

import java.awt.*;
import java.awt.event.MouseEvent;
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

    public Sound flamethrowerSound;
    public Sound end;

    public Flamethrower(){

    }

    @Override
    public void start(){
        if (damageable == null) damageable = object.scene.objects;

        emitter = new FlamethrowerParticleEmitter();
        object.addChild(emitter);

        object.tags.add("player");

        flamethrowerSound = new Sound("src/assets/audio/weapons/flamethrower/flamethrower.wav", 0.5f, Settings.volume);
        object.sounds.add(flamethrowerSound);
        flamethrowerSound.setVolume(flamethrowerSound.defaultVolume);
        flamethrowerSound.stop();
        end = new Sound("src/assets/audio/weapons/flamethrower/flamethrower-end.wav", 0.5f, Settings.volume);
        object.sounds.add(end);
        flamethrowerSound.setVolume(flamethrowerSound.defaultVolume);
        end.stop();
    }

    private float angleDifference(float a, float b) {
        float d = (b - a + 180) % 360 - 180;
        return d < -180 ? d + 360 : d;
    }

    public void damage(){
        for (Object2D obj : damageable){
            if (obj.tags.contains(exclude)) return;/*
            for (int i = 0; i < obj.tags.size(); i++) {
                System.out.println(obj.tags.get(i));
            }*/
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

    boolean firedBefore = false;

    @Override
    public void behaviour(double deltaTime){
        time += deltaTime;
        timeSinceFire += deltaTime;
        timeSinceLastSpawn += deltaTime;
        timeSinceLastDamage += deltaTime;

        if (object.destroyed ) return;

        boolean firingLastFrame = firing;

        if (timeSinceFire < 0.1f){
            firing = true;
        } else {
            firing = false;
        }

        if (firing){
            timeSinceFiringStarted += deltaTime;

            flamethrowerSound.resume();
            end.stop();
        } else {
            timeSinceFiringStarted = 0;

            flamethrowerSound.stop();
            if (firingLastFrame && firedBefore) {
                end.play();
            }
        }

        emitter.direction = object.rotation;
        emitter.enabled = timeSinceFiringStarted > 1;
        emitter.xPos = ParticleEmitter.getPointInDirection(object.rotation, 40).x;
        emitter.yPos = ParticleEmitter.getPointInDirection(object.rotation, 40).y;

        if (timeSinceFiringStarted > 1) {
            ((Level) (object.scene)).cameraController.shake(1, 0.2f);
        }

        if (firing && timeSinceLastDamage > 0.1f && timeSinceFiringStarted > 1) {
            damage();
            timeSinceLastDamage = 0;
        }

        if (emitter.parent == null || emitter.parent.destroyed || !object.children.contains(emitter)){
            object.addChild(emitter);
        }

        if (!live) return;

        if(Input.isMouseDown(MouseEvent.BUTTON1)){
            Point mouseWorldPosition = getMouseWorldPosition();
            if (mouseWorldPosition == null) return;
            fire(mouseWorldPosition);
            shot = true;
            target = mouseWorldPosition;
        }
    }

    @Override
    public void fire(Point target) {
        timeSinceFire = 0;
        firedBefore = true;
    }

    @Override
    public void onDestroy(){
        flamethrowerSound.stop();
    }
}
