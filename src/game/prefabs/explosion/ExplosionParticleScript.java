package game.prefabs.explosion;

import game.scripts.animations.Animation;
import lib.Light;
import lib.Script;

import java.awt.*;

public class ExplosionParticleScript extends Script {
    double time = 0;

    @Override
    public void start() {
        Light light = new Light(0, 0, 10);
        light.color = object.color;
        object.addChild(light);
        light.addScript(new Script() {
            @Override
            public void start() {

            }

            @Override
            public void update(double deltaTime) {
                light.radius = light.parent.xSize/2;
                light.color = light.parent.color;
            }
        });
    }

    public Color clampColor(int r, int g, int b, int a){
        return new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b)),
                a
        );
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        if (time > 0.25f){
            object.xSize = (float) (time * 40);
            object.ySize = (float) (time * 40);
        }
        if (time > 1) {
            Animation anim = new Animation();
            Color a = new Color(182, 182, 182, 255);
            Color b = new Color(78, 78, 78, 103);
            float t = (float) Math.max(Math.min(time-1, 1), 0);
            object.color = clampColor(
                    (int) anim.lerp(a.getRed(), b.getRed(), t),
                    (int) anim.lerp(a.getGreen(), b.getGreen(), t),
                    (int) anim.lerp(a.getBlue(), b.getBlue(), t),
                    (int) anim.lerp(a.getAlpha(), b.getAlpha(), t)
            );
        }
    }
}
