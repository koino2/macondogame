package game.scripts.weapons.chainsaw;

import game.prefabs.Player;
import game.scripts.animations.Animation;
import game.scripts.misc.DelayedAction;
import game.scripts.misc.HealthScript;
import game.scripts.weapons.WeaponScript;
import game.scripts.weapons.flamethrower.FlameDamageScript;
import lib.Input;
import lib.Object2D;
import lib.Script;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChainsawPullWeapon extends WeaponScript {

    public List<Object2D> damageable;
    public String pullExclude;

    public ChainsawPullWeapon(){

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
        object.addScript(new DelayedAction(1){
            @Override
            public void action() {
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
                            ((HealthScript) script).damage(2);

                            if (obj.tags.contains(pullExclude)) return;

                            //TODO the animation goes here
                        }
                    }
                }
            }
        });

    }
}
