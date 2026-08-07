package game.scripts.weapons.chainsaw;

import game.scripts.misc.DelayedAction;
import game.scripts.misc.HealthScript;
import game.scripts.weapons.WeaponScript;
import lib.Input;
import lib.Object2D;
import lib.Script;
import lib.Sound;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChainsawSlashWeapon extends WeaponScript {

    public List<Object2D> damageable;

    public ChainsawSlashWeapon(){
        cooldown = 0.5f;
    }

    @Override
    public void start() {
        if (damageable == null){
            damageable = object.scene.objects;
        }
    }

    private float angleDifference(float a, float b) {
        float d = (b - a + 180) % 360 - 180;
        return d < -180 ? d + 360 : d;
    }

    @Override
    public void fire(Point target) {
        if (!canFire()) return;
        resetTimer();

        String path = "src/assets/audio/weapons/chainsaw/slash1.wav";
        Random r = new Random();
        if (r.nextFloat() > 0.5f){
            path = "src/assets/audio/weapons/chainsaw/slash2.wav";
        }
        Sound sound = new Sound(path, 1);
        object.sounds.add(sound);
        sound.play();

        object.addChild(new ChainsawSlashVFX());
        object.addScript(new DelayedAction(0.3f){
            @Override
            public void action() {
                if (fired) return;
                for (Object2D obj : new ArrayList<>(damageable)){
                    if (obj == object) continue;

                    float dx = obj.xPos - object.xPos;
                    float dy = obj.yPos - object.yPos;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist > -50 + (obj.xSize+obj.ySize)/2 + (object.xSize+object.ySize)/2 ) continue;

                    float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

                    float diff = angleDifference(object.rotation, angle);
                    if (Math.abs(diff) > 25) continue;

                    for (Script script : new ArrayList<>(obj.scripts)){
                        if (script instanceof HealthScript){
                            ((HealthScript) script).damage(40);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void behaviour(double deltaTime) {
        if (!live) return;

        if(Input.isMouseDown(MouseEvent.BUTTON1)){
            Point mouseWorldPosition = getMouseWorldPosition();
            if (mouseWorldPosition == null) return;
            fire(mouseWorldPosition);
            shot = true;
            target = mouseWorldPosition;
        }
    }
}
